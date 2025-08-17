package com.nextcloudlab.kickytime.match.entity;

public enum MatchStatus {
    OPEN, // 모집 중 (정원 미달)
    FULL, // 모집 마감 (정원 충족)
    CLOSED, // 경기 종료
    CANCELLED // 경기 취소
}
