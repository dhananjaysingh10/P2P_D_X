package com.example.service;

import com.example.entity.Campaign;

import java.util.List;
import java.util.Optional;

public interface CampaignService {

    List<Campaign> getAllCampaigns(String shardKey);

    Optional<Campaign> getCampaignById(String shardKey, Long id);

    void createCampaign(String shardKey, Campaign campaign);

    void updateCampaign(String shardKey, Long id, Campaign campaign);

    boolean campaignExists(String shardKey, Long id);

    List<Campaign> getCampaignsByBeneficiaryId(String shardKey, Long beneficiaryId);

    List<Campaign> getCampaignsByInstitutionId(String shardKey, Long institutionId);

    List<Campaign> getLiveCampaigns(String shardKey);

    List<Campaign> getApprovedCampaigns(String shardKey);

    List<Campaign> getFulfilledCampaigns(String shardKey);
}
