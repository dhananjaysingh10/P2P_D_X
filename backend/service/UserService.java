package com.example.service;

import com.example.models.User;

import java.util.List;

/**
 * Service interface for User operations
 */
public interface UserService {

    /**
     * Register a new user
     *
     * @param user User to register
     * @return Registered user with ID
     * @throws IllegalArgumentException if user data is invalid
     * @throws IllegalStateException if user already exists
     */
    User registerUser(User user);

    /**
     * Get user by email
     *
     * @param email User email
     * @return User if found
     * @throws IllegalArgumentException if user not found
     */
    User getUserByEmail(String email);

    /**
     * Get user by PAN
     *
     * @param pan User PAN
     * @return User if found
     * @throws IllegalArgumentException if user not found
     */
    User getUserByPan(String pan);

    /**
     * Update user details
     *
     * @param email User email
     * @param user Updated user data
     * @return Updated user
     * @throws IllegalArgumentException if user not found
     */
    User updateUser(String email, User user);

    /**
     * Get all beneficiaries
     *
     * @return List of beneficiaries
     */
    List<User> getAllBeneficiaries();

    /**
     * Check if email already exists
     *
     * @param email Email to check
     * @return true if email exists
     */
    boolean isEmailExists(String email);

    /**
     * Check if PAN already exists
     *
     * @param pan PAN to check
     * @return true if PAN exists
     */
    boolean isPanExists(String pan);
}
