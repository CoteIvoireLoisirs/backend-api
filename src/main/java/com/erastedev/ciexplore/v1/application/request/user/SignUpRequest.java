/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-24 16:03:56
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:45
 * @FilePath: src/main/java/ca/deltagis/success/v1/adapters/web/http/user/request/UserSignUpRequest.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package com.erastedev.ciexplore.v1.application.request.user;

import com.erastedev.ciexplore.v1.domain.entities.user.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String phoneNumber;

    /**
     * Constructs a new {@link User} instance using the fields in this {@link SignUpRequest}.
     *
     * @return a {@link User} object with the email, username, password, first name, last name, and phone number set
     */
    public User buildUser() {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setAutoFields();
        return user;
    }
}