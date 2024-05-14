package com.recipe.config.security;


import com.recipe.service.AuthService;
import com.recipe.util.exceptions.AuthorizationException;

import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, @Nullable HttpServletResponse servletResponse,
                                    @NonNull FilterChain filterChain)  throws IOException, ServletException{


        String token = jwtTokenProvider.resolveToken(servletRequest);

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {

                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (AuthorizationException e) {
            SecurityContextHolder.clearContext();
            servletRequest.setAttribute("expired", e.getMessage());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }


}
