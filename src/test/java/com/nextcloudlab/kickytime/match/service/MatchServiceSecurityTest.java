package com.nextcloudlab.kickytime.match.service;

import com.nextcloudlab.kickytime.match.dto.MatchCreateRequestDto;
import com.nextcloudlab.kickytime.match.entity.Match;
import com.nextcloudlab.kickytime.match.repository.MatchParticipantRepository;
import com.nextcloudlab.kickytime.match.repository.MatchRepository;
import com.nextcloudlab.kickytime.user.entity.RoleEnum;
import com.nextcloudlab.kickytime.user.entity.User;
import com.nextcloudlab.kickytime.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;

@SpringBootTest
@Import(MatchService.class) // 프록시가 적용된 실제 빈 주입
class MatchServiceSecurityTest {

    @TestConfiguration
    @EnableMethodSecurity // @PreAuthorize 활성화
    static class MethodSecurityTestConfig {}

    @Autowired
    MatchService matchService;

    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    MatchRepository matchRepository;
    @MockitoBean
    MatchParticipantRepository participantRepository;

    @Test
    @DisplayName("USER 권한으로 createMatch 호출 시 접근 거부")
    @WithMockUser(username = "user", roles = "USER")
    void createMatch_forbidden_whenUserRoleIsUSER() {
        // given
        var dto = new MatchCreateRequestDto(2L,
                LocalDateTime.now().plusDays(1), "서울", 10);

        // userRepository가 호출되지 않더라도 상관 없지만, 혹시를 위해 준비
        var creator = new User();
        creator.setId(2L);
        creator.setRole(RoleEnum.USER);
        given(userRepository.findById(2L)).willReturn(Optional.of(creator));

        // when & then
        assertThatThrownBy(() -> matchService.createMatch(dto))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("ADMIN 권한으로 createMatch 호출 시 통과")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void createMatch_allowed_whenUserRoleIsADMIN() {
        // given
        var dto = new MatchCreateRequestDto(1L,
                LocalDateTime.now().plusDays(1), "서울", 10);

        var admin = new User();
        admin.setId(1L);
        admin.setRole(RoleEnum.ADMIN);
        given(userRepository.findById(1L)).willReturn(Optional.of(admin));
        given(matchRepository.save(any(Match.class))).willReturn(new Match());

        // when (예외 없어야 통과)
        matchService.createMatch(dto);
    }
}
