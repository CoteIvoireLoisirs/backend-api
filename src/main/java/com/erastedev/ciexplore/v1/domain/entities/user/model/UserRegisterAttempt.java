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

    /**
     * Return a UserRegisterAttempt with a successful registration. The user will be non-null and the state will be
     * null. The success field will be true.
     *
     * @param user the user to return
     * @return UserRegisterAttempt
     */
    public static UserRegisterAttempt withSuccess(User user) {
        return new UserRegisterAttempt.Builder()
                .user(user)
                .state(null)
                .success(true)
                .build();
    }

    /**
     * Return a UserRegisterAttempt with a specific error state. The user will be included in the attempt
     * and the success field will be false.
     *
     * @param user  the user associated with the error
     * @param state the specific error state to set
     * @return UserRegisterAttempt
     */
    public static UserRegisterAttempt withError(User user, UserRegisterState state) {
        return new UserRegisterAttempt.Builder()
                .user(user)
                .state(state)
                .success(false)
                .build();
    }

    /**
     * Return a UserRegisterAttempt with a generic error. The user will be null and the state will be
     * UserRegisterState.SOMETHING_WENT_WRONG.
     *
     * @return UserRegisterAttempt
     */
    public static UserRegisterAttempt somethingWentWrong() {
        return new UserRegisterAttempt.Builder()
                .user(null)
                .state(UserRegisterState.SOMETHING_WENT_WRONG)
                .success(false)
                .build();
    }

    /**
     * Return a UserRegisterAttempt with a specific error. The user will be null and the state will be
     * UserRegisterState.USER_NOT_FOUND.
     *
     * @return UserRegisterAttempt
     */
    public static UserRegisterAttempt userNotFound() {
        return new UserRegisterAttempt.Builder()
                .user(null)
                .state(UserRegisterState.USER_NOT_FOUND)
                .success(false)
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
