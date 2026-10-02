package com.homelab.authservice.security.authentication.register.exception;

public class PasswordTooShortException extends RuntimeException {
    public PasswordTooShortException(int minPasswordLength) {
        super("Password must contain at least " + minPasswordLength + " characters.");

    }
}
