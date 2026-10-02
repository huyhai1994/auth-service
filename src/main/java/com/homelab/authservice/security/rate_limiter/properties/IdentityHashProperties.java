package com.homelab.authservice.security.rate_limiter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.identity-hash")
public record IdentityHashProperties(String secretKey) {
    public IdentityHashProperties {
        if (secretKey == null && secretKey.isBlank()) {
            throw new IllegalArgumentException(
                    "security.identity-hash.secret must not be blank"
            );
        }
    }
}
