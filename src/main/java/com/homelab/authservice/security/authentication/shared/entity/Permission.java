package com.homelab.authservice.security.authentication.shared.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission {
    @Id
    @Column(
            name = "id"
    )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(
            name = "name",
            nullable = false,
            length = 30
    )
    @Enumerated(EnumType.STRING)
    private PermissionType type;
}
