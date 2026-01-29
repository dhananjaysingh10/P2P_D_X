package com.example.service;

import com.example.dao.CampaignStore;
import com.example.entity.Campaign;
import com.example.service.CampaignService;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CampaignServiceImpl implements CampaignService {

    private final CampaignStore campaignStore;

    @Override
    public List<Campaign> getAllCampaigns(String shardKey) {
        return campaignStore.getAll(shardKey);
    }

    @Override
    public Optional<Campaign> getCampaignById(String shardKey, Long id) {
        return campaignStore.getById(shardKey, id);
    }

    @Override
    public void createCampaign(String shardKey, Campaign campaign) {
        campaignStore.create(shardKey, campaign);
    }

    @Override
    public void updateCampaign(String shardKey, Long id, Campaign campaign) {
        if (!campaignStore.exists(shardKey, id)) {
            throw new RuntimeException("Campaign not found: " + id);
        }
        campaignStore.update(shardKey, id, campaign);
    }

    @Override
    public boolean campaignExists(String shardKey, Long id) {
        return campaignStore.exists(shardKey, id);
    }

    @Override
    public List<Campaign> getCampaignsByBeneficiaryId(String shardKey, Long beneficiaryId) {
        return campaignStore.getByBeneficiaryId(shardKey, beneficiaryId);
    }

    @Override
    public List<Campaign> getCampaignsByInstitutionId(String shardKey, Long institutionId) {
        return campaignStore.getByInstitutionId(shardKey, institutionId);
    }

    @Override
    public List<Campaign> getLiveCampaigns(String shardKey) {
        return campaignStore.getLiveCampaigns(shardKey);
    }

    @Override
    public List<Campaign> getApprovedCampaigns(String shardKey) {
        return campaignStore.getApprovedCampaigns(shardKey);
    }

    @Override
    public List<Campaign> getFulfilledCampaigns(String shardKey) {
        return campaignStore.getFulfilledCampaigns(shardKey);
    }
}
