package com.nextcloudlab.kickytime.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.nextcloudlab.kickytime.user.dto.UserDto;
import com.nextcloudlab.kickytime.user.entity.RankEnum;
import com.nextcloudlab.kickytime.user.entity.RoleEnum;
import com.nextcloudlab.kickytime.user.entity.User;
import com.nextcloudlab.kickytime.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;

    @InjectMocks private UserService userService;

    private User me;

    @BeforeEach
    void setUp() {
        me = new User();
        me.setId(1L);
        me.setEmail("me@example.com");
        me.setNickname("me");
        me.setCognitoSub("sub-123");
        me.setRole(RoleEnum.USER);
        me.setRank(RankEnum.BEGINNER);
        me.setImageUrl("/images/default-profile.png");
    }

    @Test
    @DisplayName("내 프로필 조회 - 성공")
    void getMyProfileBySubSuccess() {
        // given
        String cognitoSub = "sub-123";
        given(userRepository.findByCognitoSub(cognitoSub)).willReturn(Optional.of(me));

        // when
        UserDto res = userService.getByCognitoSub(cognitoSub);

        // then
        assertThat(res).isNotNull();
        assertThat(res.id()).isEqualTo(1L);
        assertThat(res.email()).isEqualTo("me@example.com");
        assertThat(res.nickname()).isEqualTo("me");
        verify(userRepository).findByCognitoSub(cognitoSub);
    }

    @Test
    @DisplayName("내 프로필 조회 - 사용자 없음이면 404")
    void getMyProfileBySubNotFound() {
        // given
        String missingSub = "missing";
        given(userRepository.findByCognitoSub(missingSub)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getByCognitoSub(missingSub))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("User not found");
        verify(userRepository).findByCognitoSub(missingSub);
    }

    @Test
    @DisplayName("회원가입 - 기존 사용자가 없으면 새로 생성한다")
    void signUpCreateWhenUserNotExists() {
        // given
        String sub = "sub-new";
        String email = "new@example.com";
        String nickname = "newbie";

        // 기존 사용자 없음
        given(userRepository.findByCognitoSub(sub)).willReturn(Optional.empty());

        // 저장될 엔터티 모킹
        User saved = new User();
        saved.setId(100L);
        saved.setCognitoSub(sub);
        saved.setEmail(email);
        saved.setNickname(nickname);
        saved.setRole(RoleEnum.USER);
        saved.setRank(RankEnum.BEGINNER);
        saved.setImageUrl("/images/default-profile.png");

        given(userRepository.save(any(User.class))).willReturn(saved);

        // when
        User result = userService.findOrCreateUser(sub, email, nickname);

        // then
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getNickname()).isEqualTo(nickname);
        verify(userRepository).findByCognitoSub(sub);
        verify(userRepository).save(any(User.class)); // 새로 생성되었는지 확인
    }

    @Test
    @DisplayName("로그인 - 기존 사용자가 있으면 그대로 반환한다 (save 호출 없음)")
    void loginReturnExistingWhenUserExists() {
        // given
        String sub = "sub-123";
        String email = "me@example.com";
        String nickname = "me";

        User existing = new User();
        existing.setId(1L);
        existing.setCognitoSub(sub);
        existing.setEmail(email);
        existing.setNickname(nickname);
        existing.setRole(RoleEnum.USER);
        existing.setRank(RankEnum.BEGINNER);
        existing.setImageUrl("/images/default-profile.png");

        given(userRepository.findByCognitoSub(sub)).willReturn(Optional.of(existing));

        // when
        User result = userService.findOrCreateUser(sub, email, nickname);

        // then
        assertThat(result).isSameAs(existing); // 같은 객체 반환
        verify(userRepository).findByCognitoSub(sub);
        verify(userRepository, never()).save(any()); // 저장되지 않아야 함
    }
}
