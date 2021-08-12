USE pizzeria;

START TRANSACTION;
INSERT INTO ingredient (id, name, price) VALUES (UUID_TO_BIN(uuid(), 0), "Tomate", 1.0);
INSERT INTO ingredient (id, name, price) VALUES (UUID_TO_BIN(uuid(), 0), "Queso", 3.0);

INSERT INTO pizza (id, name, url) VALUES (UUID_TO_BIN(uuid(), 0), "Carbonara", "URL");

COMMIT;

INSERT INTO pizza_ingredient (id, id_pizza, id_ingredient) VALUES (UUID_TO_BIN(uuid(), 0),
(SELECT id FROM pizza WHERE name="Carbonara"),
(SELECT id FROM ingredient WHERE name="Tomate"));

INSERT INTO pizza_ingredient (id, id_pizza, id_ingredient) VALUES (UUID_TO_BIN(uuid(), 0),
(SELECT id FROM pizza WHERE name="Carbonara"),
(SELECT id FROM ingredient WHERE name="Queso"));