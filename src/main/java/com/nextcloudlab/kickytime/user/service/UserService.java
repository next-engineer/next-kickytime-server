package com.nextcloudlab.kickytime.user.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nextcloudlab.kickytime.user.dto.UserDto;
import com.nextcloudlab.kickytime.user.entity.User;
import com.nextcloudlab.kickytime.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User findOrCreateUser(String cognitoSub, String email, String nickname) {
        Optional<User> existingUser = userRepository.findByCognitoSub(cognitoSub);

        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            User newUser = new User();
            newUser.setCognitoSub(cognitoSub);
            newUser.setEmail(email);
            newUser.setNickname(nickname);
            return userRepository.save(newUser);
        }
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
