package com.nextcloudlab.kickytime.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class TokenUseValidator implements OAuth2TokenValidator<Jwt> {
    private final String expected;

    public TokenUseValidator(String expected) {
        this.expected = expected;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        String tokenUse = token.getClaimAsString("token_use");
        if (expected.equals(tokenUse)) {
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error error =
                new OAuth2Error(
                        "invalid_token",
                        "Invalid token_use: expected '" + expected + "', got '" + tokenUse + "'",
                        null);
        return OAuth2TokenValidatorResult.failure(error);
    }
}
