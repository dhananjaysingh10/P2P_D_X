package com.example.dao;

import com.example.constants.ShardKey;
import com.example.entity.User;
import io.appform.dropwizard.sharding.dao.RelationalDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * User Store for database operations using RelationalDao
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UserStore {

    private static final String EMAIL_PARAM = "email";
    private static final String PAN_PARAM = "pan";
    private static final String IS_BENEFICIARY_PARAM = "isBeneficiary";
    private static final int MAX_FETCH_COUNT = 1000;

    private final RelationalDao<User> userRelationalDao;

    /**
     * Create a new user - Always uses shard "1"
     */
    public User createUser(User user) {
        try {
            log.info("Creating user with email: {}", user.getEmail());
            Optional<User> savedUser = userRelationalDao.save(ShardKey.SHARD_KEY, user);
            return savedUser.orElseThrow(() -> new RuntimeException("Failed to save user"));
        } catch (Exception e) {
            log.error("Failed to create user: {}", user.getEmail(), e);
            throw new RuntimeException("Failed to create user: " + user.getEmail(), e);
        }
    }

    /**
     * Get user by email - Always uses shard "1"
     */
    public Optional<User> getUserByEmail(String email) {
        try {
            log.debug("Fetching user by email: {}", email);
            DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
            criteria.add(Restrictions.eq(EMAIL_PARAM, email));

            List<User> users = userRelationalDao.select(ShardKey.SHARD_KEY, criteria, 0, 1);
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        } catch (Exception e) {
            log.error("Failed to fetch user by email: {}", email, e);
            throw new RuntimeException("Failed to fetch user by email: " + email, e);
        }
    }

    /**
     * Get user by PAN - Always uses shard "1"
     */
    public Optional<User> getUserByPan(String pan) {
        try {
            log.debug("Fetching user by PAN: {}", pan);
            DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
            criteria.add(Restrictions.eq(PAN_PARAM, pan));

            List<User> users = userRelationalDao.select(ShardKey.SHARD_KEY, criteria, 0, 1);
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        } catch (Exception e) {
            log.error("Failed to fetch user by PAN: {}", pan, e);
            throw new RuntimeException("Failed to fetch user by PAN: " + pan, e);
        }
    }

    /**
     * Get user by ID - Always uses shard "1"
     */
    public Optional<User> getUserById(String lookupKey, Long id) {
        try {
            log.debug("Fetching user by ID: {}", id);
            return userRelationalDao.get(ShardKey.SHARD_KEY, id);
        } catch (Exception e) {
            log.error("Failed to fetch user by ID: {}", id, e);
            throw new RuntimeException("Failed to fetch user by ID: " + id, e);
        }
    }

    /**
     * Get all beneficiaries - Always uses shard "1"
     */
    public List<User> getAllBeneficiaries(String lookupKey) {
        try {
            log.debug("Fetching all beneficiaries");
            DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
            criteria.add(Restrictions.eq(IS_BENEFICIARY_PARAM, true));

            return userRelationalDao.select(ShardKey.SHARD_KEY, criteria, 0, MAX_FETCH_COUNT);
        } catch (Exception e) {
            log.error("Failed to fetch beneficiaries", e);
            throw new RuntimeException("Failed to fetch beneficiaries", e);
        }
    }

    /**
     * Update user - Always uses shard "1"
     */
    public void updateUser(String email, UnaryOperator<User> mutator) {
        try {
            log.info("Updating user with email: {}", email);
            userRelationalDao.update(ShardKey.SHARD_KEY,
                DetachedCriteria.forClass(User.class)
                    .add(Restrictions.eq(EMAIL_PARAM, email)),
                mutator);
        } catch (Exception e) {
            log.error("Failed to update user: {}", email, e);
            throw new RuntimeException("Failed to update user: " + email, e);
        }
    }

    /**
     * Check if user exists by email
     */
    public boolean existsByEmail(String email) {
        return getUserByEmail(email).isPresent();
    }

    /**
     * Check if user exists by PAN
     */
    public boolean existsByPan(String pan) {
        return getUserByPan(pan).isPresent();
    }
}
