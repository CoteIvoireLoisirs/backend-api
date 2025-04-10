package com.erastedev.ciexplore.v1.domain.ports.in.user.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface IJwtService {

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the token to extract the username from
     * @return the username
     */
    String extractUsername(String token);

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token the token to extract the expiration from
     * @return the expiration date
     */
    Date extractExpiration(String token);

    /**
     * Extracts a specific claim from the given JWT token.
     *
     * @param <T>            the type of the claim to extract
     * @param token          the token to extract the claim from
     * @param claimsResolver a function that takes the {@link Claims} object and
     *                       returns the extracted claim.
     * @return the extracted claim
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Generates a JWT token from the given extra claims and user details.
     *
     * @param extraClaims a map of extra claims to include in the token
     * @param userDetails the user details to include in the token
     * @return the generated token
     */
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    /**
     * Generates a JWT token from the given user details.
     *
     * @param userDetails the user details to include in the token
     * @return the generated token
     */
    String generateToken(UserDetails userDetails);

    /**
     * Generates a JWT token for the given username with a specified expiration period.
     *
     * @param username   the username to include in the token
     * @param expiration the number of days after which the token will expire
     * @return the generated JWT token
     */
    String generateTokenBy(String username, Integer expiration);

    /**
     * Generates a new refresh token based on the provided refresh token.
     *
     * @param refreshToken the refresh token to be used as a basis for generating a new token
     * @return the newly generated refresh token
     */
    String generateRefreshToken(String refreshToken);

    /**
     * Determines if the given token is valid for the given user details.
     *
     * @param token       the token to check
     * @param userDetails the user details to check against
     * @return true if the token is valid for the given user, false otherwise
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Determines if the given token has expired.
     *
     * @param token the token to check
     * @return true if the token has expired, false otherwise
     */
    boolean isTokenExpired(String token);

    /**
     * Resolves the token by extracting it from the request headers.
     *
     * @param request the HTTP request from which to extract the token
     * @return the resolved token, or null if not found
     */
    String resolveToken(HttpServletRequest request);
}
