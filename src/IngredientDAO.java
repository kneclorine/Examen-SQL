import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class IngredientDAO implements ReadDAO<Ingredient>, WriteDAO<Ingredient>{

    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    public IngredientDAO(){
    }

    @Override
    public void add(Ingredient ingredient){
      
        try {
            this.connection = ConnectionFactory.getInstance().getConnection();

            String sql = "INSERT INTO ingredient(id, name, price) VALUES(?,?,?)";

            preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(ingredient.getId()));
			preparedStatement.setString(2, ingredient.getName());
			preparedStatement.setDouble(3, ingredient.getPrice());
			preparedStatement.executeUpdate();
            preparedStatement.close();

			System.out.println("Ingredient added successfully");
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
    public Ingredient get(UUID id) throws SQLException{

        this.connection = ConnectionFactory.getInstance().getConnection();

        String sql = "SELECT id, name, price FROM ingredient WHERE id=?";

        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(id));
        resultSet = preparedStatement.executeQuery();

        resultSet.next();
        Ingredient ingredient = new Ingredient(resultSet.getString("name"), resultSet.getDouble("price"));
        ingredient.setId(Converter.fromByteArrayToUUID(resultSet.getBytes("id")));

        resultSet.close();
        preparedStatement.close();

        return Optional.of(ingredient).orElseThrow(SQLException::new);
    }

    @Override
    public void update(Ingredient ingredient){
        
        try {
            this.connection = ConnectionFactory.getInstance().getConnection();
            
            String sql = "UPDATE ingredient SET name=?, price=? WHERE id=?";

            preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, ingredient.getName());
			preparedStatement.setDouble(2, ingredient.getPrice());
            preparedStatement.setBytes(3, Converter.fromUUIDtoByteArray(ingredient.getId()));
			preparedStatement.executeUpdate();
            preparedStatement.close();

			System.out.println("Ingredient updated successfully.");
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
    public void delete(Ingredient ingredient){

        try {
            this.connection = ConnectionFactory.getInstance().getConnection();

            String sql = "DELETE FROM ingredient WHERE id=?";
           
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(ingredient.getId()));
			preparedStatement.executeUpdate();
            preparedStatement.close();

			System.out.println("Igredient deleted successfully.");
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
    public Set<Ingredient> getAll() throws SQLException{

        this.connection = ConnectionFactory.getInstance().getConnection();

        String sql = "SELECT name, price FROM ingredient";

        preparedStatement = this.connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        
        Set<Ingredient> ingredients = new HashSet<Ingredient>();
        Ingredient ingredient = null;

        while (resultSet.next()){
            ingredient = new Ingredient(resultSet.getString("name"), resultSet.getDouble("price"));
            ingredient.setId(Converter.fromByteArrayToUUID(resultSet.getBytes("id")));
            ingredients.add(ingredient);
        }

        resultSet.close();
        preparedStatement.close();

        return Optional.of(ingredients).orElseThrow(SQLException::new);
    }
}
