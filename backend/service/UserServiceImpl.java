package com.example.service;

import com.example.dao.UserStore;
import com.example.entity.User;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of UserService
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UserServiceImpl implements UserService {

    private final UserStore userStore;

    @Override
    @UnitOfWork
    public com.example.models.User registerUser(com.example.models.User user) {
        log.info("Registering new user with email: {}", user.getEmail());

        // Validate user data
        validateUserForRegistration(user);

        // Check if email already exists
        if (userStore.existsByEmail(user.getEmail())) {
            log.warn("User registration failed: Email already exists - {}", user.getEmail());
            throw new IllegalStateException("User with email " + user.getEmail() + " already exists");
        }

        // Check if PAN already exists
        if (userStore.existsByPan(user.getPan())) {
            log.warn("User registration failed: PAN already exists - {}", user.getPan());
            throw new IllegalStateException("User with PAN " + user.getPan() + " already exists");
        }

        // Convert model to entity
        User userEntity = convertToEntity(user);

        // Save to database
        User savedEntity = userStore.createUser(userEntity);
        log.info("User registered successfully with ID: {}", savedEntity.getId());

        // Convert back to model and return
        return convertToModel(savedEntity);
    }

    @Override
    @UnitOfWork
    public com.example.models.User getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);

        Optional<User> userEntity = userStore.getUserByEmail(email);

        if (userEntity.isEmpty()) {
            log.warn("User not found with email: {}", email);
            throw new IllegalArgumentException("User not found with email: " + email);
        }

        return convertToModel(userEntity.get());
    }

    @Override
    @UnitOfWork
    public com.example.models.User getUserByPan(String pan) {
        log.info("Fetching user by PAN: {}", pan);

        Optional<User> userEntity = userStore.getUserByPan(pan);

        if (userEntity.isEmpty()) {
            log.warn("User not found with PAN: {}", pan);
            throw new IllegalArgumentException("User not found with PAN: " + pan);
        }

        return convertToModel(userEntity.get());
    }

    @Override
    @UnitOfWork
    public com.example.models.User updateUser(String email, com.example.models.User user) {
        log.info("Updating user with email: {}", email);

        // Check if user exists
        Optional<User> existingEntity = userStore.getUserByEmail(email);

        if (existingEntity.isEmpty()) {
            log.warn("Update failed: User not found with email: {}", email);
            throw new IllegalArgumentException("User not found with email: " + email);
        }

        // If email is being changed, check if new email already exists
        if (user.getEmail() != null && !user.getEmail().equals(email)) {
            if (userStore.existsByEmail(user.getEmail())) {
                throw new IllegalStateException("Email " + user.getEmail() + " already exists");
            }
        }

        // If PAN is being changed, check if new PAN already exists
        if (user.getPan() != null && !user.getPan().equals(existingEntity.get().getPan())) {
            if (userStore.existsByPan(user.getPan())) {
                throw new IllegalStateException("PAN " + user.getPan() + " already exists");
            }
        }

        // Update user using mutator
        userStore.updateUser(email, userEntity -> {
            if (user.getName() != null) {
                userEntity.setName(user.getName());
            }
            if (user.getEmail() != null) {
                userEntity.setEmail(user.getEmail());
            }
            if (user.getPhone() != null) {
                userEntity.setPhone(user.getPhone());
            }
            if (user.getPan() != null) {
                userEntity.setPan(user.getPan());
            }
            if (user.getIsBeneficiary() != null) {
                userEntity.setIsBeneficiary(user.getIsBeneficiary());
            }
            return userEntity;
        });

        log.info("User updated successfully with email: {}", email);

        // Fetch and return updated user
        return getUserByEmail(user.getEmail() != null ? user.getEmail() : email);
    }

    @Override
    @UnitOfWork
    public List<com.example.models.User> getAllBeneficiaries() {
        log.info("Fetching all beneficiaries");

        // Use a default lookup key - you might want to make this configurable
        List<User> entities = userStore.getAllBeneficiaries("default");

        return entities.stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    @UnitOfWork
    public boolean isEmailExists(String email) {
        return userStore.existsByEmail(email);
    }

    @Override
    @UnitOfWork
    public boolean isPanExists(String pan) {
        return userStore.existsByPan(pan);
    }

    /**
     * Validate user data for registration
     */
    private void validateUserForRegistration(com.example.models.User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("User name is required");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User email is required");
        }

        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("User phone is required");
        }

        if (user.getPan() == null || user.getPan().trim().isEmpty()) {
            throw new IllegalArgumentException("User PAN is required");
        }

        // Validate beneficiary-specific fields if user is beneficiary
        if (Boolean.TRUE.equals(user.getIsBeneficiary())) {
            if (!user.isValidBeneficiary()) {
                throw new IllegalArgumentException("Invalid beneficiary data");
            }
        } else {
            if (!user.isValidDonor()) {
                throw new IllegalArgumentException("Invalid donor data");
            }
        }
    }

    /**
     * Convert User model to UserEntity
     */
    private User convertToEntity(com.example.models.User user) {
        return User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .pan(user.getPan())
                .aadharCard(user.getAadharCard())
                .isBeneficiary(user.getIsBeneficiary())
                .build();
    }

    /**
     * Convert UserEntity to User model
     */
    private com.example.models.User convertToModel(User entity) {
        return com.example.models.User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .pan(entity.getPan())
                .aadharCard(entity.getAadharCard())
                .isBeneficiary(entity.getIsBeneficiary())
                .build();
    }
}
