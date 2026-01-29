import { useEffect, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AUTH_CONFIG, USER_TYPES } from '../config';

export default function Dashboard() {
    const navigate = useNavigate();
    const [userEmail, setUserEmail] = useState('');
    const [userType, setUserType] = useState('');
    const [userId, setUserId] = useState('');

    useEffect(() => {
        const email = localStorage.getItem(AUTH_CONFIG.STORAGE_KEY);
        const type = localStorage.getItem(AUTH_CONFIG.USER_TYPE_KEY);
        const id = localStorage.getItem(AUTH_CONFIG.USER_ID_KEY);

        if (!email) {
            navigate('/');
            return;
        }
        setUserEmail(email);
        setUserType(type || '');
        setUserId(id || '');
    }, [navigate]);

    const handleLogout = () => {
        localStorage.removeItem(AUTH_CONFIG.STORAGE_KEY);
        localStorage.removeItem(AUTH_CONFIG.USER_TYPE_KEY);
        localStorage.removeItem(AUTH_CONFIG.USER_ID_KEY);
        navigate('/');
    };

    const isUser = userType === USER_TYPES.USER;
    const isInstitution = userType === USER_TYPES.INSTITUTION;

    return (
        <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900">
            {/* Header */}
            <header className="bg-white/5 backdrop-blur-xl border-b border-white/10">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex justify-between items-center h-16">
                        <div className="flex items-center">
                            <div className="w-10 h-10 rounded-xl bg-gradient-to-r from-violet-500 to-purple-500 flex items-center justify-center">
                                <svg className="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                            </div>
                            <span className="ml-3 text-xl font-bold text-white">P2P Donations</span>
                        </div>

                        <div className="flex items-center space-x-4">
                            <div className="text-right">
                                <span className="text-slate-300 text-sm block">{userEmail}</span>
                                <span className={`text-xs font-medium ${isInstitution ? 'text-amber-400' : 'text-emerald-400'}`}>
                                    {isInstitution ? '🏢 Institution' : '👤 User'} (ID: {userId})
                                </span>
                            </div>
                            <button
                                onClick={handleLogout}
                                className="px-4 py-2 bg-white/10 text-white rounded-lg hover:bg-white/20 transition-all text-sm font-medium"
                            >
                                Logout
                            </button>
                        </div>
                    </div>
                </div>
            </header>

            {/* Main Content */}
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
                {/* Welcome Card */}
                <div className="bg-white/10 backdrop-blur-xl rounded-3xl p-8 shadow-2xl border border-white/10 mb-8">
                    <h1 className="text-3xl font-bold text-white mb-2">Welcome back! 👋</h1>
                    <p className="text-slate-400">
                        You are logged in as <span className="text-purple-400 font-medium">{userEmail}</span>
                        {' '}({isInstitution ? 'Institution' : 'User'})
                    </p>
                </div>

                {/* Quick Actions */}
                <h2 className="text-xl font-semibold text-white mb-4">Quick Actions</h2>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    {/* Create Campaign - Only for Users */}
                    {isUser && (
                        <Link to="/campaigns/create" className="bg-white/10 backdrop-blur-xl rounded-2xl p-6 border border-white/10 hover:bg-white/15 transition-all cursor-pointer group">
                            <div className="w-12 h-12 rounded-xl bg-gradient-to-r from-emerald-500 to-teal-500 flex items-center justify-center mb-4 group-hover:scale-110 transition-transform">
                                <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                                </svg>
                            </div>
                            <h3 className="text-lg font-semibold text-white mb-2">Create Campaign</h3>
                            <p className="text-slate-400 text-sm">Start a new donation campaign</p>
                        </Link>
                    )}

                    {/* View Campaigns - For Both */}
                    <Link to="/campaigns" className="bg-white/10 backdrop-blur-xl rounded-2xl p-6 border border-white/10 hover:bg-white/15 transition-all cursor-pointer group">
                        <div className="w-12 h-12 rounded-xl bg-gradient-to-r from-blue-500 to-cyan-500 flex items-center justify-center mb-4 group-hover:scale-110 transition-transform">
                            <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 10h16M4 14h16M4 18h16" />
                            </svg>
                        </div>
                        <h3 className="text-lg font-semibold text-white mb-2">View Campaigns</h3>
                        <p className="text-slate-400 text-sm">Browse active campaigns</p>
                    </Link>

                    {/* Manage Campaigns - Only for Institutions */}
                    {isInstitution && (
                        <Link to="/institution/manage" className="bg-white/10 backdrop-blur-xl rounded-2xl p-6 border border-white/10 hover:bg-white/15 transition-all cursor-pointer group">
                            <div className="w-12 h-12 rounded-xl bg-gradient-to-r from-amber-500 to-orange-500 flex items-center justify-center mb-4 group-hover:scale-110 transition-transform">
                                <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                                </svg>
                            </div>
                            <h3 className="text-lg font-semibold text-white mb-2">Manage Campaigns</h3>
                            <p className="text-slate-400 text-sm">Approve/reject campaigns</p>
                        </Link>
                    )}

                    {/* My Donations - Only for Users */}
                    {isUser && (
                        <div className="bg-white/10 backdrop-blur-xl rounded-2xl p-6 border border-white/10 hover:bg-white/15 transition-all cursor-pointer group">
                            <div className="w-12 h-12 rounded-xl bg-gradient-to-r from-pink-500 to-rose-500 flex items-center justify-center mb-4 group-hover:scale-110 transition-transform">
                                <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                                </svg>
                            </div>
                            <h3 className="text-lg font-semibold text-white mb-2">My Donations</h3>
                            <p className="text-slate-400 text-sm">Track your contributions</p>
                        </div>
                    )}
                </div>

                {/* Stats Preview */}
                <div className="mt-8 grid grid-cols-1 md:grid-cols-4 gap-4">
                    <div className="bg-white/5 rounded-2xl p-6 border border-white/10">
                        <p className="text-slate-400 text-sm mb-1">{isInstitution ? 'Campaigns Managed' : 'Total Donated'}</p>
                        <p className="text-2xl font-bold text-white">₹0</p>
                    </div>
                    <div className="bg-white/5 rounded-2xl p-6 border border-white/10">
                        <p className="text-slate-400 text-sm mb-1">Active Campaigns</p>
                        <p className="text-2xl font-bold text-white">0</p>
                    </div>
                    <div className="bg-white/5 rounded-2xl p-6 border border-white/10">
                        <p className="text-slate-400 text-sm mb-1">{isInstitution ? 'Pending Approvals' : 'Beneficiaries Helped'}</p>
                        <p className="text-2xl font-bold text-white">0</p>
                    </div>
                    <div className="bg-white/5 rounded-2xl p-6 border border-white/10">
                        <p className="text-slate-400 text-sm mb-1">{isInstitution ? 'Total Raised' : 'Tax Certificates'}</p>
                        <p className="text-2xl font-bold text-white">0</p>
                    </div>
                </div>
            </main>
        </div>
    );
}
