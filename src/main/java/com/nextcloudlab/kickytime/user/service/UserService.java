package com.nextcloudlab.kickytime.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nextcloudlab.kickytime.user.dto.UserDto;
import com.nextcloudlab.kickytime.user.entity.User;
import com.nextcloudlab.kickytime.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User findOrCreateUser(
            String cognitoSub, String email, String nickname, boolean emailVerified) {
        return userRepository
                .findByCognitoSub(cognitoSub)
                .map(
                        user -> {
                            // 변경 감지만 기대, save() 호출 X
                            if (email != null && !email.equals(user.getEmail()))
                                user.setEmail(email);
                            if (nickname != null && !nickname.equals(user.getNickname()))
                                user.setNickname(nickname);
                            if (emailVerified && !user.isEmailVerified())
                                user.setEmailVerified(true);
                            return user; // ← 기존 엔티티 그대로 반환
                        })
                .orElseGet(
                        () -> {
                            // 신규 생성일 때만 save()
                            User newUser = new User();
                            newUser.setCognitoSub(cognitoSub);
                            newUser.setEmail(email);
                            newUser.setNickname(nickname);
                            newUser.setEmailVerified(emailVerified);
                            return userRepository.save(newUser);
                        });
    }

    @Transactional
    public UserDto getByCognitoSub(String cognitoSub) {
        User me =
                userRepository
                        .findByCognitoSub(cognitoSub)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "User not found"));
        return UserDto.from(me);
    }
}
