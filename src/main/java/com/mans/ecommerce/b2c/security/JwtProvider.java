package com.mans.ecommerce.b2c.security;

import java.util.*;
import java.util.stream.Collectors;

import com.mans.ecommerce.b2c.utill.Global;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Utility Class for common Java Web Token operations
 * <p>
 * Created by Mary Ellen Bowman
 */
@Component
public class JwtProvider
{

    private final String ROLES_KEY = "roles";

    private final String ROLE = "user";

    private String secretKey;

    private int validityInMinutes;

    @Autowired
    public JwtProvider(
            @Value("${security.jwt.token.secret-key}") String secretKey,
            @Value("${security.jwt.token.expiration}") int validityInMinutes)
    {

        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.validityInMinutes = validityInMinutes;
    }

    /**
     * Create JWT string given username and roles.
     *
     * @param username
     * @return jwt string
     */
    public String createToken(String username)
    {
        //Add the username to the payload
        Claims claims = Jwts.claims().setSubject(username);
        //Convert roles to Spring Security SimpleGrantedAuthority objects,
        //Add to Simple Granted Authority objects to claims
        claims.put(ROLES_KEY, Arrays.asList(new SimpleGrantedAuthority(ROLE)));
        //Build the Token
        Date now = new Date();
        Date future = Global.getFuture(validityInMinutes);
        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setExpiration(future)
                   .signWith(SignatureAlgorithm.HS256, secretKey)
                   .compact();
    }

    /**
     * Validate the JWT String
     *
     * @param token JWT string
     * @return true if valid, false otherwise
     */
    public boolean isValidToken(String token)
    {
        try
        {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        }
        catch (JwtException | IllegalArgumentException e)
        {
            return false;
        }
    }

    /**
     * Get the username from the token string
     *
     * @param token jwt
     * @return username
     */
    public String getUsername(String token)
    {
        return Jwts.parser().setSigningKey(secretKey)
                   .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Get the roles from the token string
     *
     * @param token jwt
     * @return username
     */
    public List<GrantedAuthority> getRoles(String token)
    {
        List<Map<String, String>> roleClaims = Jwts.parser().setSigningKey(secretKey)
                                                   .parseClaimsJws(token).getBody().get(ROLES_KEY, List.class);
        return roleClaims.stream().map(roleClaim ->
                                               new SimpleGrantedAuthority(roleClaim.get("authority")))
                         .collect(Collectors.toList());
    }
}