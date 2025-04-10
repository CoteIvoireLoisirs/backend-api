package com.erastedev.ciexplore.v1.adapters.web.http.user;

import com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse;
import com.erastedev.ciexplore.v1.adapters.web.api.service.ApiResponseService;
import com.erastedev.ciexplore.v1.application.request.user.UserSignInRequest;
import com.erastedev.ciexplore.v1.application.services.logs.LogServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserAuthServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserProfileServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.auth.AuthenticationResponse;
import com.erastedev.ciexplore.v1.application.services.user.auth.AuthenticationResult;
import com.erastedev.ciexplore.v1.domain.entities.user.User;
import com.erastedev.ciexplore.v1.domain.entities.user.UserProfile;
import com.erastedev.ciexplore.v1.domain.ports.in.user.IUserService;
import com.erastedev.ciexplore.v1.domain.ports.in.user.auth.IUserAuthService;
import com.erastedev.ciexplore.v1.infrastructure.utils.HttpRequestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAuthControllerTests {
    @Mock
    private UserAuthServiceImpl userAuthService;

    @Mock
    private IUserService userService;

    @Mock
    private IUserAuthService userAuthServiceMock;

    @Mock
    private UserProfileServiceImpl userProfileService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpRequestUtil httpRequestUtil;

    @Mock
    private LogServiceImpl auditService;

    @Mock
    private ApiResponseService response;

    private final Logger logger =  LoggerFactory.getLogger(UserAuthControllerTests.class);

    @InjectMocks
    @Autowired
    private UserAuthController userAuthController;

    @Before
    public void setup() {
        userAuthController = new UserAuthController(authenticationManager);
        userAuthController.userAuthService = userAuthServiceMock;
        userAuthController.userService = userService;
        userAuthController.userProfileService = userProfileService;
        userAuthController.httpRequestUtil = httpRequestUtil;
        userAuthController.auditService = auditService;
        userAuthController.response = response;
        when(userAuthService.authenticate(any())).thenReturn(new AuthenticationResult("", null));
        when(userService.getUserByUsername(any())).thenReturn(new User());
        when(userProfileService.getAllByWorkspaceCodeAndUserId(any(), any())).thenReturn(List.of(new UserProfile()));
    }

    @Test
    public void testAutowiredDependencies() {
        assertNotNull(userAuthController.userService);
        assertNotNull(userAuthController.userAuthService);
        assertNotNull(userAuthController.userProfileService);
        assertNotNull(userAuthController.authenticationManager);
        assertNotNull(userAuthController.httpRequestUtil);
        assertNotNull(userAuthController.auditService);
        assertNotNull(userAuthController.response);
    }

    @Test
    public void testLoginSuccess() {
        UserSignInRequest userRequest = new UserSignInRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("mypassword");

        ResponseEntity<ApiResponse<AuthenticationResponse>> response = userAuthController.login(userRequest);
        logger.info("testLoginSuccess>reponse: {}", response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userAuthService).authenticate(userRequest);
        verify(userService).getUserByUsername(userRequest.getUsername());
        verify(userProfileService).getAllByWorkspaceCodeAndUserId(userRequest.getWorkspaceCode(), any());
    }

    @Test
    public void testLoginFailure() {
        UserSignInRequest userRequest = new UserSignInRequest();
        userRequest.setUsername("testuser");
        userRequest.setPassword("password");

        when(userAuthService.authenticate(any())).thenReturn(new AuthenticationResult("", null));

        ResponseEntity<ApiResponse<AuthenticationResponse>> response = userAuthController.login(userRequest);
        logger.info("testLoginFailure>reponse: {}", response);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userAuthService).authenticate(userRequest);
    }

}