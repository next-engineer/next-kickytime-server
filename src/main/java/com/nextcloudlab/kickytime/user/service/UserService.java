package com.nextcloudlab.kickytime.user.service;

import com.nextcloudlab.kickytime.user.entity.User;
import com.nextcloudlab.kickytime.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOrCreateUser(String cognitoSub, String email, String nickname) {
        Optional<User> existingUser = userRepository.findByCognitoSub(cognitoSub);

        if(existingUser.isPresent()) {
            return existingUser.get();
        } else {
            User newUser = new User();
            newUser.setCognitoSub(cognitoSub);
            newUser.setEmail(email);
            newUser.setNickname(nickname);
            return userRepository.save(newUser);
        }
    }
}
