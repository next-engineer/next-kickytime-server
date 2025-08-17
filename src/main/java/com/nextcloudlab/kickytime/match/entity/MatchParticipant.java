package com.nextcloudlab.kickytime.match.entity;

import com.nextcloudlab.kickytime.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "match_participants",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_match_user",
                    columnNames = {"match_id", "user_id"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private java.time.LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = java.time.LocalDateTime.now();
    }
}
