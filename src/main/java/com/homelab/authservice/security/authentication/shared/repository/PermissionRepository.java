package com.homelab.authservice.security.authentication.shared.repository;


import com.homelab.authservice.security.authentication.shared.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}
