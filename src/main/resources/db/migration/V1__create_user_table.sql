CREATE TABLE tb_users (
    id BINARY(16) PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

INSERT INTO tb_users (id, username, email) VALUES
(UNHEX(REPLACE('a1b2c3d4-1234-5678-9abc-def012345678', '-', '')), 'maria_silva', 'maria.silva@exemplo.com'),
(UNHEX(REPLACE('f5e6d7c8-4321-8765-cba9-876543210fed', '-', '')), 'carlos_dev', 'carlos@exemplo.com'),
(UNHEX(REPLACE('11223344-5566-7788-99aa-bbccddeeff00', '-', '')), 'ana_oliveira', 'ana.ol@exemplo.com'),
(UNHEX(REPLACE('ffeeddcc-bbaa-9988-7766-554433221100', '-', '')), 'pedro_santos', 'pedro.santos@exemplo.com'),
(UNHEX(REPLACE('98765432-12ab-34cd-56ef-7890abcdef12', '-', '')), 'julia_tech', 'julia@exemplo.com');