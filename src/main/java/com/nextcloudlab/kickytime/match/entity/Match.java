package com.nextcloudlab.kickytime.match.entity;

import java.time.LocalDateTime;

import com.nextcloudlab.kickytime.user.entity.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus matchStatus;

    @Column(nullable = false)
    private LocalDateTime matchDateTime;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer maxPlayers;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createdBy;

    //    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    //    private List<MatchParticipant> participants = new ArrayList<>();
    //
    //    // 현재 참가자 수를 계산하는 메서드
    //    public long getCurrentParticipantCount() {
    //        return participants.stream()
    //                .filter(p -> p.getStatus() == ParticipantStatus.REGISTERED)
    //                .count();
    //    }
    //
    //    // 정원이 찼는지 확인하는 메서드
    //    public boolean isFull() {
    //        return getCurrentParticipantCount() >= maxPlayers;
    //    }
}
