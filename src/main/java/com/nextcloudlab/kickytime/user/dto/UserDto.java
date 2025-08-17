package com.nextcloudlab.kickytime.user.dto;

import java.time.LocalDateTime;

import com.nextcloudlab.kickytime.user.entity.User;

public record UserDto(
        Long id,
        String email,
        Boolean email_verified,
        String nickname,
        String imageUrl,
        String role,
        String rank,
        LocalDateTime createdAt) {
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.isEmailVerified(),
                user.getNickname(),
                user.getImageUrl(),
                user.getRole().name(),
                user.getRank().name(),
                user.getCreatedAt());
    }
}
