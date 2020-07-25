import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
  private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
  private static final String URL = "jdbc:mysql://localhost:3306/LIBRARY?serverTimezone=UTC";
  private static final String CREDENTIAL = "libadmin";
  
  private static DBConnection instance = null;
  
  private Connection connection;

  public static DBConnection getInstance() throws SQLException {
    if (instance == null) {
      instance = new DBConnection();
    }
    return instance;
  }
  
  private DBConnection() throws SQLException {
    try {
      Class.forName(DRIVER).newInstance();
      connection = DriverManager.getConnection(URL, CREDENTIAL, CREDENTIAL);  
    } catch (ClassNotFoundException cnfe) {
      System.out.println("Missing: MYSQL Connector driver jar");
      cnfe.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public ResultSet executeSelectSQL(String sql) throws SQLException {
    Statement stmt = connection.createStatement();
    return stmt.executeQuery(sql);
  }
  
  public int executeUpdateSQL(String sql) throws SQLException {
    Statement stmt = connection.createStatement();
    return stmt.executeUpdate(sql);
  }
  
  public void close() {
    try {
      connection.close();
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
}
