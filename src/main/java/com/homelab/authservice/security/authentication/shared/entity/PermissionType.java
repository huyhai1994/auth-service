package com.homelab.authservice.security.authentication.shared.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionType {
    DELETE("delete"), DOWNLOAD("download"), UPLOAD("upload");
    private final String type;
}
