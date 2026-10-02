package com.homelab.authservice.shared.exception;

public class InvalidStateTransitionException extends RuntimeException{

    public InvalidStateTransitionException() {
        super("Invalid State Transition");
    }
}
