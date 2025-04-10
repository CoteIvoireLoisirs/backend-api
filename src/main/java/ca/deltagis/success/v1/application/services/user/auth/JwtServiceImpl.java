package ca.deltagis.success.v1.application.services.user.auth;

import ca.deltagis.success.v1.domain.ports.in.user.auth.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements IJwtService {
    /**
     * The private key used for signing JWT tokens.
     */
    private final String PRIVATE_KEY = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    /**
     * 10 minutes
     */
    private final long TOKEN_DEFAULT_EXPIRATION_TIME = 1000L * 60 * (60 * 24); // TODO : remove or write correct implementation

    @Override
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the token to extract the username from
     * @return the username
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token the token to extract the expiration from
     * @return the expiration date
     */
    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token the token to extract claims from
     * @return the extracted claims
     */
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts a specific claim from the given JWT token.
     * <p>
     * This method uses the given function to extract the desired claim from the
     * claims of the given JWT token.
     *
     * @param token          the token to extract the claim from
     * @param claimsResolver a function that takes the {@link Claims} object and
     *                       returns the extracted claim.
     * @return the extracted claim
     */
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token from the given extra claims and user details.
     * <p>
     * This method creates a JWT token with the given extra claims, the username
     * of the user as the subject, the current time as the issued at time, the
     * sum of the current time and the default token expiration time as the expiration
     * time, and signs it with the default signing key using the HS256 algorithm.
     *
     * @param extraClaims a map of extra claims to include in the token
     * @param userDetails the user details to include in the token
     * @return the generated JWT token
     */
    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_DEFAULT_EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates a JWT token for the given user details.
     * <p>
     * This is a convenience method that calls {@link #generateToken(Map, UserDetails)}
     * with an empty claims map.
     *
     * @param userDetails the user details to include in the token
     * @return the generated JWT token
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token for the given username with a specified expiration period.
     *
     * @param username   the username to include in the token
     * @param expiration the number of days after which the token will expire
     * @return the generated JWT token
     */
    @Override
    public String generateTokenBy(String username, Integer expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * expiration))
                .signWith(SignatureAlgorithm.HS256, PRIVATE_KEY)
                .compact();
    }

    /**
     * Generates a new refresh token based on the provided refresh token.
     * <p>
     * This method creates a new JWT token with the given refresh token as the subject and
     * the same expiration date as the original token. The token is signed with the same
     * secret key as the original token.
     *
     * @param refreshToken the refresh token to be used as a basis for generating a new token
     * @return the newly generated refresh token
     */
    @Override
    public String generateRefreshToken(String refreshToken) {
        return Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(refreshToken)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_DEFAULT_EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Determines if the given token is valid for the given user details.
     * <p>
     * This implementation returns false, as it is not possible to determine the
     * validity of a JWT token without knowledge of the secret key used to sign
     * the token.
     *
     * @param token       the token to check
     * @param userDetails the user details to check against
     * @return false, as the validity of the token cannot be determined
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return isTokenExpired(token);
    }

    /**
     * Returns true if the given JWT token has expired, false otherwise.
     *
     * <p>This method extracts the expiration date from the given JWT token and
     * checks if it is before the current date.
     *
     * @param token the JWT token to check
     * @return true if the token has expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Returns the secret key used for signing JWT tokens.
     *
     * <p>This method is used internally by the library to sign JWT tokens.
     * It is not intended to be used directly by clients.
     *
     * @return the secret key used for signing JWT tokens
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(PRIVATE_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
