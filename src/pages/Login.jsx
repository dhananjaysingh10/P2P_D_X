import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AUTH_CONFIG, USER_TYPES } from '../config';
import { getUserByEmail, getAllInstitutions } from '../services/api';

export default function Login() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        userType: '',
        userId: ''
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            if (!formData.email) {
                throw new Error('Email is required');
            }
            if (!formData.userType) {
                throw new Error('Please select account type');
            }

            let userId = null;

            // Fetch ID based on user type
            if (formData.userType === USER_TYPES.USER) {
                // Fetch user by email
                const user = await getUserByEmail(formData.email);
                userId = user.data.id;
            } else {
                // Fetch all institutions and find matching email
                // Note: Ideally backend should have a getInstitutionByEmail endpoint
                const institutions = await getAllInstitutions();
                const institution = institutions.find(inst => inst.email === formData.email);

                if (!institution) {
                    throw new Error('Institution not found with this email');
                }
                userId = institution.id;
            }

            // Store in localStorage
            localStorage.setItem(AUTH_CONFIG.STORAGE_KEY, formData.email);
            localStorage.setItem(AUTH_CONFIG.USER_TYPE_KEY, formData.userType);
            localStorage.setItem(AUTH_CONFIG.USER_ID_KEY, userId);

            // Navigate to dashboard
            navigate('/dashboard');
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 flex items-center justify-center p-4">
            <div className="w-full max-w-md">
                {/* Logo/Header */}
                <div className="text-center mb-8">
                    <div className="inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-r from-violet-500 to-purple-500 mb-4 shadow-xl shadow-purple-500/25">
                        <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                    </div>
                    <h1 className="text-3xl font-bold text-white mb-2">P2P Donations</h1>
                    <p className="text-slate-400">Sign in to your account</p>
                </div>

                {/* Login Card */}
                <div className="bg-white/10 backdrop-blur-xl rounded-3xl p-8 shadow-2xl border border-white/10">
                    <form onSubmit={handleSubmit} className="space-y-5">
                        {error && (
                            <div className="bg-red-500/20 border border-red-500/50 text-red-300 px-4 py-3 rounded-xl text-sm">
                                {error}
                            </div>
                        )}

                        {/* Account Type Selection */}
                        <div>
                            <label className="block text-sm font-medium text-slate-300 mb-3">
                                Account Type <span className="text-red-400">*</span>
                            </label>
                            <div className="grid grid-cols-2 gap-3">
                                <button
                                    type="button"
                                    onClick={() => setFormData({ ...formData, userType: USER_TYPES.USER })}
                                    className={`p-4 rounded-xl border-2 transition-all flex flex-col items-center gap-2 ${formData.userType === USER_TYPES.USER
                                        ? 'border-purple-500 bg-purple-500/20 text-white'
                                        : 'border-white/10 bg-white/5 text-slate-400 hover:bg-white/10'
                                        }`}
                                >
                                    <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                    </svg>
                                    <span className="font-medium">User</span>
                                </button>
                                <button
                                    type="button"
                                    onClick={() => setFormData({ ...formData, userType: USER_TYPES.INSTITUTION })}
                                    className={`p-4 rounded-xl border-2 transition-all flex flex-col items-center gap-2 ${formData.userType === USER_TYPES.INSTITUTION
                                        ? 'border-purple-500 bg-purple-500/20 text-white'
                                        : 'border-white/10 bg-white/5 text-slate-400 hover:bg-white/10'
                                        }`}
                                >
                                    <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                                    </svg>
                                    <span className="font-medium">Institution</span>
                                </button>
                            </div>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-slate-300 mb-2">
                                Email Address <span className="text-red-400">*</span>
                            </label>
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all"
                                placeholder="Enter your email"
                                required
                            />
                        </div>



                        <div>
                            <label className="block text-sm font-medium text-slate-300 mb-2">
                                Password
                            </label>
                            <input
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all"
                                placeholder="Enter any password"
                            />
                            <p className="text-xs text-slate-500 mt-1">* Any password works for now</p>
                        </div>

                        <button
                            type="submit"
                            disabled={loading}
                            className="w-full py-3 px-4 bg-gradient-to-r from-violet-600 to-purple-600 text-white font-semibold rounded-xl hover:from-violet-500 hover:to-purple-500 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 focus:ring-offset-slate-900 transition-all disabled:opacity-50 disabled:cursor-not-allowed shadow-lg shadow-purple-500/25"
                        >
                            {loading ? 'Signing in...' : 'Sign In'}
                        </button>
                    </form>

                    {/* Registration Links */}
                    <div className="mt-8 pt-6 border-t border-white/10">
                        <p className="text-center text-slate-400 mb-4">Don't have an account?</p>
                        <div className="space-y-3">
                            <Link
                                to="/register/user"
                                className="block w-full py-3 px-4 bg-white/5 border border-white/10 text-white font-medium rounded-xl hover:bg-white/10 transition-all text-center"
                            >
                                Register as User
                            </Link>
                            <Link
                                to="/register/institution"
                                className="block w-full py-3 px-4 bg-white/5 border border-white/10 text-white font-medium rounded-xl hover:bg-white/10 transition-all text-center"
                            >
                                Register as Institution
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
