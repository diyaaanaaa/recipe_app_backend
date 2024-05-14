package com.recipe.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.http.HttpMethod.*;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtTokenFilter jwtTokenFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introSpector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introSpector);

        return http
                .exceptionHandling((ex) -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(mvcMatcherBuilder.pattern(OPTIONS, "/**")).permitAll()

                        .requestMatchers(mvcMatcherBuilder.pattern(GET, "/actuator/**")).hasRole("ROOT")

                        .requestMatchers(mvcMatcherBuilder.pattern(POST, "/auth/login"),
                                mvcMatcherBuilder.pattern(POST, "/account/registration")).permitAll()

                        .requestMatchers(mvcMatcherBuilder.pattern(PATCH, "/account/verify"),
                                mvcMatcherBuilder.pattern(PATCH, "/account/resend-verification-code"),
                                mvcMatcherBuilder.pattern(PATCH, "/account/forgot-password"),
                                mvcMatcherBuilder.pattern(PATCH, "/account/validate-recovery-code"),
                                mvcMatcherBuilder.pattern(PATCH, "/account/update-password")).permitAll()

                        .requestMatchers(mvcMatcherBuilder.pattern(GET, "/swagger-ui/**"),
                                mvcMatcherBuilder.pattern(GET, "/swagger-ui.html"),
                                mvcMatcherBuilder.pattern(GET, "/webjars/**"),
                                mvcMatcherBuilder.pattern(GET, "/swagger-resources/**"),
                                mvcMatcherBuilder.pattern(GET, "/v3/api-docs/**"),
                                mvcMatcherBuilder.pattern(GET, "/hc/status"),
                                mvcMatcherBuilder.pattern(GET, "/images/**"),
                                mvcMatcherBuilder.pattern(GET, "/account")).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
