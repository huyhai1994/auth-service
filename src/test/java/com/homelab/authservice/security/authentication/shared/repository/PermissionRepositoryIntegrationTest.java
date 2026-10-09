package com.homelab.authservice.security.authentication.shared.repository;

import com.homelab.authservice.security.authentication.shared.entity.*;
import com.homelab.authservice.security.configuration.PasswordEncoderTest;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import support.AbstractIntegrationTest;
import support.MockPasswordBuilder;
import support.MockUserBuilder;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static support.MockUserBuilder.NORMALIZED_USERNAME;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@Import(PasswordEncoderTest.class)
class PermissionRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void eachRolesShouldHaveItOwnPermissions() {
        List<Role> roles = roleRepository.findAll();
        assertThat(roles).isNotEmpty();

        for (Role role : roles) {
            persistAnValidUser(role);
        }

        List<User> users = userRepository.findAll();

        // ADMIN
        User admin = getUser(users, role -> role.getName().equals(RoleType.ADMIN));

        Set<PermissionType> permissionAdmins = getPermission(admin);

        assertThat(permissionAdmins)
                .containsExactlyInAnyOrder(
                        PermissionType.DELETE,
                        PermissionType.DOWNLOAD,
                        PermissionType.UPLOAD
                );

        // USER
        User user = getUser(users, role -> role.getName().equals(RoleType.USER));
        Set<PermissionType> permissionUsers = getPermission(user);

        assertThat(permissionUsers)
                .containsExactlyInAnyOrder(
                        PermissionType.DOWNLOAD,
                        PermissionType.UPLOAD
                );

        // GUEST
        User guest = getUser(users, role -> role.getName().equals(RoleType.GUEST));
        Set<PermissionType> permissionGuest = getPermission(guest);

        assertThat(permissionGuest)
                .containsExactlyInAnyOrder(
                        PermissionType.UPLOAD
                );

        assertThat(users).isNotEmpty();


    }

    private static @NonNull User getUser(List<User> users, Predicate<Role> predicate) {
        return users.stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(predicate))
                .findFirst()
                .orElseThrow();
    }

    private static @NonNull Set<PermissionType> getPermission(User user) {
        return user.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getType)
                .collect(Collectors.toSet());
    }

    private void persistAnValidUser(Role role) {
        User user = new User(NORMALIZED_USERNAME + UUID.randomUUID(), passwordEncoder.encode(MockPasswordBuilder.RAW_PASSWORD), MockUserBuilder.VALID_EMAIL, role);
        userRepository.saveAndFlush(user);
    }

}