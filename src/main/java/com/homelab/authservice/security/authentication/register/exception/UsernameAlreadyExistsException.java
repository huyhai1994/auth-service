package com.homelab.authservice.security.authentication.register.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super(String.format("User %s already exist", username));
    }
}
