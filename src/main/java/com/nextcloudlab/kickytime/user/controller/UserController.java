package com.nextcloudlab.kickytime.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.nextcloudlab.kickytime.user.dto.UserDto;
import com.nextcloudlab.kickytime.user.entity.User;
import com.nextcloudlab.kickytime.user.service.UserService;
import com.nextcloudlab.kickytime.util.CognitoUserInfoClient;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CognitoUserInfoClient userInfoClient;

    @PostMapping("/signin-up")
    public User signInUp(@AuthenticationPrincipal Jwt accessToken) {
        String cognitoSub = accessToken.getClaimAsString("sub");
        var info = userInfoClient.fetch(accessToken.getTokenValue());

        boolean emailVerified = Boolean.TRUE.equals(info.emailVerified());

        return userService.findOrCreateUser(
                cognitoSub, info.email(), info.nickname(), emailVerified);
    }

    @GetMapping("/me")
    public UserDto myProfile(@AuthenticationPrincipal Jwt jwt) {
        String cognitoSub = jwt.getClaimAsString("sub");
        return userService.getByCognitoSub(cognitoSub);
    }
}