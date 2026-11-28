INSERT IGNORE INTO users (name, username, password)
VALUES ('Administrador', 'admin', '123456');

INSERT IGNORE INTO users (name, username, password)
VALUES ('Andreu Martorell', 'Andreu', '654321');

-- Dibuix de prova per comprovar la càrrega de figures i punts.
INSERT IGNORE INTO drawings (id, name, owner_username, canvas_width, canvas_height)
VALUES ('00000000-0000-4000-8000-000000000001', 'Dibuix de prova', 'admin', 800, 500);

INSERT IGNORE INTO figures (drawing_id, order_index, name, figure_type, color, x, y, size)
VALUES
    ('00000000-0000-4000-8000-000000000001', 0, 'Cercle blau', 'CIRCLE', '#3498db', 150, 150, 100),
    ('00000000-0000-4000-8000-000000000001', 1, 'Triangle vermell', 'TRIANGLE', '#e74c3c', 350, 150, 120),
    ('00000000-0000-4000-8000-000000000001', 2, 'Traç verd', 'FREEHAND', '#2ecc71', 0, 0, 0);

INSERT IGNORE INTO figure_points (figure_id, order_index, x, y)
SELECT id, 0, 480, 120 FROM figures
WHERE drawing_id = '00000000-0000-4000-8000-000000000001' AND order_index = 2;

INSERT IGNORE INTO figure_points (figure_id, order_index, x, y)
SELECT id, 1, 530, 170 FROM figures
WHERE drawing_id = '00000000-0000-4000-8000-000000000001' AND order_index = 2;

INSERT IGNORE INTO figure_points (figure_id, order_index, x, y)
SELECT id, 2, 490, 220 FROM figures
WHERE drawing_id = '00000000-0000-4000-8000-000000000001' AND order_index = 2;
