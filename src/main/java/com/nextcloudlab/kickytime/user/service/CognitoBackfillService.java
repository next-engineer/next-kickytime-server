package com.nextcloudlab.kickytime.user.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nextcloudlab.kickytime.user.entity.User;
import com.nextcloudlab.kickytime.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CognitoBackfillService {
    private final CognitoIdentityProviderClient cognito;
    private final UserRepository userRepository;

    @Value("${app.cognito.user-pool-id}")
    private String userPoolId;

    public BackfillReport backfillAllUsers() {
        return backfillReport();
    }

    public BackfillReport backfillReport() {
        int processed = 0, created = 0, updated = 0;

        String token = null;
        do {
            ListUsersRequest request =
                    ListUsersRequest.builder()
                            .userPoolId(userPoolId)
                            .limit(60)
                            .paginationToken(token)
                            .build();

            ListUsersResponse response = cognito.listUsers(request);

            for (UserType u : response.users()) {
                processed++;
                BackfillResult r = upsertOne(u);
                switch (r) {
                    case CREATED -> created++;
                    case UPDATED -> updated++;
                    case UNCHANGED -> {}
                }
            }
            token = response.paginationToken();
        } while (token != null);

        return new BackfillReport(processed, created, updated);
    }

    @Transactional
    BackfillResult upsertOne(UserType u) {
        String cognitoSub = attr(u, "sub");
        String email = attr(u, "email");
        String nickname = attr(u, "nickname");
        boolean emailVerified = "true".equalsIgnoreCase(attr(u, "email_verified"));

        if (cognitoSub == null || email == null) {
            log.warn(
                    "Skip user with missing sub/email. username={}, status={}",
                    u.username(),
                    u.userStatusAsString());
            return BackfillResult.UNCHANGED;
        }

        Optional<User> existOpt = userRepository.findByCognitoSub(cognitoSub);
        if (existOpt.isEmpty()) {
            User newUser = new User();
            newUser.setCognitoSub(cognitoSub);
            newUser.setEmail(email);
            newUser.setNickname(nickname);
            newUser.setEmailVerified(emailVerified);
            userRepository.save(newUser);
            return BackfillResult.CREATED;
        } else {
            User ex = existOpt.get();
            boolean dirty = false;

            if (email != null && !email.equals(ex.getEmail())) {
                ex.setEmail(email);
                dirty = true;
            }
            if (nickname != null && !nickname.equals(ex.getNickname())) {
                ex.setNickname(nickname);
                dirty = true;
            }
            if (emailVerified && !ex.isEmailVerified()) {
                ex.setEmailVerified(true);
                dirty = true;
            }

            if (dirty) {
                userRepository.save(ex);
                return BackfillResult.UPDATED;
            }
            return BackfillResult.UNCHANGED;
        }
    }

    private String attr(UserType u, String key) {
        return u.attributes().stream()
                .filter(a -> key.equals(a.name()))
                .map(AttributeType::value)
                .findFirst()
                .orElse(null);
    }

    public enum BackfillResult {
        CREATED,
        UPDATED,
        UNCHANGED
    }

    public record BackfillReport(int processed, int created, int updated) {}
}
