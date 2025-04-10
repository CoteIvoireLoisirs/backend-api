package ca.deltagis.success.v1.domain.ports.in.user.auth;

import ca.deltagis.success.v1.application.request.user.InviteUserRequest;
import ca.deltagis.success.v1.application.request.user.InviteUserResponse;
import ca.deltagis.success.v1.application.request.user.UserSignInRequest;
import ca.deltagis.success.v1.application.request.user.UserSignUpRequest;
import ca.deltagis.success.v1.application.services.user.auth.AuthenticationRecord;
import ca.deltagis.success.v1.application.services.user.auth.AuthenticationResponse;
import ca.deltagis.success.v1.application.services.user.auth.AuthenticationResult;
import ca.deltagis.success.v1.domain.core.entities.user.User;
import ca.deltagis.success.v1.domain.core.entities.user.UserProfile;
import ca.deltagis.success.v1.domain.core.models.user.UserRegisterAttempt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface IUserAuthService {
    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the user details if found, or an empty Optional if no such user exists
     */
    Optional<UserDetails> findByUsername(String username);

    /**
     * Authenticates a user given their username and password.
     *
     * @param param the user to authenticate
     * @return an AuthenticationResult containing the result of the authentication attempt
     */
    AuthenticationResult authenticate(UserSignInRequest param);

    /**
     * Builds an AuthenticationResponse containing the authentication record,
     * user details, and user profiles.
     *
     * @param record the AuthenticationRecord containing the token and type
     * @param user   the User object containing user details
     * @param rights the HashMap containing user rights
     * @return an AuthenticationResponse containing the authentication details
     */
    AuthenticationResponse buildAuthenticationResponse(AuthenticationRecord record, User user, HashMap<String, HashMap<String, Boolean>> rights);

    /**
     * Authenticates a user given their username and password.
     *
     * @param user the user to authenticate
     * @return an AuthenticationResult containing the result of the authentication attempt
     */
    InviteUserResponse inviteUser(InviteUserRequest user);

    /**
     * Saves a user to the database, encrypting their password first.
     *
     * @param user the user to be saved
     * @return the saved user
     */
    UserRegisterAttempt registerUser(UserSignUpRequest user, String tokenInvitation);

    /**
     * Registers the first user in the system, typically an admin.
     *
     * @param admin the user to be registered as the first user
     * @return the registered user
     */
    User registerFirstUser(UserSignUpRequest admin, String secretKey);

    /**
     * Sends a password recovery code to the email address associated with the provided email.
     *
     * @param email the email address to send the recovery code to
     * @throws Exception if there is an error sending the recovery code
     */
    boolean sendRecoveryCode(String email) throws Exception;

    /**
     * Verifies that the provided recovery code matches the one sent to the
     * email address associated with the given email.
     *
     * @param email the email address associated with the recovery code
     * @param code  the recovery code to verify
     * @return true if the code is valid, false otherwise
     */
    boolean verifyRecoveryCode(String email, String code);

    /**
     * Resets the password associated with the email address to the provided
     * newPassword if the given recovery code matches the one sent to the email
     * address.
     *
     * @param email       the email address associated with the recovery code
     * @param code        the recovery code to verify
     * @param newPassword the new password to set
     * @return true if the password was reset successfully, false otherwise
     */
    boolean resetPassword(String email, String code, String newPassword);

    /**
     * Generates a new JWT token based on the provided expired token.
     *
     * @param token the expired JWT token to refresh
     * @return a new JWT token with the same username and expiration time as the original token
     */
    String refreshToken(String token);

    /**
     * Sends a password recovery code to the email address associated with the provided email.
     *
     * @param email the email address to send the registration validation message
     * @throws Exception if there is an error sending the registration validation message
     */
    boolean SendRegisterConfirmationEmailMessage(String email) throws Exception;

    /**
     * Calculates the duration of the user's connection in minutes.
     * <p>
     * This method retrieves the last login timestamp of the user
     * and calculates the difference in time between the last login
     * and the current time. If the user is not connected (i.e., last
     * login timestamp is null), it returns -1 to indicate the user
     * is not connected.
     *
     * @param user the user for whom the connection duration is calculated
     * @return the duration of the user's connection in minutes, or -1 if the user is not connected
     */
    long userConexionDuration(User user);

    /**
     * Authenticates a user given their username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return true if the credentials are valid, false otherwise
     */
    boolean authenticateUser(String username, String password);

    /**
     * Updates the connection status of a user.
     * <p>
     * This method updates the connection status of the user by setting the
     * <code>connected</code> field to true and the <code>lastLogin</code> field
     * to the current time.
     *
     * @param user the user whose connection status is updated
     */
    void updateUserConnectionStatus(User user);

    /**
     * Logs out the current user from the application.
     * <p>
     * This method clears the user's connection status, clears the security
     * context, and invalidates the HTTP session if it exists.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return true if the user is successfully logged out, false otherwise
     */
    boolean logoutUser(HttpServletRequest request, HttpServletResponse response);

    User getCurrentLoggedUser();

    Long getLoggedUserId();
}
