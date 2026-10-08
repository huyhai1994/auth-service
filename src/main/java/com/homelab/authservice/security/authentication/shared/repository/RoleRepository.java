package com.homelab.authservice.security.authentication.shared.repository;

import com.homelab.authservice.security.authentication.shared.entity.Role;
import com.homelab.authservice.security.authentication.shared.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findRoleByName(RoleType roleType);

}
