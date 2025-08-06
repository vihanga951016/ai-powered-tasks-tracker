package com.flex.task_tracker.security.utils;

import com.flex.task_tracker.app.entities.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static com.flex.task_tracker.common.constants.SecurityConstants.*;

@Slf4j
@Component
public class JwtUtil {

    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    // 🛠️ Generate token with username and userId
    public String generateToken(User user) {
        log.info("token generating to: " + user);
        return Jwts.builder()
                .setSubject(user.getEmail()) // 👤 Set subject (username)
                .claim("userId", user.getId()) // 🆔 Custom claim
                .claim("role", "ROLE_" + user.getRole()) // 🆔 Custom claim
                .setIssuedAt(new Date()) // 🕒 Now
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // ⏳ 10 hours
                .signWith(key) // 🔐 Sign with key
                .compact(); // 📦 Build token
    }

    public static Claims extractTokenBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //currently username equals to subject of the token
    public static String extractUsername(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            log.error("no auth header found in " + request.getRequestURI());
            return null;
        }

        if (!authHeader.startsWith("Bearer ")) {
            log.error("no bearer with JWT in " + request.getRequestURI());
            return null;
        }

        String token = authHeader.substring(7);

        //todo: check the token is expired or not

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

//    public static boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()));
//    }
}
