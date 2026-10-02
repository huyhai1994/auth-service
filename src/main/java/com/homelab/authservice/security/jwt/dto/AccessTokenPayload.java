package com.homelab.authservice.security.jwt.dto;

import java.util.List;

public record AccessTokenPayload(
        String username,
        List<String> authorities
) {
}