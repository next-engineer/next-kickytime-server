package com.kickytime.kickytime.api.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true, name = "cognito_sub")
    private String cognitoSub;

    @Column(nullable = false, name = "email_verified")
    private boolean emailVerified;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RankEnum rank;

    @Column(name = "created_at")
    @CreatedDate
    private java.time.LocalDateTime createdAt;
}