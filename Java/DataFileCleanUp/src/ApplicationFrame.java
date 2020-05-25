import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ApplicationFrame extends JFrame {
  /**
   * Serial version ID.
   */
  private static final long serialVersionUID = 1L;

  public ApplicationFrame() {
    
  }
  
  public void start() {
    File inputFile = openFile();
    if (inputFile == null) {
      JOptionPane.showMessageDialog(this, "No file is loaded.", "Program Failed To Run", JOptionPane.WARNING_MESSAGE);
      System.exit(0);
    } else {
      int option = JOptionPane.showConfirmDialog(this, "Is the uploaded file Fixed-Width format?", "File Format Inquiry",
          JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
      if (option == JOptionPane.YES_OPTION) {
        JOptionPane.showMessageDialog(this, "The file is Fixed-Width", "", JOptionPane.PLAIN_MESSAGE);
        //TODO: REMOVE EXIT....
        System.exit(0);
      } else if (option == JOptionPane.NO_OPTION) {
        JOptionPane.showMessageDialog(this, "The file is NOT Fixed-Width", "", JOptionPane.PLAIN_MESSAGE);
        //TODO: REMOVE EXIT....
        System.exit(0);
      } else {
        JOptionPane.showMessageDialog(this, "Process is canceled.", "", JOptionPane.PLAIN_MESSAGE);
        System.exit(0);
      }
    }
    
  }
  
  private File openFile() {
    JFileChooser fileChooser = new JFileChooser();
    int opt = fileChooser.showOpenDialog(this);
    
    if (opt == JFileChooser.APPROVE_OPTION) {
      return fileChooser.getSelectedFile();
    }
    
    return null;
  }
}
