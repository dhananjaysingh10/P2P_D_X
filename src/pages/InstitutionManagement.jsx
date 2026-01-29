import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { getAllCampaigns, updateCampaign } from '../services/api';
import { AUTH_CONFIG } from '../config';

export default function InstitutionManagement() {
    const navigate = useNavigate();
    const [campaigns, setCampaigns] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [activeTab, setActiveTab] = useState('pending');
    const [actionLoading, setActionLoading] = useState(null);

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
            const data = await getAllCampaigns();
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

    const handleApprove = async (campaign) => {
        setActionLoading(campaign.id);
        try {
            await updateCampaign(campaign.id, {
                ...campaign,
                isApproved: true,
                isLive: true
            });
            await fetchCampaigns();
        } catch (err) {
            setError(err.message);
        } finally {
            setActionLoading(null);
        }
    };

    const handleClose = async (campaign) => {
        setActionLoading(campaign.id);
        try {
            await updateCampaign(campaign.id, {
                ...campaign,
                isLive: false,
                isFulfilled: true
            });
            await fetchCampaigns();
        } catch (err) {
            setError(err.message);
        } finally {
            setActionLoading(null);
        }
    };

    const handleReject = async (campaign) => {
        setActionLoading(campaign.id);
        try {
            await updateCampaign(campaign.id, {
                ...campaign,
                isApproved: false,
                isLive: false
            });
            await fetchCampaigns();
        } catch (err) {
            setError(err.message);
        } finally {
            setActionLoading(null);
        }
    };

    // Filter campaigns based on active tab
    const pendingCampaigns = campaigns.filter(c => !c.isApproved && c.isLive);
    const approvedCampaigns = campaigns.filter(c => c.isApproved && c.isLive);
    const closedCampaigns = campaigns.filter(c => c.isFulfilled || (!c.isLive && c.isApproved));

    const getDisplayedCampaigns = () => {
        switch (activeTab) {
            case 'pending': return pendingCampaigns;
            case 'approved': return approvedCampaigns;
            case 'closed': return closedCampaigns;
            default: return campaigns;
        }
    };

    const totalFundsRaised = campaigns.reduce((sum, c) => sum + (c.fundRaised || 0), 0);
    const totalDonors = campaigns.reduce((sum, c) => sum + (c.donorCount || 0), 0);

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
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                                    </svg>
                                </div>
                                <span className="ml-3 text-xl font-bold text-white">Institution Management</span>
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
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* Stats Cards */}
                <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
                    <div className="bg-white/10 backdrop-blur-xl rounded-2xl p-6 border border-white/10">
                        <p className="text-slate-400 text-sm mb-1">Total Campaigns</p>
                        <p className="text-3xl font-bold text-white">{campaigns.length}</p>
                    </div>
                    <div className="bg-amber-500/20 backdrop-blur-xl rounded-2xl p-6 border border-amber-500/30">
                        <p className="text-amber-300 text-sm mb-1">Pending Approval</p>
                        <p className="text-3xl font-bold text-amber-400">{pendingCampaigns.length}</p>
                    </div>
                    <div className="bg-emerald-500/20 backdrop-blur-xl rounded-2xl p-6 border border-emerald-500/30">
                        <p className="text-emerald-300 text-sm mb-1">Total Funds Raised</p>
                        <p className="text-3xl font-bold text-emerald-400">{formatCurrency(totalFundsRaised)}</p>
                    </div>
                    <div className="bg-blue-500/20 backdrop-blur-xl rounded-2xl p-6 border border-blue-500/30">
                        <p className="text-blue-300 text-sm mb-1">Total Donors</p>
                        <p className="text-3xl font-bold text-blue-400">{totalDonors}</p>
                    </div>
                </div>

                {/* Tabs */}
                <div className="flex space-x-2 mb-6">
                    <button
                        onClick={() => setActiveTab('pending')}
                        className={`px-6 py-3 rounded-xl font-medium transition-all ${activeTab === 'pending'
                                ? 'bg-amber-500 text-white'
                                : 'bg-white/10 text-slate-300 hover:bg-white/20'
                            }`}
                    >
                        Pending ({pendingCampaigns.length})
                    </button>
                    <button
                        onClick={() => setActiveTab('approved')}
                        className={`px-6 py-3 rounded-xl font-medium transition-all ${activeTab === 'approved'
                                ? 'bg-emerald-500 text-white'
                                : 'bg-white/10 text-slate-300 hover:bg-white/20'
                            }`}
                    >
                        Approved ({approvedCampaigns.length})
                    </button>
                    <button
                        onClick={() => setActiveTab('closed')}
                        className={`px-6 py-3 rounded-xl font-medium transition-all ${activeTab === 'closed'
                                ? 'bg-slate-500 text-white'
                                : 'bg-white/10 text-slate-300 hover:bg-white/20'
                            }`}
                    >
                        Closed ({closedCampaigns.length})
                    </button>
                </div>

                {error && (
                    <div className="bg-red-500/20 border border-red-500/50 text-red-300 px-6 py-4 rounded-xl mb-6">
                        {error}
                    </div>
                )}

                {/* Campaigns Table */}
                {loading ? (
                    <div className="flex items-center justify-center py-20">
                        <div className="w-12 h-12 border-4 border-purple-500 border-t-transparent rounded-full animate-spin"></div>
                    </div>
                ) : getDisplayedCampaigns().length === 0 ? (
                    <div className="text-center py-20 bg-white/5 rounded-2xl border border-white/10">
                        <div className="w-16 h-16 mx-auto mb-4 rounded-full bg-white/10 flex items-center justify-center">
                            <svg className="w-8 h-8 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                            </svg>
                        </div>
                        <h3 className="text-xl font-semibold text-white mb-2">No {activeTab} campaigns</h3>
                        <p className="text-slate-400">There are no campaigns in this category</p>
                    </div>
                ) : (
                    <div className="bg-white/5 backdrop-blur-xl rounded-2xl border border-white/10 overflow-hidden">
                        <div className="overflow-x-auto">
                            <table className="w-full">
                                <thead>
                                    <tr className="border-b border-white/10">
                                        <th className="text-left px-6 py-4 text-sm font-semibold text-slate-300">Campaign</th>
                                        <th className="text-left px-6 py-4 text-sm font-semibold text-slate-300">Status</th>
                                        <th className="text-right px-6 py-4 text-sm font-semibold text-slate-300">Raised</th>
                                        <th className="text-right px-6 py-4 text-sm font-semibold text-slate-300">Donors</th>
                                        <th className="text-right px-6 py-4 text-sm font-semibold text-slate-300">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {getDisplayedCampaigns().map((campaign) => (
                                        <tr key={campaign.id} className="border-b border-white/5 hover:bg-white/5 transition-colors">
                                            <td className="px-6 py-4">
                                                <div>
                                                    <p className="text-white font-medium">{campaign.title}</p>
                                                    <p className="text-slate-400 text-sm line-clamp-1">{campaign.description}</p>
                                                </div>
                                            </td>
                                            <td className="px-6 py-4">
                                                <div className="flex gap-2">
                                                    {campaign.isLive && (
                                                        <span className="px-2 py-1 bg-emerald-500/20 text-emerald-400 text-xs font-semibold rounded-full">
                                                            LIVE
                                                        </span>
                                                    )}
                                                    {campaign.isApproved && (
                                                        <span className="px-2 py-1 bg-blue-500/20 text-blue-400 text-xs font-semibold rounded-full">
                                                            APPROVED
                                                        </span>
                                                    )}
                                                    {campaign.isFulfilled && (
                                                        <span className="px-2 py-1 bg-slate-500/20 text-slate-400 text-xs font-semibold rounded-full">
                                                            CLOSED
                                                        </span>
                                                    )}
                                                    {!campaign.isApproved && !campaign.isFulfilled && (
                                                        <span className="px-2 py-1 bg-amber-500/20 text-amber-400 text-xs font-semibold rounded-full">
                                                            PENDING
                                                        </span>
                                                    )}
                                                </div>
                                            </td>
                                            <td className="px-6 py-4 text-right">
                                                <span className="text-purple-400 font-semibold">{formatCurrency(campaign.fundRaised)}</span>
                                            </td>
                                            <td className="px-6 py-4 text-right">
                                                <span className="text-white">{campaign.donorCount || 0}</span>
                                            </td>
                                            <td className="px-6 py-4">
                                                <div className="flex justify-end gap-2">
                                                    <Link
                                                        to={`/campaigns/${campaign.id}`}
                                                        className="px-3 py-1.5 bg-white/10 text-white text-sm rounded-lg hover:bg-white/20 transition-all"
                                                    >
                                                        View
                                                    </Link>

                                                    {activeTab === 'pending' && (
                                                        <>
                                                            <button
                                                                onClick={() => handleApprove(campaign)}
                                                                disabled={actionLoading === campaign.id}
                                                                className="px-3 py-1.5 bg-emerald-500 text-white text-sm rounded-lg hover:bg-emerald-600 transition-all disabled:opacity-50"
                                                            >
                                                                {actionLoading === campaign.id ? '...' : 'Approve'}
                                                            </button>
                                                            <button
                                                                onClick={() => handleReject(campaign)}
                                                                disabled={actionLoading === campaign.id}
                                                                className="px-3 py-1.5 bg-red-500 text-white text-sm rounded-lg hover:bg-red-600 transition-all disabled:opacity-50"
                                                            >
                                                                {actionLoading === campaign.id ? '...' : 'Reject'}
                                                            </button>
                                                        </>
                                                    )}

                                                    {activeTab === 'approved' && (
                                                        <button
                                                            onClick={() => handleClose(campaign)}
                                                            disabled={actionLoading === campaign.id}
                                                            className="px-3 py-1.5 bg-slate-500 text-white text-sm rounded-lg hover:bg-slate-600 transition-all disabled:opacity-50"
                                                        >
                                                            {actionLoading === campaign.id ? '...' : 'Close'}
                                                        </button>
                                                    )}
                                                </div>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}
            </main>
        </div>
    );
}
