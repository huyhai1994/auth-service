package com.homelab.authservice.security.authentication.register.exception;

public class PasswordLengthExceededException extends RuntimeException {
    public PasswordLengthExceededException(int maxPasswordLength) {
        super("Password must not exceed " + maxPasswordLength + " characters.");

    }
}
