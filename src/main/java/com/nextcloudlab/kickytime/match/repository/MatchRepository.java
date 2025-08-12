package com.nextcloudlab.kickytime.match.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextcloudlab.kickytime.match.entity.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findAllByOrderByMatchDateTimeDesc();

    //    Optional<Match> findById(Long id);
    //
    //    // 날짜가 지난 경기를 자동 종료처리 하기 위해 조회
    //    List<Match> findByStatusAndMatchDateTimeBefore(
    //            List<MatchStatus> statuses, LocalDateTime dateTime);
}
