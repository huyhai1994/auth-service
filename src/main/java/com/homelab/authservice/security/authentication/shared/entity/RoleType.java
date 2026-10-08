package com.homelab.authservice.security.authentication.shared.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {

    USER("user"),
    ADMIN("admin"),
    GUEST("guest");

    private final String role;


    }
