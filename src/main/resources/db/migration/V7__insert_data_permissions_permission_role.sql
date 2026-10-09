INSERT INTO permissions (name)
VALUES ('DELETE'),
       ('DOWNLOAD'),
       ('UPLOAD');

INSERT INTO permission_roles (permission_id, role_id)
SELECT p.id, r.id
FROM permissions p
         CROSS JOIN roles r
WHERE (r.name = 'USER'
    AND p.name IN ('UPLOAD', 'DOWNLOAD'))

   OR (r.name = 'ADMIN'
    AND p.name IN ('DELETE', 'DOWNLOAD', 'UPLOAD'))

   OR (r.name = 'GUEST'
    AND p.name = 'UPLOAD');