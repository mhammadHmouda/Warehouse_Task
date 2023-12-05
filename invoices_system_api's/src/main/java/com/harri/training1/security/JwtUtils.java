package com.harri.training1.security;

import com.harri.training1.models.entities.User;
import com.harri.training1.repositories.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${harri.app.jwtSecret}")
    private String jwtSecret;

    @Value("${harri.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    private UserRepository repository;

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Generate jwt token from user information
     */
    public String generateTokenFromUserDetails(UserDetailsImpl userDetails) {
        LOGGER.debug("generateTokenFromUserDetails :: Generating token from user details: " + userDetails);
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("id", userDetails.getId().intValue());
        claims.put("email", userDetails.getEmail());
        claims.put("role", userDetails.getRole());

        byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecret);
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "HmacSHA256");

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract username from jwt token subject based on the jwt secret
     */
    public String getUserNameFromJwtToken(String token) {
        LOGGER.debug("getUserNameFromJwtToken :: Getting username from token..");
        return Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtSecret)).build().parseClaimsJws(token.split("\\|")[0]).getBody().getSubject();
    }

    /**
     * Extract userId from jwt token claims based on the jwt secret
     */
    public Long getIdFromJwtToken(String token) {
        LOGGER.debug("getIdFromJwtToken :: Getting id from token");
        return Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtSecret)).build().parseClaimsJws(token.split("\\|")[0]).getBody().get("id", Long.class);
    }
    /**
     * Extract userRole from jwt token claims based on the jwt secret
     */
    public String getRoleFromJwtToken(String token) {
        LOGGER.debug("getRoleFromJwtToken :: Getting role from token");
        return Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtSecret)).build().parseClaimsJws(token.split("\\|")[0]).getBody().get("role", String.class);
    }

    /**
     * Get logged-in user from security auth context
     */
    public static User getUserFromAuth(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UserDetailsImpl.build(userDetails);
    }

    /**
     * Check the validity jwt token and store log based on the exception throws
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtSecret)).build().parseClaimsJws(authToken);
            LOGGER.info("validateJwtToken :: Token validated");
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Load the user from repository and convert to UserDetails data
     */
    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);

        if (user == null)
            return null;


        return UserDetailsImpl.build(user);
    }
}
