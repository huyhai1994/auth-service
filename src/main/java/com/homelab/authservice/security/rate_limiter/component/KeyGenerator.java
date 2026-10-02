package com.homelab.authservice.security.rate_limiter.component;

import org.springframework.stereotype.Component;

@Component
public class KeyGenerator {
    public String createKey(
            String keyPrefix,
            String identity,
            long windowStartMillis
    ) {
        return keyPrefix
                + identity
                + ":"
                + windowStartMillis;
    }
}
