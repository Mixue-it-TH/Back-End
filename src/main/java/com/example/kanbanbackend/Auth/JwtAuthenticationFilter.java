package com.example.kanbanbackend.Auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = httpRequest.getHeader("Authorization");

        if (httpRequest.getRequestURI().startsWith("/v2/")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove "Bearer " prefix
                String username = jwtTokenUtil.getUsernameFromToken(token);

                if (username != null && jwtTokenUtil.isTokenValid(token, username)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (userDetails != null) {
                        SecurityContextHolder.getContext().setAuthentication(
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
                        );
                    }
                } else {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    return;
                }
            } else {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException e) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        } catch (Exception e) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Internal Server Error");
            return;
        }

        chain.doFilter(request, response);
    }


}
