package com.nextcloudlab.kickytime.match.dto;

import java.time.LocalDateTime;

public record MatchCreateRequestDto(
        Long createdBy, LocalDateTime matchDateTime, String location, Integer maxPlayers) {}
