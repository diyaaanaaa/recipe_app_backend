package com.recipe.config.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        final String expired = request.getAttribute("expired") != null ? request.getAttribute("expired").toString() : "";
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Objects.requireNonNullElse(expired, "Unauthorized"));
    }
}

