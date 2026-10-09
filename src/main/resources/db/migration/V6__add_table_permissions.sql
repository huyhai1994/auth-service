CREATE TABLE IF NOT EXISTS permissions
(
    id   INTEGER AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS permission_roles
(
    permission_id INTEGER,
    role_id       INTEGER NOT NULL,

    PRIMARY KEY (permission_id, role_id),

    CONSTRAINT fk_permission_roles_permission
        FOREIGN KEY (permission_id)
            REFERENCES permissions (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_permission_roles_role
        FOREIGN KEY (role_id)
            REFERENCES roles (id)
            ON DELETE RESTRICT
);