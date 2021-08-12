import java.util.UUID;

public class App {
    public static void main(String[] args) throws Exception {
    
        IngredientDAO ingredientDAO = new IngredientDAO();
        PizzaDAO pizzaDAO = new PizzaDAO();
        UserDAO userDAO = new UserDAO();
        CommentDAO commentDAO = new CommentDAO();


        UUID idBase = UUID.randomUUID();
        Ingredient base = new Ingredient("Masa", 0.5);
        base.generateID(idBase);
        ingredientDAO.add(base);


        UUID idTomato = UUID.randomUUID();
        Ingredient tomato = new Ingredient("Tomate", 1.0);
        tomato.generateID(idTomato);
        ingredientDAO.add(tomato);


        UUID idCream = UUID.randomUUID();
        Ingredient cream = new Ingredient("Nata", 1.0);
        cream.generateID(idCream);
        ingredientDAO.add(cream);


        UUID idOnion = UUID.randomUUID();
        Ingredient onion = new Ingredient("Cebolla", 1.5);
        onion.generateID(idOnion);
        ingredientDAO.add(onion);

        
        UUID idMargarita = UUID.randomUUID();
        Pizza margarita = new Pizza("Margarita", "Url");
        margarita.generateID(idMargarita);
        margarita.addIngredient(base);
        margarita.addIngredient(tomato);
        pizzaDAO.add(margarita);
        pizzaDAO.update(margarita);
        pizzaDAO.delete(margarita);
        

        UUID idCarbonara = UUID.randomUUID();
        Pizza carbonara = new Pizza("Carbonara", "Url");
        carbonara.generateID(idCarbonara);
        carbonara.addIngredient(base);
        carbonara.addIngredient(cream);
        carbonara.addIngredient(onion);
        pizzaDAO.add(carbonara);


        UUID idUser1 = UUID.randomUUID();
        User user1 = new User("Alex", "Garcia", "eMail", "password");
        user1.generateID(idUser1);
        userDAO.add(user1);

        User selectUser1 = userDAO.get(user1.getId());
        System.out.println("Usuario: "+selectUser1.getName() + " " + selectUser1.getLastName());

        UUID idComment1 = UUID.randomUUID();
        Comment comment1 = new Comment("Esto es texto", 5, "12/08/2021", user1.getId(), carbonara.getId());
        comment1.generateID(idComment1);
        commentDAO.add(comment1);
    }
}
