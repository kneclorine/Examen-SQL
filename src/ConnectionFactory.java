import java.sql.*;

public class ConnectionFactory {
    private final String driverClassName = "com.mysql.cj.jdbc.Driver";
    private final String url = System.getenv("DBUrl");
    private final String user = System.getenv("DBUser");
    private final String password = System.getenv("DBPassword");

    private static ConnectionFactory connectionFactory;

	private ConnectionFactory() {
		try {
            Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}

	public static ConnectionFactory getInstance() {
		if (connectionFactory == null) {
			connectionFactory = new ConnectionFactory();
		}
		return connectionFactory;
	}
}
