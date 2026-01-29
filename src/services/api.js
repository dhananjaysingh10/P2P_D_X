import { API_BASE_URL } from '../config';

// Register a new user
export const registerUser = async (userData) => {
    const response = await fetch(`${API_BASE_URL}/api/v1/users/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || 'Failed to register user');
    }

    return data;
};

// Register a new institution
export const registerInstitution = async (institutionData) => {
    const response = await fetch(`${API_BASE_URL}/institutions?shardKey=test`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(institutionData),
    });

    if (!response.ok) {
        const data = await response.json().catch(() => ({}));
        throw new Error(data.message || 'Failed to register institution');
    }

    return { success: true };
};

// Get user by email
export const getUserByEmail = async (email) => {
    const response = await fetch(`${API_BASE_URL}/api/v1/users/email/${encodeURIComponent(email)}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || 'User not found');
    }

    return data;
};

// Check if email exists
export const checkEmailExists = async (email) => {
    const response = await fetch(`${API_BASE_URL}/api/v1/users/check/email/${encodeURIComponent(email)}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    const data = await response.json();
    return data.data;
};

// ============ CAMPAIGN APIs ============

// Get all live campaigns
export const getLiveCampaigns = async () => {
    const response = await fetch(`${API_BASE_URL}/campaigns/live?shardKey=test`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        throw new Error('Failed to fetch campaigns');
    }

    return await response.json();
};

// Get campaign by ID
export const getCampaignById = async (id) => {
    const response = await fetch(`${API_BASE_URL}/campaigns/${id}?shardKey=test`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        throw new Error('Campaign not found');
    }

    return await response.json();
};

// Create a new campaign
export const createCampaign = async (campaignData) => {
    const response = await fetch(`${API_BASE_URL}/campaigns?shardKey=test`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(campaignData),
    });

    if (!response.ok) {
        const data = await response.json().catch(() => ({}));
        throw new Error(data.message || 'Failed to create campaign');
    }

    return { success: true };
};

// ============ TRANSACTION APIs ============

// Create a donation transaction
export const createTransaction = async (transactionData) => {
    const response = await fetch(`${API_BASE_URL}/transactions?shardKey=test`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(transactionData),
    });

    if (!response.ok) {
        const data = await response.json().catch(() => ({}));
        throw new Error(data.message || 'Failed to process donation');
    }

    return { success: true };
};

// ============ INSTITUTION CAMPAIGN MANAGEMENT APIs ============

// Get all campaigns (for institution management)
export const getAllCampaigns = async () => {
    const response = await fetch(`${API_BASE_URL}/campaigns?shardKey=test`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        throw new Error('Failed to fetch campaigns');
    }

    return await response.json();
};

// Get approved campaigns
export const getApprovedCampaigns = async () => {
    const response = await fetch(`${API_BASE_URL}/campaigns/approved?shardKey=test`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        throw new Error('Failed to fetch approved campaigns');
    }

    return await response.json();
};

// Get campaigns by institution ID
export const getCampaignsByInstitution = async (institutionId) => {
    const response = await fetch(`${API_BASE_URL}/campaigns/institution/${institutionId}?shardKey=test`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        throw new Error('Failed to fetch institution campaigns');
    }

    return await response.json();
};

// Update campaign (for approve/close actions)
export const updateCampaign = async (id, campaignData) => {
    const response = await fetch(`${API_BASE_URL}/campaigns/${id}?shardKey=test`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(campaignData),
    });

    if (!response.ok) {
        const data = await response.json().catch(() => ({}));
        throw new Error(data.message || 'Failed to update campaign');
    }

    return { success: true };
};
