package com.example.service;

import com.example.dao.InstitutionStore;
import com.example.entity.Institution;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionStore institutionStore;

    @Override
    public List<Institution> getAllInstitutions(String shardKey) {
        return institutionStore.getAll(shardKey);
    }

    @Override
    public Optional<Institution> getInstitutionById(String shardKey, Long id) {
        return institutionStore.getById(shardKey, id);
    }

    @Override
    public void createInstitution(String shardKey, Institution institution) {
        institutionStore.create(shardKey, institution);
    }

    @Override
    public void updateInstitution(String shardKey, Long id, Institution institution) {
        if (!institutionStore.exists(shardKey, id)) {
            throw new RuntimeException("Institution not found: " + id);
        }
        institutionStore.update(shardKey, id, institution);
    }

    @Override
    public boolean institutionExists(String shardKey, Long id) {
        return institutionStore.exists(shardKey, id);
    }
}
