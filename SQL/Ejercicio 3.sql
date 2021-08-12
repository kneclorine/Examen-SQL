USE pizzeria;

SELECT pizza.name, SUM(ingredient.price*pizza_ingredient.quantity) AS precio FROM pizza
LEFT JOIN pizza_ingredient ON pizza.id = pizza_ingredient.id_pizza
LEFT JOIN ingredient ON pizza_ingredient.id_ingredient = ingredient.id
GROUP BY pizza.id, pizza.name, url;
