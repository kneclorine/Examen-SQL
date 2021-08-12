import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class CommentDAO implements WriteDAO<Comment>, ReadDAO<Comment>{

    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    public CommentDAO(){
    }

    @Override
    public void add(Comment comment){

        try {
            this.connection = ConnectionFactory.getInstance().getConnection();

            String sql = "INSERT INTO comment(id, text, rating, date, id_user, id_pizza) VALUES(?,?,?,?,?,?)";

            preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(comment.getId()));
			preparedStatement.setString(2, comment.getText());
			preparedStatement.setFloat(3, comment.getRating());
			preparedStatement.setString(4, comment.getDate());
			preparedStatement.setBytes(5, Converter.fromUUIDtoByteArray(comment.getUserID()));
			preparedStatement.setBytes(6, Converter.fromUUIDtoByteArray(comment.getPizzaID()));
			preparedStatement.executeUpdate();
            preparedStatement.close();

			System.out.println("Comment added successfully");
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
    public Comment get(UUID id) throws SQLException{
        this.connection = ConnectionFactory.getInstance().getConnection();

        String sql = "SELECT text, rating, date, id_user, id_pizza FROM comment WHERE id=?";

        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(id));
        resultSet = preparedStatement.executeQuery();
        
        resultSet.next();

        Comment comment = new Comment(resultSet.getString("text"), resultSet.getFloat("rating"),
                    resultSet.getString("date"), Converter.fromByteArrayToUUID(resultSet.getBytes("id_user")),
                    Converter.fromByteArrayToUUID(resultSet.getBytes("id_pizza")));

        comment.setId(id);

        resultSet.close();
        preparedStatement.close();
        this.connection.close();

        return Optional.of(comment).orElseThrow(SQLException::new);
    }

    @Override
    public void update(Comment comment){

        try {
            this.connection = ConnectionFactory.getInstance().getConnection();

            String sql = "UPDATE comment SET text=?, rating=?, date=?, id_user=?, id_pizza=? WHERE id=?";
            
            preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, comment.getText());
			preparedStatement.setFloat(2, comment.getRating());
			preparedStatement.setString(3, comment.getDate());
            preparedStatement.setBytes(4, Converter.fromUUIDtoByteArray(comment.getUserID()));
            preparedStatement.setBytes(5, Converter.fromUUIDtoByteArray(comment.getPizzaID()));
            preparedStatement.setBytes(6, Converter.fromUUIDtoByteArray(comment.getId()));
			preparedStatement.executeUpdate();
            preparedStatement.close();

			System.out.println("Comment updated successfully.");
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
    public void delete(Comment comment){

        try {
            this.connection = ConnectionFactory.getInstance().getConnection();

            String sql = "DELETE FROM comment WHERE id=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBytes(1, Converter.fromUUIDtoByteArray(comment.getId()));
			preparedStatement.executeUpdate();
            preparedStatement.close();

			System.out.println("Comment deleted successfully.");
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
    public Set<Comment> getAll() throws SQLException{
        
        this.connection = ConnectionFactory.getInstance().getConnection();

        String sql = "SELECT id, text, rating, date, id_user, id_pizza FROM comment";

        preparedStatement = this.connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        
        Set<Comment> comments = new HashSet<Comment>();
        Comment comment = null;

        while (resultSet.next()){
            comment = new Comment(resultSet.getString("text"), resultSet.getFloat("rating"),
                            resultSet.getString("date"), Converter.fromByteArrayToUUID(resultSet.getBytes("id_user")),
                            Converter.fromByteArrayToUUID(resultSet.getBytes("id_pizza")));
            
            comment.setId(Converter.fromByteArrayToUUID(resultSet.getBytes("id")));
            comments.add(comment);
        }

        resultSet.close();
        preparedStatement.close();

        return Optional.of(comments).orElseThrow(SQLException::new);
    }
}
