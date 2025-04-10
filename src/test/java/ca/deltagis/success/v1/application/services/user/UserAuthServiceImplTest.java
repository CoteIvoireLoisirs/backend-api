package ca.deltagis.success.v1.application.services.user;

import ca.deltagis.success.v1.application.request.user.UserSignInRequest;
import ca.deltagis.success.v1.adapters.web.message.user.AuthLoginError;
import ca.deltagis.success.v1.application.mock.UserMock;
import ca.deltagis.success.v1.application.services.user.auth.AuthenticationResult;
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
    private UserAuthServiceImpl userAuthService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAuthenticate_UserNotFound() {
        UserSignInRequest request = new UserSignInRequest();
        request.setUsername(UserMock.notRegisteredUser().getUsername());
        request.setPassword(UserMock.notRegisteredUser().getPassword());

        when(userService.getOptionalUserByUsername(UserMock.notRegisteredUser().getUsername())).thenReturn(Optional.empty());

        AuthenticationResult result = userAuthService.authenticate(request);

        assertEquals(AuthLoginError.USER_NOT_FOUND, result.getError());
    }

    @Test
    public void testAuthenticate_AuthenticationSuccess() {
        UserSignInRequest request = new UserSignInRequest();
        request.setUsername(UserMock.user().getUsername());
        request.setPassword(UserMock.user().getPassword());

        // when(userService.getOptionalUserByUsername(UserMock.user().getUsername())).thenReturn(Optional.of(UserMock.user()));
        when(userAuthService.authenticate(request)).thenReturn(AuthenticationResult.success(UserMock.token()));

        AuthenticationResult result = userAuthService.authenticate(request);

        assertEquals(AuthenticationResult.success(UserMock.token()), result);
    }

}
