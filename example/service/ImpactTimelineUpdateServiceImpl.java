package com.example.service;

import com.example.dao.ImpactTimelineUpdateStore;
import com.example.entity.ImpactTimelineUpdate;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ImpactTimelineUpdateServiceImpl implements ImpactTimelineUpdateService {
    private final ImpactTimelineUpdateStore store;

    @Override
    public void addUpdate(String shardKey, ImpactTimelineUpdate update) {
        ImpactTimelineUpdate toSave = ImpactTimelineUpdate.builder()
                .campaignId(update.getCampaignId())
                .institutionId(update.getInstitutionId())
                .description(update.getDescription())
                .proofFilePath(update.getProofFilePath())
                .createdAt(new java.sql.Timestamp(System.currentTimeMillis()))
                .build();
        store.create(shardKey, toSave);
    }

    @Override
    public List<ImpactTimelineUpdate> getUpdatesForCampaign(String shardKey, Long campaignId) {
        return store.getByCampaignId(shardKey, campaignId);
    }
}
