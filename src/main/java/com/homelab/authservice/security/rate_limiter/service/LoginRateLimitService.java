package com.homelab.authservice.security.rate_limiter.service;

public interface LoginRateLimitService {
    boolean allow(String identity);
}
