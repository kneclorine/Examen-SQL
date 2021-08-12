USE pizzeria;

INSERT INTO pizza (id, name, url) VALUES (UUID_TO_BIN(uuid(), 0), "Margarita", "URL");

SELECT pizza.id, pizza.name, pizza.url FROM pizza
LEFT JOIN pizza_ingredient ON pizza.id = pizza_ingredient.id_pizza
WHERE pizza_ingredient.id IS NULL;