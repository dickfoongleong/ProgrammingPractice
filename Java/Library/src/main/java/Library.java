import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Library extends JFrame implements WindowListener {

  /**
   * Serial Version ID.
   */
  private static final long serialVersionUID = 1L;
  
  private static User user = null;
  private static DBConnection connection;
  
  public Library() {
    try {
      connection = DBConnection.getInstance();
      showLogin();
      start();
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
  
  private void showLogin() {
    JPanel labelPanel = new JPanel();
    labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
    JLabel usernameLabel = new JLabel("Username:");
    JLabel passwordLabel = new JLabel("PasswordLabel");
    labelPanel.add(usernameLabel);
    labelPanel.add(passwordLabel);
    
    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
    JTextField usernameField = new JTextField(10);
    JPasswordField passwordField = new JPasswordField();
    inputPanel.add(usernameField);
    inputPanel.add(passwordField);

    JPanel loginPanel = new JPanel();
    loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    loginPanel.add(labelPanel);
    loginPanel.add(inputPanel);
    
    while(true) {
      usernameField.setText("");
      passwordField.setText("");
      
      int option = JOptionPane.showConfirmDialog(this, loginPanel, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
      if (option == JOptionPane.OK_OPTION) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        try {
          MessageDigest md = MessageDigest.getInstance("MD5");
          byte[] bytes = md.digest(password.getBytes());
          BigInteger no = new BigInteger(1, bytes);
          
          password = no.toString(16);
          while (password.length() < 32) {
            password = "0" + password; 
          }
          
          String userSQL = "SELECT ID, ADMIN, FIRST_NAME, LAST_NAME FROM USER WHERE USERNAME='"+username+"' AND PASSWORD='"+password+"'";
          ResultSet rs = connection.executeSelectSQL(userSQL);
          if (rs.next()) {
            int id = rs.getInt("ID");
            boolean isAdmin = rs.getBoolean("ADMIN");
            String fName = rs.getString("FIRST_NAME");
            String lName = rs.getString("LAST_NAME");
            user = new User(id, fName, lName, username, isAdmin);
          }
          
          if (user == null) {
            JOptionPane.showMessageDialog(this, "User not found. Please try again.");
          } else {
            break;
          }
        } catch (NoSuchAlgorithmException nsae) {
          nsae.printStackTrace();
        } catch (SQLException sqle) {
          sqle.printStackTrace();
        }
      } else {
        System.exit(0);
      }
    }
  }
  
  private void start() {
    addWindowListener(this);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    
    setExtendedState(MAXIMIZED_BOTH);
    setVisible(true);
  }

  @Override
  public void windowOpened(WindowEvent e) {}

  @Override
  public void windowClosing(WindowEvent e) {
    try {
      DBConnection.getInstance().close();
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
    System.exit(0);
  }

  @Override
  public void windowClosed(WindowEvent e) {}

  @Override
  public void windowIconified(WindowEvent e) {}

  @Override
  public void windowDeiconified(WindowEvent e) {}

  @Override
  public void windowActivated(WindowEvent e) {}

  @Override
  public void windowDeactivated(WindowEvent e) {}

  public static void main(String[] args) throws Exception {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new Library();
      }
    });
  }
}
