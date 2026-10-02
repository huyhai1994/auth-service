package com.homelab.authservice.security.authentication.register.service;

import com.homelab.authservice.security.authentication.shared.entity.SecurityUser;
import com.homelab.authservice.security.authentication.shared.entity.User;
import com.homelab.authservice.security.authentication.shared.service.NormalizeUsernameService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsManager implements UserDetailsService {
    private final NormalizeUsernameService normalizeUsernameService;
    private final UserFinderService userFinderService;

    @Override
    @Transactional(readOnly = true)
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) {
        String normalizedUsername = normalizeUsernameService.normalizeUsername(username);
        User user = getRequiredUser(normalizedUsername);
        return new SecurityUser(user);
    }

    private User getRequiredUser(String username) {
        return userFinderService.find(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + username
                ));
    }
}