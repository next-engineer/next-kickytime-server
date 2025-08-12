package com.nextcloudlab.kickytime.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextcloudlab.kickytime.user.entity.User;
import com.nextcloudlab.kickytime.user.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public User getMyInfo(@AuthenticationPrincipal Jwt jwt) {
        String cognitoSub = jwt.getClaimAsString("sub");
        String email = jwt.getClaimAsString("email");
        String nickname = jwt.getClaimAsString("nickname");

        return userService.findOrCreateUser(cognitoSub, email, nickname);
    }
}
