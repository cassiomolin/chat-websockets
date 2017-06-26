package com.cassiomolin.example.chat.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.enterprise.context.ApplicationScoped;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * In-memory authenticator.
 *
 * @author cassiomolin
 */
@ApplicationScoped
public class Authenticator {

    private static Map<String, String> users = new HashMap<>();

    private static final String JWT_SIGNING_KEY = "secret";

    static {
        users.put("joe", "secret");
        users.put("jane", "secret");
        users.put("john", "secret");
    }

    public boolean checkCredentials(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public String issuesAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(30).toInstant()))
                .signWith(SignatureAlgorithm.HS512, JWT_SIGNING_KEY)
                .compact();
    }

    public String validateAccessToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(JWT_SIGNING_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new InvalidAccessTokenException(e.getMessage(), e);
        }
    }
}
