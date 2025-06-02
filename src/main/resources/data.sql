DELETE FROM clients;
DELETE FROM users;
DELETE FROM direccion;




-- Direcciones
INSERT INTO direccion (id, calle, numero, portal, piso, codigo_postal, ciudad, provincia) VALUES
(1, 'Calle Mayor', '12', 'A', '3º', '28013', 'Madrid', 'Madrid'),
(2, 'Avenida Libertad', '45', 'B', '2º', '03001', 'Alicante', 'Alicante'),
(3, 'Calle del Sol', '8', 'C', '1º', '08001', 'Barcelona', 'Barcelona'),
(4, 'Paseo de la Castellana', '100', 'D', '5º', '28046', 'Madrid', 'Madrid'),
(5, 'Ronda Norte', '23', 'E', '4º', '50015', 'Zaragoza', 'Zaragoza');

-- Usuarios
INSERT INTO users (id, username, password, created_at, updated_at, is_active) VALUES
(1, 'alice', 'password123', '2024-01-01T10:00:00', '2024-04-01T12:00:00', true),
(2, 'bob', 'password123', '2024-01-02T10:00:00', '2024-04-02T12:00:00', true),
(3, 'carol', 'password123', '2024-01-03T10:00:00', '2024-04-03T12:00:00', true),
(4, 'dave', 'password123', '2024-01-04T10:00:00', '2024-04-04T12:00:00', true),
(5, 'eve', 'password123', '2024-01-05T10:00:00', '2024-04-05T12:00:00', true);

-- Clientes
INSERT INTO clients (id, address_id, phone_number, name, surname, user_id) VALUES
(1, 1, '600111111', 'Alicia', 'Pérez', 1),
(2, 2, '600222222', 'Roberto', 'Gómez', 2),
(3, 3, '600333333', 'Carolina', 'Ruiz', 3),
(4, 4, '600444444', 'David', 'Sánchez', 4),
(5, 5, '600555555', 'Eva', 'Martínez', 5);
