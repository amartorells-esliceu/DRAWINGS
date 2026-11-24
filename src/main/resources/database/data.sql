-- Usuaris de prova. INSERT IGNORE evita errors si ja existeixen d'un inici anterior.
INSERT IGNORE INTO users (name, username, password)
VALUES ('Administrador', 'admin', '123456');

INSERT IGNORE INTO users (name, username, password)
VALUES ('Andreu Martorell', 'Andreu', '654321');
