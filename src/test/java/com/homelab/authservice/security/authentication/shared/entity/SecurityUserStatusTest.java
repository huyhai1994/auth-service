package com.homelab.authservice.security.authentication.shared.entity;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SecurityUserStatusTest {

    @ParameterizedTest
    @EnumSource(UserStatus.class)
    void authenticatesOnlyActiveAccounts(UserStatus status) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        User user = new User("alice", encoder.encode("valid-password"));
        user.setStatus(status);
        SecurityUser principal = new SecurityUser(user);
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(username -> principal);
        provider.setPasswordEncoder(encoder);
        var credentials = UsernamePasswordAuthenticationToken.unauthenticated("alice", "valid-password");

        if (status == UserStatus.ACTIVE) {
            assertThat(provider.authenticate(credentials).isAuthenticated()).isTrue();
        } else {
            assertThatThrownBy(() -> provider.authenticate(credentials))
                    .isInstanceOf(DisabledException.class);
        }
    }
}
