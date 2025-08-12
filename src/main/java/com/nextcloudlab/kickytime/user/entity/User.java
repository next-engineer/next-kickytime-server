package com.nextcloudlab.kickytime.user.entity;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String nickname;

    @NotBlank
    @Column(nullable = false, unique = true, name = "cognito_sub")
    private String cognitoSub;

    @NotBlank
    @Column(nullable = false, name = "email_verified")
    private boolean emailVerified;

    @Column(nullable = true)
    private String password;

    @NotBlank
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RankEnum rank;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.role == null) this.role = RoleEnum.USER;
        if (this.rank == null) this.rank = RankEnum.BEGINNER;
        if (this.imageUrl == null) this.imageUrl = "/images/default-profile.png";
        if (this.createdAt == null) this.createdAt = java.time.LocalDateTime.now();
    }
}
