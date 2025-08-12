package com.nextcloudlab.kickytime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(
                                                "/swagger-ui/**",
                                                "/v3/api-docs/**",
                                                "/actuator/health")
                                        .permitAll()
                                        .requestMatchers("/api/user/login", "/api/user/signup")
                                        .permitAll()
                                        .requestMatchers("/api/**")
                                        .authenticated()
                                        .anyRequest()
                                        .permitAll())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
