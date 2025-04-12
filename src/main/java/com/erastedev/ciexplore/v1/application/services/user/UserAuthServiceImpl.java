package com.erastedev.ciexplore.v1.application.services.user;

import com.erastedev.ciexplore.v1.adapters.web.exception.ExpiredInvitationCodeException;
import com.erastedev.ciexplore.v1.adapters.web.exception.InvalidInvitationCodeException;
import com.erastedev.ciexplore.v1.adapters.web.message.user.AuthLoginError;
import com.erastedev.ciexplore.v1.application.request.user.*;
import com.erastedev.ciexplore.v1.application.services.language.LangServiceImpl;
import com.erastedev.ciexplore.v1.application.services.notification.NotificationService;
import com.erastedev.ciexplore.v1.application.services.rights.RightServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.auth.AuthenticationRecord;
import com.erastedev.ciexplore.v1.application.services.user.auth.AuthenticationResponse;
import com.erastedev.ciexplore.v1.application.services.user.auth.AuthenticationResult;
import com.erastedev.ciexplore.v1.application.services.user.auth.JwtServiceImpl;
import com.erastedev.ciexplore.v1.application.services.workspace.WorkspaceServiceImpl;
import com.erastedev.ciexplore.v1.domain.entities.rights.Right;
import com.erastedev.ciexplore.v1.domain.entities.user.User;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserInvitationState;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserMapper;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserRegisterAttempt;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserRegisterState;
import com.erastedev.ciexplore.v1.domain.ports.in.user.auth.IUserAuthService;
import com.erastedev.ciexplore.v1.infrastructure.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements IUserAuthService {

    private final UserServiceImpl userService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtServiceImpl jwtService;

    private final RightServiceImpl rightService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WorkspaceServiceImpl workspaceService;

    @Autowired
    private LangServiceImpl langService;

    private static final Logger logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);

    private final String SECRET_PASSWORD = "OuJj0qjFQ5596oBYKBGS8GC0aIuAip";

    private static final long BLOCKING_THRESHOLD_MINUTES = 5;

    private UserRepository userRepository;

    @Autowired
    public UserAuthServiceImpl(
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            UserServiceImpl userService,
            JwtServiceImpl jwtService,
            RightServiceImpl rightService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.rightService = rightService;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the user details if found, or an empty
     * Optional if no such user exists
     */
    public Optional<UserDetails> findByUsername(String username) {
        return userService
                .getOptionalUserByUsername(username)
                .map(this::mapToUserDetails);
    }

    /**
     * Converts a User entity to a UserDetails object.
     *
     * @param user the user entity
     * @return a UserDetails instance representing the user
     */
    private UserDetails mapToUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_USER") // Or use user's actual roles if available
                .build();
    }

    /**
     * Authenticates a user given their username and password.
     *
     * @param param
     * @return the authenticated user if the credentials match, or null if they do
     * not
     */
    @Transactional
    public AuthenticationResult authenticate(UserSignInRequest param) {
        String username = param.getUsername();
        String password = param.getPassword();
        String workspaceCode = param.getWorkspaceCode();
        Optional<User> user = userService.getOptionalUserByEmailOrUsername(username);

        logger.error("user {}", user);

        if (user.isEmpty()) {
            logger.error("User not found");
            return AuthenticationResult.failure(AuthLoginError.USER_NOT_FOUND);
        }

        if (!authenticateUser(username, password)) {
            return AuthenticationResult.failure(AuthLoginError.INVALID_CREDENTIALS);
        }

        updateUserConnectionStatus(user.get());
        String token = generateTokenForUser(user.get());
        return AuthenticationResult.success(token);
    }

    @Override
    public AuthenticationResponse buildAuthenticationResponse(AuthenticationRecord record, User user) {
        return new AuthenticationResponse(record, user);
    }

    public InviteUserResponse inviteUser(InviteUserRequest params) {
        InviteUserResponse invitation = new InviteUserResponse();
        invitation.setUser(null);
        invitation.setInvited(InviteUserState.REJECTED);
        invitation.setLink(null);
        invitation.setEmailSent(false);

        if ((params == null) || (params.getEmail() == null) || (params.getHost() == null)) {
            return invitation;
        }

        // check role
        Right right = rightService.getById((long) params.getRoleId());
        if (right == null) {
            invitation.setInvited(InviteUserState.REJECTED);
            return invitation;
        }

        // #0 check if user is already invited
        Optional<User> userCheck = userService.getOptionalUserByEmail(params.getEmail());
        if (userCheck.isPresent()) {
            invitation.setUser(userCheck.get());
            invitation.setInvited(InviteUserState.ALREADY_INVITED);
            invitation.setLink(null);
            invitation.setEmailSent(false);
            return invitation;
        }

        // #1 create a new user
        String token = jwtService.generateTokenBy(params.getEmail(), 2);
        User user = definieInvitedUser(params);
        try {
            user = userService.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user", e);
        }

        // #2 send email
        boolean emailSent = false;
        try {
            emailSent = notificationService.sendInvitationEmail(user.getEmail(), token, params.getHost());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error sending email", e);
        }

        // #3 return response
        invitation.setUser(user);
        invitation.setEmailSent(emailSent);
        invitation.setLink(params.getHost());
        invitation.setInvited(InviteUserState.PENDING);
        return invitation;
    }

    /**
     * Saves a user to the database, encrypting their password first.
     *
     * @param user the user to be saved
     * @return the saved user
     */
    public UserRegisterAttempt registerUser(UserSignUpRequest user, String tokenInvitation) {
        // initialize username
        UserRegisterAttempt attempt = UserRegisterAttempt.builder()
                .user(UserMapper.mapUserSignUpRequestToUser(user, null, null))
                .state(UserRegisterState.USER_NOT_FOUND)
                .success(false)
                .build();

        // check email, by token
        String emailExtract = jwtService.extractUsername(tokenInvitation);
        if (emailExtract != null && !emailExtract.equals(user.getEmail())
                || jwtService.extractExpiration(tokenInvitation).before(Date.from(java.time.Instant.now()))) {
            attempt.setState(UserRegisterState.INVALID_TOKEN);
            return attempt;
        }

        // check if user already registered
        Optional<User> userCheck = userService.getOptionalUserByEmail(user.getEmail());
        if (userCheck.isPresent()) {
//            if (userCheck.get().getInvitationState().equals(UserInvitationState.ACCEPTED.toString())) {
//                attempt.setState(UserRegisterState.ALREADY_REGISTERED);
//                return attempt;
//            }
        }

        try {
            // * 1 check if user already exists in the database
            if (userService.getOptionalUserByEmail(user.getEmail()).isEmpty()) {
                attempt.setState(UserRegisterState.USER_NOT_FOUND);
                return attempt;
            }

            // * 3 Attempt to register the user
            User searchUser = userService.getUserByEmail(user.getEmail());
            User registration = userService.save(UserMapper.mapUserSignUpRequestToUser(user, passwordEncoder, searchUser));
            if (registration != null) {
                attempt = UserRegisterAttempt.builder()
                        .user(registration)
                        .state(UserRegisterState.SUCCESS)
                        .success(true)
                        .build();
            } else {
                attempt.setState(UserRegisterState.INVALID_EMAIL);
            }
        } catch (InvalidInvitationCodeException e) {
            attempt.setState(UserRegisterState.INVALID_INVITATION_CODE);
        } catch (ExpiredInvitationCodeException e) {
            attempt.setState(UserRegisterState.EXPIRED_INVITATION_CODE);
        } catch (Exception e) {
            attempt.setState(UserRegisterState.INVALID_EMAIL);
        }

        return attempt;
    }

    /**
     * Registers the first user in the system if no users exist in the database and
     * the provided secret key matches the predefined secret password.
     *
     * @param admin     the user to be registered as the first user
     * @param secretKey the secret key required to register the first user
     * @return the registered user if successful, or null if the conditions are not
     * met
     */
    @Override
    public User registerFirstUser(UserSignUpRequest admin, String secretKey) {
        // * 1 check if not user exists in the database
        List<User> users = userService.getAll();
        if (users.isEmpty() && secretKey.equals(SECRET_PASSWORD)) {
            // * 2 check secret key
            if (admin.getUsername() == null) {
                admin.setUsername(admin.getEmail());
            }

            return userService.save(UserMapper.mapUserSignUpRequestToUser(admin, passwordEncoder, null));
        }

        return null;
    }

    /**
     * Defines a new user entity with the given email as the username and sets the
     * invitation status to PENDING.
     *
     * @param params the InviteUserRequest containing the email to be used for the
     *               new user
     * @return the created user
     * @throws IllegalArgumentException if the given params is null
     */
    private User definieInvitedUser(InviteUserRequest params) {
        if (params == null) {
            throw new IllegalArgumentException("params cannot be null");
        }

        User user = new User();
        // user.setRoleId(params.getRoleId());
        user.setEmail(params.getEmail());
        user.setUsername(params.getEmail());
        // user.setInvitationStatus(UserInvitationState.PENDING.toString());
        return user;
    }

    /**
     * Sends a password recovery code to the email address associated with the
     * provided email. The email address is used to retrieve the user from the
     * database and the stored recovery code is compared with the one provided.
     * If the codes match, true is returned. If the codes do not match or the user
     * associated with the email address is not found, false is returned.
     *
     * @param email the email address associated with the recovery code
     * @throws Exception if there is an error sending the recovery code
     */
    @Override
    public boolean sendRecoveryCode(String email) throws Exception {
        String code = generateRandomCode();

        // #1 Stockez le code en base de données avec une date d'expiration associée à
        // l'email
        Optional<User> user = userService.getOptionalUserByEmail(email);
        if (user.isPresent()) {
            // user.get().setRecoveryCode(code);
            userService.save(user.get());

            // #2 Envoyez le code par email
            try {
                return notificationService.sendRecoveryCode(user.get().getEmail(), code);
            } catch (Exception e) {
                logger.info("Error sending email ", e);
                throw new RuntimeException("Error sending email", e);
            }
        }

        return false;
    }

    /**
     * Verifies if the provided recovery code matches the one associated with the
     * email address. The email address is used to retrieve the user from the
     * database and the stored recovery code is compared with the one provided.
     * If the codes match, true is returned. If the codes do not match or the user
     * associated with the email address is not found, false is returned.
     *
     * @param email the email address associated with the recovery code
     * @param code  the recovery code to verify
     * @return true if the recovery code is valid, false otherwise
     */
    @Override
    public boolean verifyRecoveryCode(String email, String code) {
        Optional<User> user = userService.getOptionalUserByEmail(email);
        if (user.isPresent()) {
            User foundUser = user.get();
            // return foundUser.getRecoveryCode().equals(code);
        }

        return false;
    }

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
    @Override
    public boolean resetPassword(String email, String code, String newPassword) {
        if (verifyRecoveryCode(email, code)) {
            Optional<User> user = userService.getOptionalUserByEmail(email);
            if (user.isPresent()) {
                user.get().setPassword(passwordEncoder.encode(newPassword));
                userService.save(user.get());
            }
            return true;
        }
        return false;
    }

    /**
     * Generates a random 6-digit code.
     *
     * @return a 6-digit code as a string
     */
    private String generateRandomCode() {
        return String.valueOf(new Random().nextInt(999999));
    }

    /**
     * Refreshes the JWT token if it is expired.
     * <p>
     * This method checks if the provided token is expired. If expired, it extracts
     * the username
     * from the token and generates a new refresh token using the username. If the
     * token is not
     * expired, it returns the original token.
     *
     * @param oldToken the token to be checked and potentially refreshed
     * @return a new refresh token if the old token is expired, otherwise the old
     * token
     */
    public String refreshToken(String oldToken) {
        if (jwtService.isTokenExpired(oldToken)) {
            // Extract the username from the token
            String username = jwtService.extractUsername(oldToken);
            Optional<UserDetails> userDetails = findByUsername(username);
            if (userDetails.isPresent()) {
                return jwtService.generateRefreshToken(String.valueOf(userDetails.get()));
            } else {
                logger.warn("User with username {} not found during token refresh.", username);
                throw new RuntimeException("User not found for token refresh.");
            }
        }
        return oldToken;
    }

    /**
     * Sends a registration confirmation email to the provided email address.
     * <p>
     * This method checks if the user associated with the email address exists.
     * If the user exists, it calls the notification service to send an email
     * containing a link to confirm the registration. If the user does not exist,
     * it returns false. If there is an error sending the email, it throws a
     * RuntimeException.
     *
     * @param email the email address to send the confirmation email to
     * @return true if the email was sent successfully, false otherwise
     * @throws Exception if there is an error sending the email
     */
    public boolean SendRegisterConfirmationEmailMessage(String email) throws Exception {
        Optional<User> user = userService.getOptionalUserByEmail(email);
        if (user.isPresent()) {
            try {
                return notificationService.sendRegisterConfirmationEmailMessage(user.get());
            } catch (Exception e) {
                logger.info("Error sending email ", e);
                throw new RuntimeException("Error sending email", e);
            }
        }
        return false;
    }

    /**
     * Authenticates a user with the provided username and password.
     * <p>
     * This method uses the AuthenticationManager to attempt authentication with
     * the given credentials. If authentication is successful, it returns true.
     * If an exception is thrown during authentication, it returns false.
     *
     * @param username the username of the user attempting to authenticate
     * @param password the password of the user attempting to authenticate
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticateUser(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Updates the connection status of a user to reflect that they are currently
     * connected and records the current time as the last login timestamp.
     *
     * @param user the user whose connection status is updated
     */
    public void updateUserConnectionStatus(User user) {
//        user.setConnected(true);
//        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        userService.save(user);
    }

    /**
     * Generates a JWT token for the given user.
     * <p>
     * This method first wraps the given user in a
     * {@link CustomUserDetails} object, which is then used to generate a JWT
     *
     * @param user the user for whom the JWT token is generated
     * @return the generated JWT token
     */
    private String generateTokenForUser(User user) {
        UserDetails userDetails = new CustomUserDetails(user);
        return jwtService.generateToken(userDetails);
    }

    @Override
    /**
     * Retrieves the currently authenticated user from the security context.
     * If the user is not authenticated, returns a default user with the last name
     * set to the authentication name. Logs a message if the user is anonymous or
     * not found in the database.
     *
     * @return the current logged-in user or a default user if not authenticated.
     */
    public User getCurrentLoggedUser() {
        try {
            User entity = new User();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            entity.setLastName(authentication.getName());

            if (authentication instanceof AnonymousAuthenticationToken) {
                logger.info("Anonymous access detected {}", entity.getLastName());
                return entity;
            }

            Optional<User> userDetail = userService.getOptionalUserByUsername(entity.getLastName());
            if (!userDetail.isEmpty()) {
                entity = userDetail.get();
            } else {
                logger.info("User with username {} not found", entity.getLastName());
            }

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Long getLoggedUserId() {
        try {
            if (getCurrentLoggedUser() == null) {
                logger.error("Id not found");
                return null;
            }

            return getCurrentLoggedUser().getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Logout the current user from the application.
     * <p>
     * Updates the user's connection status, clears the security context and invalidates
     * the HTTP session if it exists. Finally, redirects to the login page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     */
    public boolean logoutUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Récupère l'utilisateur actuellement connecté
            User currentUser = getCurrentLoggedUser();

            // Met à jour l'état de connexion de l'utilisateur
            if (currentUser != null) {
                // currentUser.setConnected(false);
                // currentUser.setLastLogin(null);
                userService.save(currentUser);
                return true;
            }
        } catch (Exception e) {
            // Affiche l'erreur en cas d'exception
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
