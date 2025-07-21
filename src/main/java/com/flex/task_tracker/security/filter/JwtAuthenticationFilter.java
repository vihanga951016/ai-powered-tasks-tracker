package com.flex.task_tracker.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flex.task_tracker.app.repositories.user.ExpiredTokenRepository;
import com.flex.task_tracker.common.http.ReturnResponse;
import com.flex.task_tracker.security.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Slf4j
@Component
@SuppressWarnings("Duplicates")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final ExpiredTokenRepository expiredTokenRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(UserDetailsService userDetailsService,
                                   ExpiredTokenRepository expiredTokenRepository) {
        this.userDetailsService = userDetailsService;
        this.expiredTokenRepository = expiredTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization"); // 👈 Get the Authorization header from the HTTP request.

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 🛑  If there's no token or it doesn't start with "Bearer ", skip and let the request continue (unauthenticated).
            filterChain.doFilter(request, response);

            ReturnResponse.FORBIDDEN(objectMapper, response);

            if (authHeader == null) log.error("auth header is null");

            if (authHeader != null && !authHeader.startsWith("Bearer ")) log.error("no 'Bearer' string");

            return;
        }

        String token = authHeader.substring(7); // 👈 Remove "Bearer " prefix.

        if (expiredTokenRepository.existsById(token)) {

            ReturnResponse.FORBIDDEN(objectMapper, response);

            log.error("token is expired");
            return;
        }

        String username = null;
        String role = null;

        try {

            try {

                Claims claims = JwtUtil.extractTokenBody(token);

                username = claims.getSubject(); // 👈 Extract the email (subject) from the token payload.
                role = claims.get("role", String.class);

                Set<String> allowedRoles = Set.of("ROLE_SUPER_ADMIN", "ROLE_USER");

                if (!allowedRoles.contains(role)) {

                    ReturnResponse.FORBIDDEN(objectMapper, response);

                    log.error("invalid role: " + role);
                    return; // ❗Stop further processing
                }

            } catch (Exception e) {
                e.printStackTrace(); // Log the real issue
                System.out.println("JWT parsing failed: " + e.getMessage());
            }

        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🛑 Extract the username (subject) from the token payload.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 👇 Load user details (from DB or memory) using Spring’s UserDetailsService.
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

            // 👇 Create an Authentication object for Spring Security.
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            Collections.singleton(authority)
                    );

            // 👇 Create an Authentication object for Spring Security.
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
