package com.nextcloudlab.kickytime.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextcloudlab.kickytime.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCognitoSub(String cognitoSub);

    boolean existsByCognitoSub(String cognitoSub);
}
