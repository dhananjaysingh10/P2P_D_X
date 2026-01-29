package com.example.dao;

import com.example.entity.ImpactTimelineUpdate;
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
public class ImpactTimelineUpdateStore {
    private static final int MAX_FETCH_COUNT = 100;
    private final RelationalDao<ImpactTimelineUpdate> impactTimelineUpdateRelationalDao;

    public List<ImpactTimelineUpdate> getAll(String shardKey) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(ImpactTimelineUpdate.class);
            return impactTimelineUpdateRelationalDao.select(shardKey, criteria, 0, MAX_FETCH_COUNT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch impact timeline updates", e);
        }
    }

    public Optional<ImpactTimelineUpdate> getById(String shardKey, Long id) {
        try {
            return impactTimelineUpdateRelationalDao.get(shardKey, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch impact timeline update: " + id, e);
        }
    }

    public void create(String shardKey, ImpactTimelineUpdate update) {
        try {
            impactTimelineUpdateRelationalDao.save(shardKey, update);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create impact timeline update", e);
        }
    }

    public void update(String shardKey, Long id, ImpactTimelineUpdate updatedUpdate) {
        try {
            impactTimelineUpdateRelationalDao.update(shardKey,
                    DetachedCriteria.forClass(ImpactTimelineUpdate.class)
                            .add(Restrictions.eq("id", id)),
                    update -> {
                        update.setDescription(updatedUpdate.getDescription());
                        update.setProofFilePath(updatedUpdate.getProofFilePath());
                        update.setInstitutionId(updatedUpdate.getInstitutionId());
                        // Do not update campaignId or createdAt
                        return update;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Failed to update impact timeline update: " + id, e);
        }
    }

    public boolean exists(String shardKey, Long id) {
        return getById(shardKey, id).isPresent();
    }

    public List<ImpactTimelineUpdate> getByCampaignId(String shardKey, Long campaignId) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(ImpactTimelineUpdate.class)
                    .add(Restrictions.eq("campaignId", campaignId));
            return impactTimelineUpdateRelationalDao.select(shardKey, criteria, 0, MAX_FETCH_COUNT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch impact timeline updates for campaign: " + campaignId, e);
        }
    }
}
