CREATE TABLE IF NOT EXISTS roles
(
    id   INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL UNIQUE,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id BINARY(16) NOT NULL,
    role_id INTEGER NOT NULL,

    PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user_role_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_user_role_role
        FOREIGN KEY (role_id)
            REFERENCES roles(id)
            ON DELETE RESTRICT
);