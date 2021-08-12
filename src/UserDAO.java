import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class UserDAO implements WriteDAO<User>, ReadDAO<User>{

    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    public UserDAO(){
    }

    @Override
    public void add(User user){

        try {
            this.connection = ConnectionFactory.getInstance().getConnection();

            String sql = "INSERT INTO user(id, name, lastName, email, password) VALUES(?,?,?,?,?)";

            preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(user.getId()));
			preparedStatement.setString(2, user.getName());
			preparedStatement.setString(3, user.getLastName());
			preparedStatement.setString(4, user.getEmail());
			preparedStatement.setString(5, user.getPassword());
			preparedStatement.executeUpdate();
            preparedStatement.close();

			System.out.println("User added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public User get(UUID id) throws SQLException {

        this.connection = ConnectionFactory.getInstance().getConnection();

        String sql = "SELECT name, lastName, email, password FROM user WHERE id=?";
        
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(id));
        resultSet = preparedStatement.executeQuery();
        
        resultSet.next();
        User user = new User(resultSet.getString("name"), resultSet.getString("lastName"),
                            resultSet.getString("email"), resultSet.getString("password"));

        user.setId(id);

        resultSet.close();
        preparedStatement.close();

        return Optional.of(user).orElseThrow(SQLException::new);
    }

    @Override
    public void update(User user){

        try {
            this.connection = ConnectionFactory.getInstance().getConnection();

            String sql = "UPDATE user SET name=?, lastName=?, email=?, password=? WHERE id=?";

            preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getLastName());
			preparedStatement.setString(3, user.getEmail());
			preparedStatement.setString(4, user.getPassword());
            preparedStatement.setBytes(5, Converter.fromUUIDtoByteArray(user.getId()));
			preparedStatement.executeUpdate();
            preparedStatement.close();

			System.out.println("User updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(User user){

        try {
            this.connection = ConnectionFactory.getInstance().getConnection();

            String sql = "DELETE FROM user WHERE id=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(user.getId()));
			preparedStatement.executeUpdate();
            preparedStatement.close();

			System.out.println("User deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<User> getAll() throws SQLException{

        this.connection = ConnectionFactory.getInstance().getConnection();

        String sql = "SELECT id, name, lastName, email, password FROM user";

        preparedStatement = this.connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        
        Set<User> users = new HashSet<User>();
        User user = null;

        while (resultSet.next()){
            user = new User(resultSet.getString("name"), resultSet.getString("lastName"),
                            resultSet.getString("email"), resultSet.getString("password"));
            user.setId(Converter.fromByteArrayToUUID(resultSet.getBytes("id")));
            users.add(user);
        }

        resultSet.close();
        preparedStatement.close();

        return Optional.of(users).orElseThrow(SQLException::new);
    }
}
