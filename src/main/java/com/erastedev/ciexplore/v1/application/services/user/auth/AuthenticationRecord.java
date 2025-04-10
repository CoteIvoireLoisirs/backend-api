package com.erastedev.ciexplore.v1.application.services.user.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticationRecord(@JsonProperty String token, @JsonProperty String type) {

}
