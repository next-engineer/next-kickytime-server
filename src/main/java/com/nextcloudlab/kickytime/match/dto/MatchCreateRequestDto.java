package com.nextcloudlab.kickytime.match.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchCreateRequestDto {
    private Long createdBy;
    private LocalDateTime matchDateTime;
    private String location;
    private Integer maxPlayers;
}
