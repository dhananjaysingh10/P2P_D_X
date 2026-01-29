import { useState, useEffect } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { getCampaignById, createTransaction } from '../services/api';
import { AUTH_CONFIG } from '../config';

export default function CampaignDetail() {
    const navigate = useNavigate();
    const { id } = useParams();
    const [campaign, setCampaign] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [showDonateModal, setShowDonateModal] = useState(false);
    const [donationData, setDonationData] = useState({
        amount: '',
        upiId: '',
        isAnonymous: false,
        donorMessage: ''
    });
    const [donating, setDonating] = useState(false);
    const [donationSuccess, setDonationSuccess] = useState(false);

    useEffect(() => {
        const email = localStorage.getItem(AUTH_CONFIG.STORAGE_KEY);
        if (!email) {
            navigate('/');
            return;
        }
        fetchCampaign();
    }, [navigate, id]);

    const fetchCampaign = async () => {
        try {
            setLoading(true);
            const data = await getCampaignById(id);
            setCampaign(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const formatCurrency = (amount) => {
        return new Intl.NumberFormat('en-IN', {
            style: 'currency',
            currency: 'INR',
            maximumFractionDigits: 0
        }).format(amount || 0);
    };

    const handleDonationChange = (e) => {
        const { name, value, type, checked } = e.target;
        setDonationData({
            ...donationData,
            [name]: type === 'checkbox' ? checked : value
        });
    };

    const handleDonate = async (e) => {
        e.preventDefault();
        setDonating(true);
        setError('');

        try {
            const transactionData = {
                transactionId: `TXN_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`,
                donorId: 1, // TODO: Get from logged-in user
                campaignId: parseInt(id),
                amount: parseFloat(donationData.amount),
                upiId: donationData.upiId,
                status: 'PENDING',
                isAnonymous: donationData.isAnonymous,
                donorMessage: donationData.donorMessage
            };

            await createTransaction(transactionData);
            setDonationSuccess(true);
            setShowDonateModal(false);

            // Refresh campaign data
            fetchCampaign();
        } catch (err) {
            setError(err.message);
        } finally {
            setDonating(false);
        }
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 flex items-center justify-center">
                <div className="w-12 h-12 border-4 border-purple-500 border-t-transparent rounded-full animate-spin"></div>
            </div>
        );
    }

    if (error && !campaign) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 flex items-center justify-center p-4">
                <div className="bg-red-500/20 border border-red-500/50 text-red-300 px-6 py-4 rounded-xl max-w-md text-center">
                    <h2 className="text-xl font-semibold mb-2">Error</h2>
                    <p>{error}</p>
                    <Link to="/campaigns" className="inline-block mt-4 text-purple-400 hover:text-purple-300">
                        ← Back to Campaigns
                    </Link>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900">
            {/* Header */}
            <header className="bg-white/5 backdrop-blur-xl border-b border-white/10">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex justify-between items-center h-16">
                        <Link to="/campaigns" className="flex items-center text-slate-400 hover:text-white transition-colors">
                            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
                            </svg>
                            Back to Campaigns
                        </Link>
                    </div>
                </div>
            </header>

            {/* Main Content */}
            <main className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
                {donationSuccess && (
                    <div className="bg-emerald-500/20 border border-emerald-500/50 text-emerald-300 px-6 py-4 rounded-xl mb-8 flex items-center">
                        <svg className="w-6 h-6 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                        Thank you for your donation! Your transaction is being processed.
                    </div>
                )}

                {campaign && (
                    <div className="bg-white/10 backdrop-blur-xl rounded-3xl overflow-hidden border border-white/10">
                        {/* Campaign Header Image */}
                        <div className="h-64 bg-gradient-to-br from-violet-600/40 to-purple-600/40 flex items-center justify-center">
                            <svg className="w-24 h-24 text-white/50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                            </svg>
                        </div>

                        <div className="p-8">
                            {/* Status badges */}
                            <div className="flex gap-2 mb-4">
                                {campaign.isLive && (
                                    <span className="px-3 py-1 bg-emerald-500/20 text-emerald-400 text-xs font-semibold rounded-full">
                                        LIVE
                                    </span>
                                )}
                                {campaign.isApproved && (
                                    <span className="px-3 py-1 bg-blue-500/20 text-blue-400 text-xs font-semibold rounded-full">
                                        VERIFIED
                                    </span>
                                )}
                            </div>

                            <h1 className="text-3xl font-bold text-white mb-4">{campaign.title}</h1>

                            {/* Progress Section */}
                            <div className="bg-white/5 rounded-2xl p-6 mb-6">
                                <div className="flex justify-between items-end mb-4">
                                    <div>
                                        <p className="text-slate-400 text-sm">Raised</p>
                                        <p className="text-3xl font-bold text-purple-400">{formatCurrency(campaign.fundRaised)}</p>
                                    </div>
                                    <div className="text-right">
                                        <p className="text-slate-400 text-sm">Donors</p>
                                        <p className="text-2xl font-bold text-white">{campaign.donorCount || 0}</p>
                                    </div>
                                </div>
                                <div className="h-3 bg-white/10 rounded-full overflow-hidden">
                                    <div
                                        className="h-full bg-gradient-to-r from-violet-500 to-purple-500 rounded-full transition-all duration-500"
                                        style={{ width: `${Math.min((campaign.fundRaised / 100000) * 100, 100)}%` }}
                                    />
                                </div>
                            </div>

                            {/* Description */}
                            <div className="mb-8">
                                <h2 className="text-xl font-semibold text-white mb-3">About this campaign</h2>
                                <p className="text-slate-300 leading-relaxed whitespace-pre-wrap">{campaign.description}</p>
                            </div>

                            {/* Donate Button */}
                            <button
                                onClick={() => setShowDonateModal(true)}
                                className="w-full py-4 bg-gradient-to-r from-violet-600 to-purple-600 text-white font-semibold text-lg rounded-xl hover:from-violet-500 hover:to-purple-500 transition-all shadow-lg shadow-purple-500/25"
                            >
                                Donate Now
                            </button>
                        </div>
                    </div>
                )}
            </main>

            {/* Donate Modal */}
            {showDonateModal && (
                <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center p-4 z-50">
                    <div className="bg-slate-900 rounded-3xl p-8 max-w-md w-full border border-white/10 shadow-2xl">
                        <div className="flex justify-between items-center mb-6">
                            <h2 className="text-2xl font-bold text-white">Make a Donation</h2>
                            <button
                                onClick={() => setShowDonateModal(false)}
                                className="text-slate-400 hover:text-white transition-colors"
                            >
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                                </svg>
                            </button>
                        </div>

                        <form onSubmit={handleDonate} className="space-y-5">
                            {error && (
                                <div className="bg-red-500/20 border border-red-500/50 text-red-300 px-4 py-3 rounded-xl text-sm">
                                    {error}
                                </div>
                            )}

                            <div>
                                <label className="block text-sm font-medium text-slate-300 mb-2">
                                    Amount (₹) <span className="text-red-400">*</span>
                                </label>
                                <input
                                    type="number"
                                    name="amount"
                                    value={donationData.amount}
                                    onChange={handleDonationChange}
                                    min="1"
                                    className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all text-lg"
                                    placeholder="Enter amount"
                                    required
                                />
                                {/* Quick amount buttons */}
                                <div className="flex gap-2 mt-3">
                                    {[100, 500, 1000, 5000].map((amt) => (
                                        <button
                                            key={amt}
                                            type="button"
                                            onClick={() => setDonationData({ ...donationData, amount: amt.toString() })}
                                            className="flex-1 py-2 bg-white/5 text-slate-300 rounded-lg hover:bg-white/10 transition-all text-sm font-medium"
                                        >
                                            ₹{amt}
                                        </button>
                                    ))}
                                </div>
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-slate-300 mb-2">
                                    UPI ID
                                </label>
                                <input
                                    type="text"
                                    name="upiId"
                                    value={donationData.upiId}
                                    onChange={handleDonationChange}
                                    className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all"
                                    placeholder="yourname@upi"
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-slate-300 mb-2">
                                    Message (optional)
                                </label>
                                <textarea
                                    name="donorMessage"
                                    value={donationData.donorMessage}
                                    onChange={handleDonationChange}
                                    rows={3}
                                    className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all resize-none"
                                    placeholder="Leave a message of support..."
                                />
                            </div>

                            <div className="flex items-center">
                                <input
                                    type="checkbox"
                                    name="isAnonymous"
                                    id="isAnonymous"
                                    checked={donationData.isAnonymous}
                                    onChange={handleDonationChange}
                                    className="w-5 h-5 bg-white/5 border border-white/10 rounded text-purple-500 focus:ring-purple-500 focus:ring-offset-slate-900"
                                />
                                <label htmlFor="isAnonymous" className="ml-3 text-slate-300">
                                    Make this donation anonymous
                                </label>
                            </div>

                            <button
                                type="submit"
                                disabled={donating}
                                className="w-full py-4 bg-gradient-to-r from-violet-600 to-purple-600 text-white font-semibold rounded-xl hover:from-violet-500 hover:to-purple-500 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 focus:ring-offset-slate-900 transition-all disabled:opacity-50 disabled:cursor-not-allowed shadow-lg shadow-purple-500/25"
                            >
                                {donating ? 'Processing...' : `Donate ${donationData.amount ? formatCurrency(donationData.amount) : ''}`}
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}
