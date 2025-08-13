package com.nextcloudlab.kickytime.match.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.nextcloudlab.kickytime.common.entity.BaseEntity;
import com.nextcloudlab.kickytime.user.entity.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "matches")
public class Match extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus matchStatus;

    @Future(message = "경기 일시는 현재보다 미래여야 합니다.")
    @Column(nullable = false)
    private LocalDateTime matchDateTime;

    @NotBlank(message = "장소는 필수입니다.")
    @Size(max = 255, message = "장소는 255자를 초과할 수 없습니다.")
    @Column(nullable = false)
    private String location;

    @Min(value = 2, message = "최소 2명 이상이어야 합니다.")
    @Max(value = 22, message = "최대 22명까지 가능합니다.")
    @Column(nullable = false)
    private Integer maxPlayers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createdBy;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<MatchParticipant> participants = new ArrayList<>();

    // 정원이 찼는지 확인
    public boolean isFull() {
        return matchStatus == MatchStatus.FULL;
    }

    // 현재 참가자 수를 반환
    public Integer getCurrentParticipantCount() {
        return participants.size();
    }
}
