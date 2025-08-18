package com.nextcloudlab.kickytime.config;

import com.nextcloudlab.kickytime.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.LinkedHashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class JwtAuthConverterConfig {

    private final UserRepository userRepository;

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter((Jwt jwt) -> {
            Set<GrantedAuthority> authorities =new LinkedHashSet<>();

            String cognitoSub = jwt.getClaimAsString("sub");
            if (cognitoSub == null || cognitoSub.isBlank()) {
                return authorities;
            }

            userRepository.findByCognitoSub(cognitoSub).ifPresent(user -> {
                var role = user.getRole();
                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
            });

            return authorities;
        });

        return converter;
    }
}
