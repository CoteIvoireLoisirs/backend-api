package com.erastedev.ciexplore.v1.application.services.user;

import com.erastedev.ciexplore.v1.application.request.user.SignInRequest;
import com.erastedev.ciexplore.v1.adapters.web.message.user.AuthLoginError;
import com.erastedev.ciexplore.v1.application.mock.UserMock;
import com.erastedev.ciexplore.v1.application.services.auth.AuthenticationServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.auth.AuthenticationResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAuthServiceImplTest {
    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private AuthenticationServiceImpl userAuthService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAuthenticate_UserNotFound() {
        SignInRequest request = new SignInRequest();
        request.setUsername(UserMock.notRegisteredUser().getUsername());
        request.setPassword(UserMock.notRegisteredUser().getPassword());

        when(userService.getOptionalUserByUsername(UserMock.notRegisteredUser().getUsername())).thenReturn(Optional.empty());

        AuthenticationResult result = userAuthService.authenticate(request);

        assertEquals(AuthLoginError.USER_NOT_FOUND, result.getError());
    }

    @Test
    public void testAuthenticate_AuthenticationSuccess() {
        SignInRequest request = new SignInRequest();
        request.setUsername(UserMock.user().getUsername());
        request.setPassword(UserMock.user().getPassword());

        // when(userService.getOptionalUserByUsername(UserMock.user().getUsername())).thenReturn(Optional.of(UserMock.user()));
        when(userAuthService.authenticate(request)).thenReturn(AuthenticationResult.success(UserMock.token()));

        AuthenticationResult result = userAuthService.authenticate(request);

        assertEquals(AuthenticationResult.success(UserMock.token()), result);
    }

}
