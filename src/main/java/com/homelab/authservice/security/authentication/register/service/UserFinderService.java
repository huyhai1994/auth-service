package com.homelab.authservice.security.authentication.register.service;

import com.homelab.authservice.security.authentication.shared.entity.User;
import com.homelab.authservice.security.authentication.shared.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFinderService {
    private final UserRepository userRepository;

    public Optional<User> find(String username) {
        return userRepository.findByUsername(username);
    }


}
