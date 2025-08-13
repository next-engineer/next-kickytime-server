package com.nextcloudlab.kickytime.user.dto;

import com.nextcloudlab.kickytime.user.entity.User;

public record UserDto(
        Long id, String email, String nickname, String imageUrl, String role, String rank) {
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getImageUrl(),
                user.getRole().name(),
                user.getRank().name());
    }
}
