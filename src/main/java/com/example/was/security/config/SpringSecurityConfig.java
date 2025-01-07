package com.example.was.security.config;

import com.example.was.security.filter.JwtAuthenticatorFilter;
import com.example.was.security.handler.JwtAuthenticationEntryPoint;
import com.example.was.constants.ApiConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig {
    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticatorFilter jwtAuthenticatorFilter;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((authorize) -> authorize.requestMatchers(ApiConstant.WHITE_LIST_URL)
                                                           .permitAll()
                                                           .anyRequest()
                                                           .hasAnyRole("USER")
            )
            .exceptionHandling(handler -> {
                handler.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                handler.accessDeniedHandler(accessDeniedHandler);
            });

        http.addFilterBefore(jwtAuthenticatorFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}