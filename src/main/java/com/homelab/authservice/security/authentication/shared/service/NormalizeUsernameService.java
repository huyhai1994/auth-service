package com.homelab.authservice.security.authentication.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class NormalizeUsernameService {

    public String normalizeUsername(String username) {
        return
                username
                        .trim()
                        .toLowerCase(Locale.ROOT);
    }
}
