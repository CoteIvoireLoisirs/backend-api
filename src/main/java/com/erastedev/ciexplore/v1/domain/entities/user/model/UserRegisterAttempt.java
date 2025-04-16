package com.erastedev.ciexplore.v1.domain.entities.user.model;

import lombok.Getter;

@Getter
public class UserRegisterAttempt {
    private final User user;
    private UserRegisterState state;
    private final Boolean success;

    private UserRegisterAttempt(Builder builder) {
        this.user = builder.user;
        this.state = builder.state;
        this.success = builder.success;
    }

    public UserRegisterAttempt setSuccess(Boolean success) {
        return new UserRegisterAttempt.Builder()
                .user(this.user)
                .state(this.state)
                .success(success)
                .build();
    }

    public UserRegisterAttempt setUser(User user) {
        return new UserRegisterAttempt.Builder()
                .user(user)
                .state(this.state)
                .success(this.success)
                .build();
    }

    public UserRegisterAttempt setState(UserRegisterState state) {
        return new UserRegisterAttempt.Builder()
                .user(this.user)
                .state(state)
                .success(this.success)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private User user;
        private UserRegisterState state;
        private Boolean success;

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder state(UserRegisterState state) {
            this.state = state;
            return this;
        }

        public Builder success(Boolean success) {
            this.success = success;
            return this;
        }

        public UserRegisterAttempt build() {
            return new UserRegisterAttempt(this);
        }
    }
}
