package com.erastedev.ciexplore.v1.adapters.web.http.authentification;

import com.erastedev.ciexplore.v1.adapters.web.api.endpoints.Endpoint;
import com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse;
import com.erastedev.ciexplore.v1.adapters.web.api.service.ApiResponseService;
import com.erastedev.ciexplore.v1.adapters.web.api.service.builder.ApiBuilder;
import com.erastedev.ciexplore.v1.application.request.user.*;
import com.erastedev.ciexplore.v1.application.services.logs.LogServiceImpl;
import com.erastedev.ciexplore.v1.application.services.auth.AuthenticationServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.auth.AuthenticationRecord;
import com.erastedev.ciexplore.v1.application.services.user.auth.AuthenticationResponse;
import com.erastedev.ciexplore.v1.application.services.user.auth.AuthenticationResult;
import com.erastedev.ciexplore.v1.domain.entities.user.model.User;
import com.erastedev.ciexplore.v1.domain.models.AuditLogActionCode;
import com.erastedev.ciexplore.v1.domain.models.logs.Loggable;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserRegisterAttempt;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserRegisterState;
import com.erastedev.ciexplore.v1.infrastructure.utils.EnumToStringConverter;
import com.erastedev.ciexplore.v1.infrastructure.utils.HttpRequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.AUTH)
@Tag(name = "User Authentication API", description = "Operations related to User Authentication")
public class AuthenticationController {
    @Autowired
    public AuthenticationServiceImpl authService;

    @Autowired
    public UserServiceImpl userService;

    @Lazy
    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    public HttpRequestUtil httpRequestUtil;

    @Autowired
    public LogServiceImpl auditService;

    public Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    public ApiResponseService response;

    public Class<?> className() {
        return this.getClass();
    }

