import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { createCampaign } from '../services/api';
import { AUTH_CONFIG } from '../config';

export default function CreateCampaign() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        beneficiaryId: '',
        institutionId: '',
        title: '',
        description: '',
        medicalReportUrl: '',
        isLive: true,
        isApproved: false,
        isFulfilled: false,
        fundRaised: 0,
        donorCount: 0,
        priorityScore: 0
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const email = localStorage.getItem(AUTH_CONFIG.STORAGE_KEY);
        if (!email) {
            navigate('/');
        }
    }, [navigate]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData({
            ...formData,
            [name]: type === 'checkbox' ? checked : value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            const campaignData = {
                ...formData,
                beneficiaryId: parseInt(formData.beneficiaryId) || 1,
                institutionId: parseInt(formData.institutionId) || 1
            };

            await createCampaign(campaignData);
            alert('Campaign created successfully!');
            navigate('/campaigns');
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 flex items-center justify-center p-4">
            <div className="w-full max-w-2xl">
                {/* Header */}
                <div className="text-center mb-8">
                    <Link to="/campaigns" className="inline-flex items-center text-slate-400 hover:text-white transition-colors mb-4">
                        <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
                        </svg>
                        Back to Campaigns
                    </Link>
                    <h1 className="text-3xl font-bold text-white mb-2">Create Campaign</h1>
                    <p className="text-slate-400">Start a fundraising campaign for someone in need</p>
                </div>

                {/* Form Card */}
                <div className="bg-white/10 backdrop-blur-xl rounded-3xl p-8 shadow-2xl border border-white/10">
                    <form onSubmit={handleSubmit} className="space-y-6">
                        {error && (
                            <div className="bg-red-500/20 border border-red-500/50 text-red-300 px-4 py-3 rounded-xl text-sm">
                                {error}
                            </div>
                        )}

                        <div>
                            <label className="block text-sm font-medium text-slate-300 mb-2">
                                Campaign Title <span className="text-red-400">*</span>
                            </label>
                            <input
                                type="text"
                                name="title"
                                value={formData.title}
                                onChange={handleChange}
                                className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all"
                                placeholder="Enter campaign title"
                                required
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-slate-300 mb-2">
                                Description <span className="text-red-400">*</span>
                            </label>
                            <textarea
                                name="description"
                                value={formData.description}
                                onChange={handleChange}
                                rows={5}
                                className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all resize-none"
                                placeholder="Describe the campaign, beneficiary details, and how the funds will be used..."
                                required
                            />
                        </div>

                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-medium text-slate-300 mb-2">
                                    Beneficiary ID <span className="text-red-400">*</span>
                                </label>
                                <input
                                    type="number"
                                    name="beneficiaryId"
                                    value={formData.beneficiaryId}
                                    onChange={handleChange}
                                    className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all"
                                    placeholder="Beneficiary ID"
                                    required
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-slate-300 mb-2">
                                    Institution ID <span className="text-red-400">*</span>
                                </label>
                                <input
                                    type="number"
                                    name="institutionId"
                                    value={formData.institutionId}
                                    onChange={handleChange}
                                    className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all"
                                    placeholder="Institution ID"
                                    required
                                />
                            </div>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-slate-300 mb-2">
                                Medical Report URL
                            </label>
                            <input
                                type="url"
                                name="medicalReportUrl"
                                value={formData.medicalReportUrl}
                                onChange={handleChange}
                                className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all"
                                placeholder="https://example.com/report.pdf"
                            />
                        </div>

                        <div className="flex items-center">
                            <input
                                type="checkbox"
                                name="isLive"
                                id="isLive"
                                checked={formData.isLive}
                                onChange={handleChange}
                                className="w-5 h-5 bg-white/5 border border-white/10 rounded text-purple-500 focus:ring-purple-500 focus:ring-offset-slate-900"
                            />
                            <label htmlFor="isLive" className="ml-3 text-slate-300">
                                Make campaign live immediately
                            </label>
                        </div>

                        <button
                            type="submit"
                            disabled={loading}
                            className="w-full py-4 bg-gradient-to-r from-violet-600 to-purple-600 text-white font-semibold text-lg rounded-xl hover:from-violet-500 hover:to-purple-500 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 focus:ring-offset-slate-900 transition-all disabled:opacity-50 disabled:cursor-not-allowed shadow-lg shadow-purple-500/25"
                        >
                            {loading ? 'Creating Campaign...' : 'Create Campaign'}
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}
