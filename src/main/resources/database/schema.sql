CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(20) PRIMARY KEY NOT NULL,
    name VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS drawings (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    owner_username VARCHAR(20) NOT NULL,
    canvas_width INT NOT NULL DEFAULT 800,
    canvas_height INT NOT NULL DEFAULT 500,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_username) REFERENCES users (username)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS figures (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    drawing_id VARCHAR(36) NOT NULL,
    order_index INT NOT NULL,
    name VARCHAR(255),
    figure_type VARCHAR(50),
    color VARCHAR(50),
    x INT NOT NULL DEFAULT 0,
    y INT NOT NULL DEFAULT 0,
    size INT NOT NULL DEFAULT 0,
    FOREIGN KEY (drawing_id) REFERENCES drawings (id)
        ON DELETE CASCADE,
    UNIQUE (drawing_id, order_index)
);

CREATE TABLE IF NOT EXISTS figure_points (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    figure_id BIGINT NOT NULL,
    order_index INT NOT NULL,
    x INT NOT NULL,
    y INT NOT NULL,
    FOREIGN KEY (figure_id) REFERENCES figures (id)
        ON DELETE CASCADE,
    UNIQUE (figure_id, order_index)
);
