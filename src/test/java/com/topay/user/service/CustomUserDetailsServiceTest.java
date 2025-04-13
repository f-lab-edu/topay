package com.topay.user.service;

import com.topay.user.domain.UserEntity;
import com.topay.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class CustomUserDetailsServiceTest {

    private UserRepository userRepository;
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    @DisplayName("사용자 존재 시, UserDetails 반환")
    void 사용자_존재_시_정상_반환() {
        // given
        String email = "test@example.com";
        String password = "password";

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password(password)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // then
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("존재하지 않는 사용자일 경우 예외 발생")
    void 사용자_미_존재시_예외_발생() {
        // given
        String email = "notfound@example.com";

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // then
        UsernameNotFoundException exception =
                assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));
        assertEquals("User not found", exception.getMessage());
    }

}
