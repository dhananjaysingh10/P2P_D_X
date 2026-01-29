import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { getLiveCampaigns } from '../services/api';
import { AUTH_CONFIG } from '../config';

export default function Campaigns() {
    const navigate = useNavigate();
    const [campaigns, setCampaigns] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const email = localStorage.getItem(AUTH_CONFIG.STORAGE_KEY);
        if (!email) {
            navigate('/');
            return;
        }
        fetchCampaigns();
    }, [navigate]);

    const fetchCampaigns = async () => {
        try {
            setLoading(true);
            const data = await getLiveCampaigns();
            setCampaigns(data);
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

    return (
        <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900">
            {/* Header */}
            <header className="bg-white/5 backdrop-blur-xl border-b border-white/10">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex justify-between items-center h-16">
                        <div className="flex items-center">
                            <Link to="/dashboard" className="flex items-center">
                                <div className="w-10 h-10 rounded-xl bg-gradient-to-r from-violet-500 to-purple-500 flex items-center justify-center">
                                    <svg className="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                                    </svg>
                                </div>
                                <span className="ml-3 text-xl font-bold text-white">P2P Donations</span>
                            </Link>
                        </div>

                        <Link
                            to="/dashboard"
                            className="px-4 py-2 bg-white/10 text-white rounded-lg hover:bg-white/20 transition-all text-sm font-medium"
                        >
                            Back to Dashboard
                        </Link>
                    </div>
                </div>
            </header>

            {/* Main Content */}
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
                <div className="flex justify-between items-center mb-8">
                    <div>
                        <h1 className="text-3xl font-bold text-white mb-2">Active Campaigns</h1>
                        <p className="text-slate-400">Support causes that matter to you</p>
                    </div>
                    <Link
                        to="/campaigns/create"
                        className="px-6 py-3 bg-gradient-to-r from-violet-600 to-purple-600 text-white font-semibold rounded-xl hover:from-violet-500 hover:to-purple-500 transition-all shadow-lg shadow-purple-500/25"
                    >
                        Create Campaign
                    </Link>
                </div>

                {loading ? (
                    <div className="flex items-center justify-center py-20">
                        <div className="w-12 h-12 border-4 border-purple-500 border-t-transparent rounded-full animate-spin"></div>
                    </div>
                ) : error ? (
                    <div className="bg-red-500/20 border border-red-500/50 text-red-300 px-6 py-4 rounded-xl">
                        {error}
                    </div>
                ) : campaigns.length === 0 ? (
                    <div className="text-center py-20">
                        <div className="w-20 h-20 mx-auto mb-6 rounded-full bg-white/10 flex items-center justify-center">
                            <svg className="w-10 h-10 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                            </svg>
                        </div>
                        <h3 className="text-xl font-semibold text-white mb-2">No Active Campaigns</h3>
                        <p className="text-slate-400">Be the first to create a campaign!</p>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {campaigns.map((campaign) => (
                            <Link
                                key={campaign.id}
                                to={`/campaigns/${campaign.id}`}
                                className="bg-white/10 backdrop-blur-xl rounded-2xl overflow-hidden border border-white/10 hover:bg-white/15 hover:border-purple-500/50 transition-all group"
                            >
                                {/* Campaign Image Placeholder */}
                                <div className="h-48 bg-gradient-to-br from-violet-600/30 to-purple-600/30 flex items-center justify-center">
                                    <svg className="w-16 h-16 text-white/50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                                    </svg>
                                </div>

                                <div className="p-6">
                                    <h3 className="text-lg font-semibold text-white mb-2 group-hover:text-purple-300 transition-colors line-clamp-2">
                                        {campaign.title}
                                    </h3>
                                    <p className="text-slate-400 text-sm mb-4 line-clamp-2">
                                        {campaign.description}
                                    </p>

                                    {/* Progress */}
                                    <div className="mb-4">
                                        <div className="flex justify-between text-sm mb-2">
                                            <span className="text-purple-400 font-semibold">{formatCurrency(campaign.fundRaised)}</span>
                                            <span className="text-slate-500">{campaign.donorCount || 0} donors</span>
                                        </div>
                                        <div className="h-2 bg-white/10 rounded-full overflow-hidden">
                                            <div
                                                className="h-full bg-gradient-to-r from-violet-500 to-purple-500 rounded-full"
                                                style={{ width: `${Math.min((campaign.fundRaised / 100000) * 100, 100)}%` }}
                                            />
                                        </div>
                                    </div>

                                    <div className="flex items-center text-sm text-purple-400 font-medium">
                                        Donate Now
                                        <svg className="w-4 h-4 ml-2 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                                        </svg>
                                    </div>
                                </div>
                            </Link>
                        ))}
                    </div>
                )}
            </main>
        </div>
    );
}
