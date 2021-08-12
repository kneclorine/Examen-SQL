import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class PizzaDAO implements ReadDAO<Pizza>, WriteDAO<Pizza>{
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    public PizzaDAO(){
    }

    @Override
    public void add(Pizza pizza){
    
        try {
            this.connection = ConnectionFactory.getInstance().getConnection();
            this.connection.setAutoCommit(false);

            String sql = "INSERT INTO pizza(id, name, url) VALUES (?,?,?)";

            preparedStatement = this.connection.prepareStatement(sql);
			preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(pizza.getId()));
			preparedStatement.setString(2, pizza.getName());
			preparedStatement.setString(3, pizza.getPhotoURL());
			preparedStatement.executeUpdate();
            preparedStatement.close();

            sql = "INSERT INTO pizza_ingredient(id, id_pizza, id_ingredient) VALUES (?,?,?)";

            for (Ingredient ingredient : pizza.getIngredients()) {
                preparedStatement = this.connection.prepareStatement(sql);
                preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(UUID.randomUUID()));
                preparedStatement.setBytes(2, Converter.fromUUIDtoByteArray(pizza.getId()));
                preparedStatement.setBytes(3, Converter.fromUUIDtoByteArray(ingredient.getId()));
                preparedStatement.executeUpdate();
            }

            preparedStatement.close();

            this.connection.commit();

			System.out.println("Pizza added successfully");
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally{
            try {
                if(this.connection != null){
                    this.connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public Pizza get(UUID id) throws SQLException{

        this.connection = ConnectionFactory.getInstance().getConnection();

        String sql = """
        SELECT p.id, p.name, url, i.id, i.name, i.price FROM pizza p 
        INNER JOIN pizza_ingredient pi ON p.id = pi.id_pizza
        INNER JOIN ingredient i ON pi.id_ingredient = i.id
        WHERE p.id=?
        """;

        preparedStatement = this.connection.prepareStatement(sql);
        preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(id));
        resultSet = preparedStatement.executeQuery();
        
        Pizza pizza = null;
        Set<Ingredient> ingredients = new HashSet<Ingredient>();

        while (resultSet.next()) {
            if(pizza == null){
                pizza = new Pizza(resultSet.getString("p.name"), resultSet.getString("url"));
                pizza.setId(Converter.fromByteArrayToUUID(resultSet.getBytes("p.id")));
            }
            Ingredient ingredient = new Ingredient(resultSet.getString("i.name"),
                                                    resultSet.getDouble("i.price"));

            ingredient.setId(Converter.fromByteArrayToUUID(resultSet.getBytes("id")));
            ingredients.add(ingredient);
        }

        for (Ingredient ingredient : ingredients) {
            pizza.addIngredient(ingredient);
        }

        resultSet.close();
        preparedStatement.close();
        this.connection.close();

        return Optional.of(pizza).orElseThrow(SQLException::new);
    }

    public void update(Pizza pizza){
        try {
            this.connection = ConnectionFactory.getInstance().getConnection();
            this.connection.setAutoCommit(false);

            String sql = "UPDATE pizza SET name = ?, url = ? WHERE id = ?";

            preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, pizza.getName());
			preparedStatement.setString(2, pizza.getPhotoURL());
            preparedStatement.setBytes(3, Converter.fromUUIDtoByteArray(pizza.getId()));
			preparedStatement.executeUpdate();
            preparedStatement.close();

            sql = "DELETE FROM pizza_ingredient WHERE id_pizza=?";

            preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(pizza.getId()));
            preparedStatement.executeUpdate();
            preparedStatement.close();

            sql = "INSERT INTO pizza_ingredient(id, id_pizza, id_ingredient) VALUES (?,?,?)";

            for (Ingredient ingredient : pizza.getIngredients()) {
                preparedStatement = this.connection.prepareStatement(sql);
                preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(UUID.randomUUID()));
                preparedStatement.setBytes(2, Converter.fromUUIDtoByteArray(pizza.getId()));
                preparedStatement.setBytes(3, Converter.fromUUIDtoByteArray(ingredient.getId()));
                preparedStatement.executeUpdate();
            }

            preparedStatement.close();

            this.connection.commit();

			System.out.println("Pizza updated successfully.");
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally{
            try {
                if(this.connection != null){
                    this.connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(Pizza pizza){

        try {
            this.connection = ConnectionFactory.getInstance().getConnection();

            String sql = "DELETE FROM pizza WHERE id=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(pizza.getId()));
			preparedStatement.executeUpdate();
            preparedStatement.close();

			System.out.println("Pizza deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                if(this.connection != null){
                    this.connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Set<Pizza> getAll() throws SQLException{

        this.connection = ConnectionFactory.getInstance().getConnection();

        String sql = """
        SELECT p.id, p.name, url, i.id, i.name, i.price FROM pizza p 
        LEFT JOIN pizza_ingredient pi ON p.id = pi.id_pizza
        LEFT JOIN ingredient i ON pi.id_ingredient = i.id
        """;

        preparedStatement = this.connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        
        Set<Pizza> pizzas = new HashSet<Pizza>();
        Pizza  pizza = null; 

        while (resultSet.next()){
            if(!(resultSet.getBytes("p.id").equals(Converter.fromUUIDtoByteArray(pizza.getId())))){
                pizza = new Pizza(resultSet.getString("p.name"), resultSet.getString("url"));
                pizza.setId(Converter.fromByteArrayToUUID(resultSet.getBytes("p.id")));
            }
            Ingredient ingredient = new Ingredient(resultSet.getString("i.name"), resultSet.getDouble("i.price"));
            ingredient.setId(Converter.fromByteArrayToUUID(resultSet.getBytes("i.id")));

            pizza.addIngredient(ingredient);
        }

        resultSet.close();
        preparedStatement.close();
        this.connection.close();

        return Optional.of(pizzas).orElseThrow(SQLException::new);
    }
}
