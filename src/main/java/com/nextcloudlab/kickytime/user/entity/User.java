package com.nextcloudlab.kickytime.user.entity;

import com.nextcloudlab.kickytime.common.entity.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

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

    @Column(nullable = false, name = "email_verified")
    private boolean emailVerified;

    @Column(nullable = true)
    private String password;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RankEnum rank;

    @PrePersist
    public void prePersist() {
        if (this.role == null) this.role = RoleEnum.USER;
        if (this.rank == null) this.rank = RankEnum.BEGINNER;
        if (this.imageUrl == null) this.imageUrl = "/images/default-profile.png";
    }
}
