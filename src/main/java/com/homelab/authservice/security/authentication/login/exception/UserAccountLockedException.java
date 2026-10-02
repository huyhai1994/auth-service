package com.homelab.authservice.security.authentication.login.exception;

public class UserAccountLockedException extends RuntimeException {
    public UserAccountLockedException(String msg, Throwable ex) {
        super(msg, ex);
    }

    public UserAccountLockedException() {

    }
}
