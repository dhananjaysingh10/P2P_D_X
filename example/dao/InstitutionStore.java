package com.example.dao;

import com.example.constants.ShardKey;
import com.example.entity.Institution;
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
public class InstitutionStore {

    private static final int MAX_FETCH_COUNT = 100;
    private final RelationalDao<Institution> institutionRelationalDao;

    public List<Institution> getAll(String shardKey) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Institution.class);
            return institutionRelationalDao.select(ShardKey.SHARD_KEY, criteria, 0, MAX_FETCH_COUNT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch institutions", e);
        }
    }

    public Optional<Institution> getById(String shardKey, Long id) {
        try {
            return institutionRelationalDao.get(ShardKey.SHARD_KEY, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch institution: " + id, e);
        }
    }

    public void create(String shardKey, Institution institution) {
        try {
            institutionRelationalDao.save(ShardKey.SHARD_KEY, institution);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create institution", e);
        }
    }

    public void update(String shardKey, Long id, Institution updatedInstitution) {
        try {
            institutionRelationalDao.update(ShardKey.SHARD_KEY,
                    DetachedCriteria.forClass(Institution.class)
                            .add(Restrictions.eq("id", id)),
                    institution -> {
                        institution.setName(updatedInstitution.getName());
                        institution.setEmail(updatedInstitution.getEmail());
                        institution.setPhone(updatedInstitution.getPhone());
                        institution.setAddress(updatedInstitution.getAddress());
                        return institution;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Failed to update institution: " + id, e);
        }
    }

    public boolean exists(String shardKey, Long id) {
        return getById(shardKey, id).isPresent();
    }
}
