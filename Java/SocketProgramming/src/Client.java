import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements WindowListener {
  private static final long serialVersionUID = 1L;
  private static final String SERVER_IP = "10.0.0.18";
  private static final List<String> ONLINE_USERS = new ArrayList<String>();

  private JScrollPane outputPane, userPane;
  private JTextArea inputTextArea;

  private Socket socket;
  private PrintWriter output;
  private BufferedReader input;

  public Client(String username) {
    addWindowListener(this);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setResizable(false);
    setTitle(username + " Chat Room");

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BorderLayout());
    contentPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    setContentPane(contentPanel);

    initComponents();

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(outputPane, BorderLayout.CENTER);
    mainPanel.add(userPane, BorderLayout.EAST);
    contentPanel.add(mainPanel, BorderLayout.CENTER);

    contentPanel.add(msgInputComp(), BorderLayout.SOUTH);

    Dimension size = new Dimension(900, 500);
    setPreferredSize(size);
    setMaximumSize(size);
    setMinimumSize(size);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int scMidWidth = screenSize.width / 2;
    int scMidHeight = screenSize.height / 2;
    int frameMidWidth = size.width / 2;
    int frameMidHeight = size.height / 2;
    setLocation(scMidWidth - frameMidWidth, scMidHeight - frameMidHeight);

    setVisible(true);
    inputTextArea.requestFocus();

    try {
      socket = new Socket(SERVER_IP, 9000);

      output = new PrintWriter(socket.getOutputStream(), true);
      input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      
      Thread inputReader = new Thread(new Runnable() {
        public void run() {
          try {
            msgFromServer();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });

      inputReader.start();
      msgToServer(username);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initComponents() {
    outputPane = new JScrollPane();
    JTextArea outputArea = new JTextArea();
    outputArea.setEditable(false);

    outputPane.setViewportView(outputArea);

    userPane = new JScrollPane();
    userPane.setPreferredSize(new Dimension(200, 0));
    userPane.setMaximumSize(new Dimension(200, 0));
    userPane.setMinimumSize(new Dimension(200, 0));
    updateUserList();
  }

  private JPanel msgInputComp() {
    JPanel msgPanel = new JPanel();
    msgPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

    inputTextArea = new JTextArea();
    JScrollPane msgPane = new JScrollPane(inputTextArea);
    msgPane.setPreferredSize(new Dimension(500, 50));
    msgPanel.add(msgPane);

    JButton enterBtn = new JButton("Send");
    enterBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        String msg = inputTextArea.getText();
        if (!msg.trim().isEmpty()) {
          try {
            msgToServer(msg);
            inputTextArea.setText("");
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        inputTextArea.requestFocus();
      }
    });
    msgPanel.add(enterBtn);

    return msgPanel;
  }

  private void updateMsg(String msg) {
    JTextArea textArea = (JTextArea) outputPane.getViewport().getView();
    String text = textArea.getText();
    textArea.setText(text + msg + "\n");

    JScrollBar verticalBar = outputPane.getVerticalScrollBar();
    verticalBar.setValue(verticalBar.getMaximum());
    revalidate();
    repaint();
  }

  private void updateUserList() {
    JPanel userPanel = new JPanel();
    userPanel.setOpaque(true);
    userPanel.setBackground(Color.WHITE);
    userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
    for (String user : ONLINE_USERS) {
      userPanel.add(new JLabel(user));
    }
    userPane.setViewportView(userPanel);
  }

  private void msgFromServer() throws Exception {
    String line = "";
    while ((line = input.readLine()) != null) {
      if (line.startsWith("******") && line.endsWith("has joined ******")) {
        String newUser = line.substring(7, line.length() - 18);
        
        if (!ONLINE_USERS.contains(newUser)) {
          updateMsg(line);
          ONLINE_USERS.add(newUser);
        }
        updateUserList();
      } else if (line.startsWith("******") && line.endsWith("has left ******")) {
        String rmvUser = line.substring(7, line.length() - 16);
        ONLINE_USERS.remove(rmvUser);
        updateUserList();
        
        updateMsg(line);
      } else {
        updateMsg(line); 
      }
    }
  }

  private void msgToServer(String msg) throws Exception {
    output.println(msg);
  }

  @Override
  public void windowOpened(WindowEvent e) {
  }

  @Override
  public void windowClosing(WindowEvent e) {
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

  public static void main(String[] args) {
    while (true) {
      final String username = JOptionPane.showInputDialog("Please enter your name:");

      if (username == null) {
        System.exit(0);
      } else if (username.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Name must not be empty.");
      } else {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            new Client(username);
          }
        });
        break;
      }
      
    }
  }

}
