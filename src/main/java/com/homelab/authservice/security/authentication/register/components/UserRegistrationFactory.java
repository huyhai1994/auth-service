package com.homelab.authservice.security.authentication.register.components;

import com.homelab.authservice.security.authentication.register.dto.RegisterRequest;
import com.homelab.authservice.security.authentication.register.service.UsernameVerifyService;
import com.homelab.authservice.security.authentication.shared.entity.User;
import com.homelab.authservice.security.authentication.shared.service.NormalizeUsernameService;
import com.homelab.authservice.security.authentication.shared.service.PasswordVerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegistrationFactory {

    private final NormalizeUsernameService normalizeUsernameService;
    private final UsernameVerifyService usernameVerifyService;
    private final PasswordVerifyService passwordVerifyService;
    private final PasswordEncoder passwordEncoder;

    public User createUser(RegisterRequest request) {
        String normalizedUsername = normalizeUsernameService.normalizeUsername(request.username());

        usernameVerifyService.verify(normalizedUsername);
        passwordVerifyService.verify(request.password());

        String passwordHash = passwordEncoder.encode(request.password());

        return new User(
                normalizedUsername,
                passwordHash,
                request.emailAddress()
        );

    }

}
