package com.example.dao;

import com.example.constants.ShardKey;
import com.example.entity.Campaign;
import io.appform.dropwizard.sharding.dao.RelationalDao;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CampaignStore {

    private static final int MAX_FETCH_COUNT = 100;
    private final RelationalDao<Campaign> campaignRelationalDao;

    public List<Campaign> getAll(String shardKey) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Campaign.class);
            return campaignRelationalDao.select(ShardKey.SHARD_KEY, criteria, 0, MAX_FETCH_COUNT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch campaigns", e);
        }
    }

    public Optional<Campaign> getById(String shardKey, Long id) {
        try {
            return campaignRelationalDao.get(ShardKey.SHARD_KEY, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch campaign: " + id, e);
        }
    }

    public void create(String shardKey, Campaign campaign) {
        try {
            campaignRelationalDao.save(ShardKey.SHARD_KEY, campaign);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create campaign", e);
        }
    }

    public void update(String shardKey, Long id, Campaign updatedCampaign) {
        try {
            campaignRelationalDao.update(ShardKey.SHARD_KEY,
                    DetachedCriteria.forClass(Campaign.class)
                            .add(Restrictions.eq("id", id)),
                    campaign -> {
                        campaign.setBeneficiaryId(updatedCampaign.getBeneficiaryId());
                        campaign.setInstitutionId(updatedCampaign.getInstitutionId());
                        campaign.setTitle(updatedCampaign.getTitle());
                        campaign.setDescription(updatedCampaign.getDescription());
                        campaign.setFundRaised(updatedCampaign.getFundRaised());
                        campaign.setReportFileId(updatedCampaign.getReportFileId());
                        campaign.setIsLive(updatedCampaign.getIsLive());
                        campaign.setIsApproved(updatedCampaign.getIsApproved());
                        campaign.setIsFulfilled(updatedCampaign.getIsFulfilled());
                        campaign.setVerifiedBy(updatedCampaign.getVerifiedBy());
                        campaign.setDonorCount(updatedCampaign.getDonorCount());
                        campaign.setPriorityScore(updatedCampaign.getPriorityScore());
                        return campaign;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Failed to update campaign: " + id, e);
        }
    }

    public void updateReportFileId(String shardKey, Long id, String reportFileId) {
        try {
            campaignRelationalDao.update(ShardKey.SHARD_KEY,
                    DetachedCriteria.forClass(Campaign.class)
                            .add(Restrictions.eq("id", id)),
                    campaign -> {
                        campaign.setReportFileId(reportFileId);
                        return campaign;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Failed to update report file ID for campaign: " + id, e);
        }
    }

    public boolean exists(String shardKey, Long id) {
        return getById(shardKey, id).isPresent();
    }

    public List<Campaign> getByBeneficiaryId(String shardKey, Long beneficiaryId) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Campaign.class)
                    .add(Restrictions.eq("beneficiaryId", beneficiaryId));
            return campaignRelationalDao.select(ShardKey.SHARD_KEY, criteria, 0, MAX_FETCH_COUNT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch campaigns for beneficiary: " + beneficiaryId, e);
        }
    }

    public List<Campaign> getByInstitutionId(String shardKey, Long institutionId) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Campaign.class)
                    .add(Restrictions.eq("institutionId", institutionId));
            return campaignRelationalDao.select(ShardKey.SHARD_KEY, criteria, 0, MAX_FETCH_COUNT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch campaigns for institution: " + institutionId, e);
        }
    }

    public List<Campaign> getLiveCampaigns(String shardKey) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Campaign.class)
                    .add(Restrictions.eq("isLive", true));
            return campaignRelationalDao.select(ShardKey.SHARD_KEY, criteria, 0, MAX_FETCH_COUNT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch live campaigns", e);
        }
    }

    public List<Campaign> getApprovedCampaigns(String shardKey) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Campaign.class)
                    .add(Restrictions.eq("isApproved", true));
            return campaignRelationalDao.select(ShardKey.SHARD_KEY, criteria, 0, MAX_FETCH_COUNT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch approved campaigns", e);
        }
    }

    public List<Campaign> getFulfilledCampaigns(String shardKey) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Campaign.class)
                    .add(Restrictions.eq("isFulfilled", true));
            return campaignRelationalDao.select(ShardKey.SHARD_KEY, criteria, 0, MAX_FETCH_COUNT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch fulfilled campaigns", e);
        }
    }
}
