package com.myblog8.security;

import com.myblog8.config.CustomUserDetailsService;
import com.myblog8.exception.BlogAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // get JWT (token) from http request
        String token = getJWTfromRequest(request);

        // Token Validation and User Authentication:
        try {
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                // get username from token
                String username = tokenProvider.getUsernameFromJWT(token);
                // load user associated with token
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                        (userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // If the token is valid, it sets the authentication token in the Spring Security context.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (BlogAPIException e) {
            throw new RuntimeException(e);
        }
        filterChain.doFilter(request, response);
    }

    // Bearer <accessToken>
    // this method is a utility function used to extract the JWT from the Authorization header in an HTTP request,
    // following the "Bearer" token convention. The extracted JWT can then be used for further validation and authentication processes.
    private String getJWTfromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}