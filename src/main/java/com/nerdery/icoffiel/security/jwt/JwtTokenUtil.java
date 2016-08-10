package com.nerdery.icoffiel.security.jwt;

import com.nerdery.icoffiel.config.AppProperties;
import com.nerdery.icoffiel.security.SecurityUtils;
import com.nerdery.icoffiel.web.rest.jwttoken.model.JwtToken;
import com.nerdery.icoffiel.web.rest.jwttoken.service.JwtTokenService;
import com.nerdery.icoffiel.web.rest.user.model.User;
import com.nerdery.icoffiel.web.rest.user.service.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility providing methods for handling a JWT.
 */
@Component
public class JwtTokenUtil {

    private static final String AUDIENCE_UNKNOWN = "unknown";
    private static final String AUDIENCE_WEB = "web";
    private static final String AUDIENCE_TABLET = "tablet";
    private static final String AUDIENCE_MOBILE = "mobile";

    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    private final String secret;
    private final Integer expiration;
    private final Integer refreshExpiration;

    @Autowired
    public JwtTokenUtil(AppProperties appProperties, JwtTokenService jwtTokenService, UserService userService) {
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;

        this.secret = appProperties.getSecurity().getAuthentication().getJwt().getSecret();
        this.expiration = appProperties.getSecurity().getAuthentication().getJwt().getExpiration();
        this.refreshExpiration = appProperties.getSecurity().getAuthentication().getJwt().getRefreshExpiration();
    }

    /**
     * Retrieve the username from the JWT
     * @param token The JWT.
     * @return The username if found, otherwise null.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.getSubject();
        } else {
            return null;
        }
    }

    /**
     * Retrieve the claims from the JSON Web Token.
     * @param token The JWT.
     * @return The Claims on the JWT
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            claims = null;
        }

        return claims;
    }

    /**
     * Validate the JWT.
     * @param authToken The JWT.
     * @param userDetails The user details of the
     * @return
     */
    public boolean validateToken(String authToken, UserDetails userDetails) {
        String username = getUsernameFromToken(authToken);

        return (
                username.equals(userDetails.getUsername())
                && !isTokenExpired(authToken)
                );
    }

    /**
     * Check if the JWT is expired.
     *
     * @param token The JWT
     * @return true if the token is expired, otherwise false.
     */
    private boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Retrieve the expiration date from the JWT.
     *
     * @param token The JWT.
     * @return The expiration date from the JWT.
     */
    private Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * Create a JWT using an authentication and setting the audience to be the current device that is requesting a token.
     *
     * @param authentication The authentication containing the security principal details.
     * @param device The current device that is requesting a JWT.
     * @return A JWT containing the username as the subject and the audience as the device requesting access.
     */
    public String createToken(Authentication authentication, Device device) {
        return createToken(authentication, device, generateExpirationDate());

    }

    /**
     * Create a Refresh JWT using an authentication and setting the audience to be the current device that is requesting a token.
     *
     * @param authentication The authentication containing the security principal details.
     * @param device The current device that is requesting a JWT.
     * @return A JWT containing the username as the subject and the audience as the device requesting access.
     */
    public JwtToken createAndPersistRefreshToken(Authentication authentication, Device device) {
        String token = createToken(authentication, device, generateRefreshExpirationDate());
        return persistToken(authentication, token);
    }

    private JwtToken persistToken(Authentication authentication, String token) {
        User user = userService.findOneByUsername(SecurityUtils.getCurrentUserLogin());

        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(token);
        jwtToken.setExpired(false);
        jwtToken.setUser(user);

        return jwtTokenService.createToken(jwtToken);
    }

    /**
     * Create a Refresh JWT using an authentication, audience, and the expiration date for the token.
     *
     * @param authentication The authentication containing the security principal details.
     * @param device The current device that is making the request.
     * @param date The expiration date of the token.
     * @return A JWT.
     */
    private String createToken(Authentication authentication, Device device, Date date) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setAudience(generateAudience(device))
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Create a JWT using an existing set of claims.
     *
     * @param claims The claims to be added to the JWT.
     * @return A JWT containing the username as the subject and the audience as the device requesting access.
     */
    private String createToken(Claims claims) {
        return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(generateExpirationDate())
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
    }

    /**
     * Get the expiration date of the JWT.
     *
     * @return A Date for the current expiration
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * Get the expiration date of the refresh JWT.
     *
     * @return A Date for the current expiration
     */
    private Date generateRefreshExpirationDate() {
        return new Date(System.currentTimeMillis() + refreshExpiration * 1000);
    }

    /**
     * Generate the audience for the JWT.
     *
     * @param device The current device that is making the request.
     * @return The audience for the JWT.
     */
    private String generateAudience(Device device) {
        String audience = AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = AUDIENCE_WEB;
        } else if (device.isTablet()) {
            audience = AUDIENCE_TABLET;
        } else if (device.isMobile()) {
            audience = AUDIENCE_MOBILE;
        }
        return audience;
    }

    /**
     * Check if the token can be refreshed.
     *
     * @param token The JWT to check.
     * @return true if the token can be refreshed, otherwise false.
     */
    public boolean canTokenBeRefreshed(String token) {
        return isValidRefreshToken(token) && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    /**
     * Check if the refresh token retrieved is valid.
     * @param token The token to fetch from the database.
     * @return true is the fetched token is valid, otherwise false.
     */
    private boolean isValidRefreshToken(String token) {
        JwtToken jwtToken = jwtTokenService.findOneByToken(token);
        return null != jwtToken
                && !jwtToken.getExpired();
    }

    /**
     * Check if the expiration timestamp on the token can be ignored.
     *
     * @param token The JWT to check.
     * @return true if the expiration date can be ignored, otherwise false.
     */
    private boolean ignoreTokenExpiration(String token) {
        String audience = getAudienceFromToken(token);
        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
    }

    /**
     * Retrieve the audience from a JWT.
     *
     * @param token The JWT to retrieve the audience from.
     * @return The audience.
     */
    private String getAudienceFromToken(String token) {
        try {
            return Jwts.parser().parseClaimsJws(token).getBody().getAudience();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Refresh the token.
     *
     * @param token The JWT to refresh.
     * @return The refreshed token, or null if there was an issue.
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            refreshedToken = createToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }
}
