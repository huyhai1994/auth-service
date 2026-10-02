package com.homelab.authservice.security.authentication.register.exception;

public class UsernameLengthExceededException extends RuntimeException {
    public UsernameLengthExceededException(int lower, int upper) {
        super(String.format("Username must not exceed %d - %d characters", lower, upper));
    }
}
