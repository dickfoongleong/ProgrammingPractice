import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ApplicationFrame extends JFrame implements WindowListener {
  /**
   * Serial version ID.
   */
  private static final long serialVersionUID = 1L;
  
  private static Parser parser = null;
  
  public ApplicationFrame() {
    addWindowListener(this);
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
        String delimiter = "\0";
        String encloseChar = "\0";

        while (true) {
          String userInput = JOptionPane.showInputDialog(this, "Delimiter: Default delimiter is comma(,)");

          if (userInput == null) {
            endProcess();
          } else {
            if (userInput.trim().length() <= 1) {
              delimiter = userInput.trim().isEmpty() ? delimiter : userInput.trim();
              break;
            } else {
              int opt = JOptionPane.showConfirmDialog(this, "Please enter 1 character only for delimiter", "", JOptionPane.OK_CANCEL_OPTION);
              if (opt != JOptionPane.OK_OPTION) {
                endProcess();
              }              
            }
          }
        }
        
        while (true) {
          String userInput = JOptionPane.showInputDialog(this, "Quotation: Default quotation is Double-Quote(\")");
          
          if (userInput == null) {
            endProcess();
          } else {
            if (userInput.trim().length() <= 1) {
              encloseChar = userInput.trim().isEmpty() ? encloseChar : userInput.trim();
              break;
            } else {
              int opt = JOptionPane.showConfirmDialog(this, "Please enter 1 character only for quotation", "", JOptionPane.OK_CANCEL_OPTION);
              if (opt != JOptionPane.OK_OPTION) {
                endProcess();
              }
            }
          }
        }
        
        displayCSV(inputFile, delimiter.charAt(0), encloseChar.charAt(0));
      } else {
        endProcess();
      }
    }
  }
  
  private void displayCSV(File inputFile, char delimiter, char encloseChar) {
    parser = new CSVParser(inputFile, delimiter, encloseChar);
    
    try {
      parser.parseData();
      Map<Long, Data[]> dataMap = parser.getDataMap();
      
      for (long counter : dataMap.keySet()) {
        System.out.print(counter);
        for (Data data : dataMap.get(counter)) {
          System.out.print("," + data.getValue());
        }
        System.out.println();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setTitle("CSV Viewer");
    
    CSVPortView viewer = new CSVPortView();
    setContentPane(viewer);
    
    revalidate();
    repaint();
    setPreferredSize(new Dimension(200, 200));
    setVisible(true);
  }

  private File openFile() {
    JFileChooser fileChooser = new JFileChooser();
    int opt = fileChooser.showOpenDialog(this);

    if (opt == JFileChooser.APPROVE_OPTION) {
      return fileChooser.getSelectedFile();
    }

    return null;
  }

  private void endProcess() {
    JOptionPane.showMessageDialog(this, "Process is canceled.", "", JOptionPane.PLAIN_MESSAGE);
    System.exit(0);
  }

  @Override
  public void windowOpened(WindowEvent e) {
  }

  @Override
  public void windowClosing(WindowEvent e) {
    if (parser instanceof CSVParser) {
      System.out.println("Is CSV Parser");
    } else {
      System.out.println("Not CSV Parser");
    }
    System.exit(0);
  }

  @Override
  public void windowClosed(WindowEvent e) {
  }

  @Override
  public void windowIconified(WindowEvent e) {
  }

  @Override
  public void windowDeiconified(WindowEvent e) {
  }

  @Override
  public void windowActivated(WindowEvent e) {
  }

  @Override
  public void windowDeactivated(WindowEvent e) {
  }
}
