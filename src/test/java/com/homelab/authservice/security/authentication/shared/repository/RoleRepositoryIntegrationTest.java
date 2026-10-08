package com.homelab.authservice.security.authentication.shared.repository;

import com.homelab.authservice.security.authentication.shared.entity.Role;
import com.homelab.authservice.security.authentication.shared.entity.RoleType;
import com.homelab.authservice.security.authentication.shared.entity.User;
import com.homelab.authservice.security.configuration.PasswordEncoderTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import support.AbstractIntegrationTest;
import support.MockPasswordBuilder;
import support.MockUserBuilder;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static support.MockUserBuilder.NORMALIZED_USERNAME;

@DataJpaTest
@ActiveProfiles("test")
@Import(PasswordEncoderTest.class)
class RoleRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        Role role = roleRepository.findRoleByName(RoleType.USER).orElseThrow();
        persistAnValidUser(role);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        roleRepository.deleteAllInBatch();
    }

    @Test
    void saveUser_whenSaveUserAndRole_thenUserHaveRole() {
        User user = userRepository
                .findAll()
                .get(0);
        assertThat(user).isNotNull();
    }

    private void persistAnValidUser(Role role) {
        User user = new User(NORMALIZED_USERNAME, passwordEncoder.encode(MockPasswordBuilder.RAW_PASSWORD), MockUserBuilder.VALID_EMAIL, role);
        userRepository.saveAndFlush(user);
    }

}