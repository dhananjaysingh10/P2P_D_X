package com.example.resources;

import com.example.api.ApiResponse;
import com.example.models.User;
import com.example.service.UserService;
import com.codahale.metrics.annotation.Timed;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * REST Resource for User operations
 */
@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name="User Resource", description="APIs for User operations")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UserResource {

    private final UserService userService;

    /**
     * Register a new user
     * POST /api/v1/users/register
     */
    @POST
    @Path("/register")
    @Timed
    public Response registerUser(@Valid @NotNull User user) {
        try {
            log.info("Register user request received for email: {}", user.getEmail());

            User registeredUser = userService.registerUser(user);

            return Response
                    .status(Response.Status.CREATED)
                    .entity(ApiResponse.success(registeredUser, "User registered successfully"))
                    .build();

        } catch (IllegalArgumentException e) {
            log.error("Validation error during user registration: {}", e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Validation failed", e.getMessage()))
                    .build();

        } catch (IllegalStateException e) {
            log.error("User already exists: {}", e.getMessage());
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity(ApiResponse.error("User already exists", e.getMessage()))
                    .build();

        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage(), e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error", "Failed to register user"))
                    .build();
        }
    }

    /**
     * Get user by email
     * GET /api/v1/users/email/{email}
     */
    @GET
    @Path("/email/{email}")
    @Timed
    public Response getUserByEmail(@PathParam("email") String email) {
        try {
            log.info("Get user request received for email: {}", email);

            User user = userService.getUserByEmail(email);

            return Response
                    .ok(ApiResponse.success(user, "User retrieved successfully"))
                    .build();

        } catch (IllegalArgumentException e) {
            log.error("User not found: {}", e.getMessage());
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User not found", e.getMessage()))
                    .build();

        } catch (Exception e) {
            log.error("Error getting user: {}", e.getMessage(), e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error", "Failed to get user"))
                    .build();
        }
    }

    /**
     * Get user by PAN
     * GET /api/v1/users/pan/{pan}
     */
    @GET
    @Path("/pan/{pan}")
    @Timed
    public Response getUserByPan(@PathParam("pan") String pan) {
        try {
            log.info("Get user request received for PAN: {}", pan);

            User user = userService.getUserByPan(pan);

            return Response
                    .ok(ApiResponse.success(user, "User retrieved successfully"))
                    .build();

        } catch (IllegalArgumentException e) {
            log.error("User not found: {}", e.getMessage());
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User not found", e.getMessage()))
                    .build();

        } catch (Exception e) {
            log.error("Error getting user: {}", e.getMessage(), e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error", "Failed to get user"))
                    .build();
        }
    }

    /**
     * Update user
     * PUT /api/v1/users/{email}
     */
    @PUT
    @Path("/{email}")
    @Timed
    public Response updateUser(@PathParam("email") String email, @Valid @NotNull User user) {
        try {
            log.info("Update user request received for email: {}", email);

            User updatedUser = userService.updateUser(email, user);

            return Response
                    .ok(ApiResponse.success(updatedUser, "User updated successfully"))
                    .build();

        } catch (IllegalArgumentException e) {
            log.error("User not found: {}", e.getMessage());
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User not found", e.getMessage()))
                    .build();

        } catch (IllegalStateException e) {
            log.error("Conflict during update: {}", e.getMessage());
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity(ApiResponse.error("Conflict", e.getMessage()))
                    .build();

        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage(), e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error", "Failed to update user"))
                    .build();
        }
    }

    /**
     * Get all beneficiaries
     * GET /api/v1/users/beneficiaries
     */
    @GET
    @Path("/beneficiaries")
    @Timed
    public Response getAllBeneficiaries() {
        try {
            log.info("Get all beneficiaries request received");

            List<User> beneficiaries = userService.getAllBeneficiaries();

            return Response
                    .ok(ApiResponse.success(beneficiaries, "Beneficiaries retrieved successfully"))
                    .build();

        } catch (Exception e) {
            log.error("Error getting beneficiaries: {}", e.getMessage(), e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error", "Failed to get beneficiaries"))
                    .build();
        }
    }

    /**
     * Check if email exists
     * GET /api/v1/users/check/email/{email}
     */
    @GET
    @Path("/check/email/{email}")
    @Timed
    public Response checkEmailExists(@PathParam("email") String email) {
        try {
            log.info("Check email exists request received for: {}", email);

            boolean exists = userService.isEmailExists(email);

            return Response
                    .ok(ApiResponse.success(exists, exists ? "Email exists" : "Email available"))
                    .build();

        } catch (Exception e) {
            log.error("Error checking email: {}", e.getMessage(), e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error", "Failed to check email"))
                    .build();
        }
    }

    /**
     * Check if PAN exists
     * GET /api/v1/users/check/pan/{pan}
     */
    @GET
    @Path("/check/pan/{pan}")
    @Timed
    public Response checkPanExists(@PathParam("pan") String pan) {
        try {
            log.info("Check PAN exists request received for: {}", pan);

            boolean exists = userService.isPanExists(pan);

            return Response
                    .ok(ApiResponse.success(exists, exists ? "PAN exists" : "PAN available"))
                    .build();

        } catch (Exception e) {
            log.error("Error checking PAN: {}", e.getMessage(), e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error", "Failed to check PAN"))
                    .build();
        }
    }
}
