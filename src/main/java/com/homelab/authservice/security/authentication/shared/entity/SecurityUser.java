package com.homelab.authservice.security.authentication.shared.entity;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SecurityUser implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .map(RoleType::name)
                .map(s -> "ROLE_" + s)
                .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                .collect(Collectors.toCollection(ArrayList::new));

        List<GrantedAuthority> permissions = user.getRoles()
                .stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(Permission::getType)
                .map(PermissionType::getType)
                .map(s -> "SCOPE_" + s)
                .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                .toList();

        roles.addAll(permissions);

        return roles;
    }


    @Override
    public @Nullable String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public @Nullable String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
