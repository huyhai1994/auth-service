package com.homelab.authservice.security.authentication.register.components;

import com.homelab.authservice.security.authentication.register.dto.RegisterRequest;
import com.homelab.authservice.security.authentication.register.service.UsernameVerifyService;
import com.homelab.authservice.security.authentication.shared.entity.Role;
import com.homelab.authservice.security.authentication.shared.entity.RoleType;
import com.homelab.authservice.security.authentication.shared.entity.User;
import com.homelab.authservice.security.authentication.shared.repository.RoleRepository;
import com.homelab.authservice.security.authentication.shared.service.NormalizeUsernameService;
import com.homelab.authservice.security.authentication.shared.service.PasswordVerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserRegistrationFactory {

    private final NormalizeUsernameService normalizeUsernameService;
    private final UsernameVerifyService usernameVerifyService;
    private final PasswordVerifyService passwordVerifyService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public User createUser(RegisterRequest request) {
        String normalizedUsername = normalizeUsernameService.normalizeUsername(request.username());

        usernameVerifyService.verify(normalizedUsername);
        passwordVerifyService.verify(request.password());

        String passwordHash = passwordEncoder.encode(request.password());

        Role role = roleRepository.findRoleByName(RoleType.USER).orElseThrow();

        return new User(
                normalizedUsername,
                passwordHash,
                request.emailAddress(),
                role
        );

    }

}
