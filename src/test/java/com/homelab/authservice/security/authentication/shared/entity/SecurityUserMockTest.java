package com.homelab.authservice.security.authentication.shared.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityUserMockTest {

    @InjectMocks
    SecurityUser securityUser;

    @Mock
    User user;

    @Mock
    Role role;

    @Test
    void getAuthorities_whenAccountIsUser_thenReturnUserRoleAndPermissions() {

        when(user.getRoles()).thenReturn(Set.of(role));

        when(role.getName()).thenReturn(RoleType.USER);

        when(role.getPermissions()).thenReturn(Set.of(
                new Permission(PermissionType.DOWNLOAD),
                new Permission(PermissionType.UPLOAD)
        ));

        List<GrantedAuthority> results =
                new ArrayList<>(securityUser.getAuthorities());

        assertThat(results)
                .containsExactlyInAnyOrder(
                        new SimpleGrantedAuthority("ROLE_USER"),
                        new SimpleGrantedAuthority("SCOPE_UPLOAD"),
                        new SimpleGrantedAuthority("SCOPE_DOWNLOAD")
                );    }
}