    /**
     * Authenticates a request with the provided username and password.
     * If authentication is successful, returns a response with the authentication
     * token and request details.
     * If authentication fails, returns an error response indicating invalid
     * credentials.
     * Handles exceptions and returns an internal server error response in case of
     * unexpected errors.
     *
     * @param request the UserSignInRequest containing the username and password
     * @return a ResponseEntity containing an ApiResponse with the authentication
     * token and request details, or an error message
     */
    @PostMapping(Endpoint.LOGIN_USER)
    @Operation(summary = "Login a request", description = "Logs in a request and returns the authentication token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Loggable(action = AuditLogActionCode.AUTH_LOGIN_SUCCESS, message = "User logged in", actionFailed = AuditLogActionCode.AUTH_LOGIN_FAILED)
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody SignInRequest request) {
        try {
            AuthenticationResponse authResponse = new AuthenticationResponse(null, null);
            AuthenticationResult result = authService.authenticate(request);

            if (result.isSuccess()) {
                User userAttempt = userService.getUserByUsernameOrEmail(request.getUsername());
                AuthenticationRecord record = new AuthenticationRecord(result.getToken(), "Bearer");

                authResponse = authService.buildAuthenticationResponse(record, userAttempt);
                return response.success("User logged in successfully", authResponse, HttpStatus.OK);
            }

            authResponse.setError(result.getError());
            return response.error(result.getError().getMessage(), result.getError().getMessage(), null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ApiBuilder<AuthenticationResponse>().internalServerError("Error logging in request", e);
        }
    }

    /**
     * Sends an invitation email to the specified email address to create a user
     * account.
     * <p>
     * This endpoint accepts an InviteUserRequest containing the email address to
     * send
     * the invitation to. It constructs the invitation link using the base URL from
     * the
     * incoming HttpServletRequest. If successful, it returns a response indicating
     * the
     * user was invited successfully along with the invitation details.
     *
     * @param params  the InviteUserRequest containing the email address for the
     *                invitation
     * @param request the HttpServletRequest used to retrieve the base URL for the
     *                invitation link
     * @return a ResponseEntity containing an ApiResponse with the invitation
     * details if successful,
     * or an error message if the invitation fails
     * @throws Exception if there is an error during the invitation process
     */
    @PostMapping(Endpoint.INVITE_USER)
    @Operation(summary = "Send user invitation to create an account", description = "Sends an invitation to create an account with the provided email address")
    public ResponseEntity<ApiResponse<InviteUserResponse>> inviteUser(@RequestBody InviteUserRequest params,
                                                                      HttpServletRequest request) {
        try {
            InviteUserResponse data = authService.inviteUser(params);
            return switch (data.getInvited()) {
                case ALREADY_INVITED -> {
                    yield response.error("User already invited", "Email already used", null, HttpStatus.BAD_REQUEST);
                }
                case REJECTED -> {
                    yield response.error("User invitation rejected", "The invitation was not accepted", null, HttpStatus.BAD_REQUEST);
                }
                default -> {
                    User invited = new User();
                    yield response.success("User has been invited", data, HttpStatus.OK);
                }
            };
        } catch (Exception e) {
            return response.error("Error registering user", e.getMessage(), null, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Registers a new user by creating an account based on the provided registration details.
     * Sends a confirmation email upon successful registration of the user.
     * If registration fails, an appropriate error response is returned.
     *
     * @param user the SignUpRequest object containing the registration details for the new user
     * @return a ResponseEntity containing an ApiResponse with the created User details if
     * registration is successful, or an error message if the registration fails
     */
    @PostMapping(Endpoint.REGISTER_USER)
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns the created user")
    public ResponseEntity<ApiResponse<User>> registerUser(
            @RequestBody SignUpRequest user
    ) {
        try {
            UserRegisterAttempt attempt = authService.registerUser(user);

            if (attempt.getState() == null || attempt.getState().equals(UserRegisterState.SUCCESS)) {
                authService.SendConfirmRegisterMail(attempt.getUser());
                return response.success("User registered successfully", attempt.getUser(), HttpStatus.CREATED);
            }

            return response.error(EnumToStringConverter.enumToSentence(attempt.getState()), attempt.getState().toString(), null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return response.error("Error registering user", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Verify email after registration
     * <p>
     * This endpoint accepts an email address and a verification code as parameters.
     * It checks if the provided verification code matches the one associated with the email
     * address and returns a success response if the verification is successful.
     * If the verification fails, it returns an error response indicating the failure.
     * If an unexpected error occurs, it returns an internal server error response.
     *
     * @param request the VerifyEmailRequest containing the email address and verification code
     * @return a ResponseEntity containing an ApiResponse with a boolean indicating the success
     * of the verification, or an error message if the verification fails
     */
    @PostMapping(Endpoint.VERIFY_EMAIL_WITH_CODE)
    @Operation(summary = "Verify Email", description = "Verify email after registration")
    public ResponseEntity<ApiResponse<Boolean>> verifyEmailFromCode(
            @RequestBody VerifyEmailRequest request
    ) {
        try {
            UserRegisterAttempt attempt = userService.verifyEmail(request);

            if (attempt.getState() == null || attempt.getState().equals(UserRegisterState.SUCCESS)) {
                authService.SendConfirmRegisterMail(attempt.getUser());
                return response.success("User registered successfully", true, HttpStatus.CREATED);
            }

            return response.error(EnumToStringConverter.enumToSentence(attempt.getState()), attempt.getState().toString(), null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return response.error("Error registering user", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Sends a recovery code to the email address associated with the user for
     * password recovery.
     * <p>
     * This endpoint accepts an email address as a parameter and sends a recovery
     * code to the associated email address.
     * If the recovery code is sent successfully, it returns a response indicating
     * the recovery code was sent successfully.
     * If the recovery code could not be sent, it returns an error response
     * indicating the error message.
     *
     * @param email the email address to send the recovery code to
     * @return a ResponseEntity containing an ApiResponse with the result of the
     * password recovery request
     * @throws Exception if there is an error sending the recovery code
     */
    @PostMapping(Endpoint.FORGET_PASSWORD)
    @Operation(summary = "Request password recovery", description = "Sends a recovery code to the user's email for password recovery")
    public ResponseEntity<ApiResponse<Boolean>> requestPasswordRecovery(
            @RequestParam String email, HttpServletRequest request
    ) {
        try {
            boolean emailSent = authService.sendRecoveryCode(email);
            if (emailSent) {
                return response.success("Recovery code sent successfully", true, HttpStatus.OK);
            }
            return response.error("Email not found", "Email not found", null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return response.error("Email not found", "Email not found", null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint to verify the validity of a password recovery code.
     * <p>
     * This endpoint accepts an email address and a recovery code as parameters.
     * It checks if the provided recovery code matches the one associated with the
     * email address and has not expired. If the code is valid, it returns a success
     * response. If the code is invalid or expired, it returns an error response.
     * <p>
     * Possible HTTP responses:
     * - 200 OK: Recovery code is valid.
     * - 400 BAD REQUEST: Recovery code is invalid or expired.
     * - 500 INTERNAL SERVER ERROR: An unexpected error occurred while verifying the
     * recovery code.
     *
     * @param email the email address associated with the recovery code
     * @param code  the recovery code to verify
     * @return a ResponseEntity containing an ApiResponse with the verification
     * result
     */
    @PostMapping(Endpoint.VERIFY_RECOVERY_CODE)
    @Operation(summary = "Verify password recovery code", description = "Verifies if the provided recovery code is valid")
    public ResponseEntity<ApiResponse<String>> verifyRecoveryCode(
            @RequestParam String email, @RequestParam String code, HttpServletRequest request) {
        try {
            boolean isValid = authService.verifyRecoveryCode(email, code);
            if (isValid) {
                return response.success("Recovery code verified successfully", "Code is valid.", HttpStatus.OK);
            }
            return response.error("Invalid recovery code", "The provided recovery code is incorrect or has expired.", null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return response.error("Error verifying recovery code", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to reset a user's password using a valid recovery code.
     * <p>
     * This endpoint accepts an email address, a recovery code, and a new password
     * as parameters. It verifies if the provided recovery code matches the one
     * associated with the email address and has not expired. If the code is valid,
     * it resets the user's password with the provided new password. If the code
     * is invalid or expired, it returns an error response. If the password is
     * reset successfully, it returns a success response. If an unexpected error
     * occurs, it returns an internal server error response.
     * <p>
     * Possible HTTP responses:
     * - 200 OK: Password reset successfully.
     * - 400 BAD REQUEST: Recovery code is invalid or expired.
     * - 500 INTERNAL SERVER ERROR: An unexpected error occurred while resetting the
     * password.
     *
     * @param email       the email address associated with the recovery code
     * @param code        the recovery code to verify
     * @param newPassword the new password to set
     * @return a ResponseEntity containing an ApiResponse with the reset result
     */
    @PostMapping(Endpoint.RESET_PASSWORD)
    @Operation(summary = "Reset user password", description = "Allows the user to reset their password using a valid recovery code")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @RequestParam String email,
            @RequestParam String code,
            @RequestParam String newPassword, HttpServletRequest request) {
        try {
            boolean isReset = authService.resetPassword(email, code, newPassword);
            if (isReset) {
                return response.success("Password reset successfully", "Your password has been updated.", HttpStatus.OK);
            }
            return response.error("Invalid recovery code", "The provided recovery code is incorrect or has expired.", null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return response.error("Error resetting password", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Generates a new JWT token based on the provided expired token.
     * <p>
     * This endpoint accepts an expired JWT token as a parameter and returns a
     * new JWT token with the same username and expiration time as the original
     * token. If the provided token is invalid or has not expired, it returns an
     * error response.
     * <p>
     * Possible HTTP responses:
     * - 200 OK: A new JWT token was generated successfully.
     * - 400 BAD REQUEST: The provided token is invalid or has not expired.
     * - 500 INTERNAL SERVER ERROR: An unexpected error occurred while generating
     * the
     * new token.
     *
     * @param token the expired JWT token to refresh
     * @return a ResponseEntity containing an ApiResponse with the new JWT token
     */
    @PostMapping(Endpoint.REFRESH_TOKEN)
    @Operation(summary = "Refresh JWT token", description = "Refreshes the JWT token based on an expired token")
    public ResponseEntity<ApiResponse<String>> refreshToken(@RequestParam String token) {
        String newToken = authService.refreshToken(token);
        return response.success("Password reset successfully", newToken, HttpStatus.OK);
    }

    /**
     * Logs out the current user.
     * <p>
     * This endpoint takes no parameters and simply logs out the current user.
     * <p>
     * Possible HTTP responses:
     * - 200 OK: The user was logged out successfully.
     * - 500 INTERNAL SERVER ERROR: An unexpected error occurred while logging out
     * the user.
     *
     * @return a ResponseEntity containing an ApiResponse with the status of the
     * logout operation
     */
    @PostMapping(Endpoint.LOGOUT_USER)
    @Operation(summary = "Logout user", description = "Logs out the current user")
    public ResponseEntity<ApiResponse<Boolean>> logout() {
        boolean logoutStatus = authService.logoutUser(null, null);
        return response.success("Logout successful", logoutStatus, HttpStatus.OK);
    }

    @GetMapping(Endpoint.USER_DETAIL)
    @Operation(summary = "retrieve information from the logged in user", description = "this api allows you to retrieve information from the logged in user")
    public ResponseEntity<ApiResponse<User>> getUser() {

        try {
            User user = authService.getCurrentLoggedUser();
            if (user != null) {
                // Exclure le champ sensible
                user.setPassword(null);

                return response.success("profile information has been retrieved successfully", user, HttpStatus.OK);

            }
            return response.error(
                    "profile information could not be retrieved",
                    "profile information could not be retrieved",
                    null,
                    HttpStatus.BAD_REQUEST
            );

        } catch (Exception e) {
            return response.error("information unavailable", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
