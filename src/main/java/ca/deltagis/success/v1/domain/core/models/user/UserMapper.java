/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-30 10:07:46
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:46
 * @FilePath: src/main/java/ca/deltagis/success/v1/domain/core/models/user/UserMapper.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package ca.deltagis.success.v1.domain.core.models.user;

import ca.deltagis.success.v1.application.request.user.UserSignUpRequest;
import ca.deltagis.success.v1.domain.core.entities.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nullable;

public class UserMapper {
    /**
     * Maps a {@link UserSignUpRequest} to a {@link User} model.
     *
     * <p>If {@code searchUser} is not null, the method will copy the UUID and ID from the given user to the one being created.
     * If {@code passwordEncoder} is not null, the method will encrypt the password given in the request.
     *
     * @param param           the user sign up request
     * @param searchUser      the user to be updated, or null if creating a new user
     * @param passwordEncoder the password encoder to use, or null if no password should be set
     * @return the mapped user
     */
    public static User mapUserSignUpRequestToUser(UserSignUpRequest param, @Nullable PasswordEncoder passwordEncoder, @Nullable User searchUser) {
        User user = new User();
        user.setUsername(param.getUsername());
        user.setEmail(param.getEmail());
        user.setFirstName(param.getFirstName());
        user.setLastName(param.getLastName());
        user.setInvitationStatus(UserInvitationState.ACCEPTED.toString());
        user.setEmployer(param.getEmployer());
        user.setPhoneNumber(param.getPhoneNumber());

        if (searchUser != null) {
            user.setUuid(searchUser.getUuid());
            user.setId(searchUser.getId());
        }

        if (param.getPassword() != null && passwordEncoder != null) {
            user.setPassword(passwordEncoder.encode(param.getPassword()));
        }

        return user;
    }
}
