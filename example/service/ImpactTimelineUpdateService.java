package com.example.service;

import com.example.entity.ImpactTimelineUpdate;
import java.util.List;

public interface ImpactTimelineUpdateService {
    List<ImpactTimelineUpdate> getUpdatesForCampaign(String shardKey, Long campaignId);
    void addUpdate(String shardKey, ImpactTimelineUpdate update);
}
