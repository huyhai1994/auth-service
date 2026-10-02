package com.homelab.authservice.security.jwt.dto;

import java.util.List;

public record AuthenticatedUser(
        String username,
        List<String> authorities
) {
}
