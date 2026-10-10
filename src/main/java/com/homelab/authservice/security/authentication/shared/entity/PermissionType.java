package com.homelab.authservice.security.authentication.shared.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionType {
    DELETE("DELETE"), DOWNLOAD("DOWNLOAD"), UPLOAD("UPLOAD");
    private final String type;
}
