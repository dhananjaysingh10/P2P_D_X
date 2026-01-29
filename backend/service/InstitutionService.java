package com.example.service;

import com.example.entity.Institution;

import java.util.List;
import java.util.Optional;

public interface InstitutionService {

    List<Institution> getAllInstitutions(String shardKey);

    Optional<Institution> getInstitutionById(String shardKey, Long id);

    void createInstitution(String shardKey, Institution institution);

    void updateInstitution(String shardKey, Long id, Institution institution);

    boolean institutionExists(String shardKey, Long id);
}
