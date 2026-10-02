package com.homelab.authservice.security.rate_limiter.exceptions;


public class RateLimiterUnavailableException extends RuntimeException {

    public RateLimiterUnavailableException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}
