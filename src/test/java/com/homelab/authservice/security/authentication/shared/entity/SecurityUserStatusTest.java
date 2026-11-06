package com.homelab.authservice.security.authentication.shared.entity;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SecurityUserStatusTest {

    @Test
    void authenticatesOnlyActiveAccounts() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        User user = new User("alice", encoder.encode("valid-password"));
        user.setStatus(UserStatus.DEACTIVE);
        SecurityUser principal = new SecurityUser(user);
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(username -> principal);
        provider.setPasswordEncoder(encoder);
        var credentials = UsernamePasswordAuthenticationToken.unauthenticated("alice", "valid-password");

        assertThatThrownBy(() -> provider.authenticate(credentials))
                .isInstanceOf(DisabledException.class);
    }
}
