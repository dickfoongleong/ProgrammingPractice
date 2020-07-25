import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class ATM extends JFrame implements WindowListener {
  /**
   * Serial Version ID.
   */
  private static final long serialVersionUID = 1L;

  private static final Dimension FRAME_SIZE = new Dimension(1000, 800);

  private static final NumberFormat US_DOLLAR_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);
  
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE MM/dd/yyyy - hh:mm a");

  public ATM() {
    addWindowListener(this);
    setTitle(Bank.NAME + " - ATM");
    setIcon();
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setResizable(false);
    setPreferredSize(FRAME_SIZE);
    setMaximumSize(FRAME_SIZE);
    setMinimumSize(FRAME_SIZE);
    relocateWindow();

    showSignOnScreen();
    setVisible(true);
  }

  public void setIcon() {
    try {
      URL imgURL = ATM.class.getResource("AppIcon.png");
      Image img =  ImageIO.read(imgURL);

      String os = System.getProperty("os.name").toLowerCase();
      if (os.indexOf("mac") >= 0) {
        Taskbar.getTaskbar().setIconImage(img);
      } else {
        setIconImage(img);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showSignOnScreen() {
    JLayeredPane screen = new JLayeredPane();
    screen.setOpaque(true);
    screen.setBackground(Color.WHITE);

    JLabel titleLabel = new JLabel("Welcome to Loy Loy Bank ATM service.");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setForeground(Color.BLUE);
    titleLabel.setBounds(FRAME_SIZE.width / 2 - 170, 150, 340, 30);
    screen.add(titleLabel);

    JLabel usernameLabel = new JLabel("Username:");
    usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    usernameLabel.setBounds(FRAME_SIZE.width / 2 - 116, 200, 80, 30);
    screen.add(usernameLabel);

    final JTextField usernameTF = new JTextField();
    usernameTF.setFont(new Font("Arial", Font.PLAIN, 14));
    usernameTF.setBounds(FRAME_SIZE.width / 2 - 45, 200, 150, 30);
    screen.add(usernameTF);

    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    passwordLabel.setBounds(FRAME_SIZE.width / 2 - 116, 240, 80, 30);
    screen.add(passwordLabel);

    final JPasswordField passwordField = new JPasswordField();
    passwordField.setBounds(FRAME_SIZE.width / 2 - 45, 240, 150, 30);
    screen.add(passwordField);

    JButton loginBtn = new JButton("Login");
    loginBtn.setFont(new Font("Arial", Font.PLAIN, 14));
    loginBtn.setBounds(FRAME_SIZE.width / 2 - 124, 280, 232, 30);
    loginBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        login(usernameTF.getText(), new String(passwordField.getPassword()));
      }
    });
    screen.add(loginBtn);

    JButton createBtn = new JButton("Create New User");
    createBtn.setFont(new Font("Arial", Font.PLAIN, 14));
    createBtn.setBounds(FRAME_SIZE.width / 2 - 124, 310, 232, 30);
    createBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        create(usernameTF.getText(), new String(passwordField.getPassword()));
      }
    });
    screen.add(createBtn);

    setContentPane(screen);
    refresh();
  }

  private void viewMainMenu(User user) {
    JPanel screen = new JPanel();
    screen.setOpaque(true);
    screen.setBackground(Color.WHITE);
    screen.setLayout(new BorderLayout());

    JLabel titleLabel = new JLabel("Hello, " + user.getFName() + "!");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    titleLabel.setForeground(Color.BLUE);
    screen.add(titleLabel, BorderLayout.NORTH);

    JScrollPane mainScrollPane = new JScrollPane();
    mainScrollPane.setOpaque(false);
    mainScrollPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(3, 3, 3, 3), 
        BorderFactory.createLineBorder(Color.GRAY, 1, true)));
    screen.add(mainScrollPane, BorderLayout.CENTER);

    JPanel btnPanel = new JPanel();
    btnPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
    btnPanel.setOpaque(false);
    screen.add(btnPanel, BorderLayout.SOUTH);

    showAccountSummary(user, mainScrollPane, btnPanel);

    setContentPane(screen);
    refresh();
  }

  private void showAccountSummary(User user, JScrollPane mainScrollPane, JPanel btnPanel) {
    JPanel accountPanel = new JPanel();
    accountPanel.setOpaque(false);
    accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
    mainScrollPane.setViewportView(accountPanel);

    Map<UUID, Account> accountList = user.getAccountList();
    for (UUID id : accountList.keySet()) {
      final Account account = accountList.get(id);
      JPanel summaryPanel = getSummaryPanel(account);
      summaryPanel.addMouseListener(new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent evt) {
          viewAccountDetail(user, account);
        }

        @Override
        public void mousePressed(MouseEvent evt) {
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
        }

        @Override
        public void mouseEntered(MouseEvent evt) {
        }

        @Override
        public void mouseExited(MouseEvent evt) {
        }
      });
      accountPanel.add(summaryPanel);
    }

    JButton addAccBtn = new JButton("Add New Account");
    addAccBtn.setFont(new Font("Arial", Font.PLAIN, 14));
    addAccBtn.setAlignmentX(CENTER_ALIGNMENT);
    addAccBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        addAccount(user);
      }
    });
    accountPanel.add(addAccBtn);

    btnPanel.removeAll();
    JButton transferBtn = new JButton("Transfer");
    transferBtn.setFont(new Font("Arial", Font.PLAIN, 14));
    transferBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        transfer(user);
      }
    });
    btnPanel.add(transferBtn);

    JButton logoutBtn = new JButton("Exit");
    logoutBtn.setFont(new Font("Arial", Font.PLAIN, 14));
    logoutBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        showSignOnScreen();
      }
    });
    btnPanel.add(logoutBtn);
  }
  
  private void viewAccountDetail(User user, Account account) {
    JPanel screen = new JPanel();
    screen.setOpaque(true);
    screen.setBackground(Color.WHITE);
    screen.setLayout(new BorderLayout());

    JPanel headingPanel = new JPanel();
    headingPanel.setOpaque(false);
    headingPanel.setLayout(new GridLayout(2, 1, 0, 0));
    screen.add(headingPanel, BorderLayout.NORTH);
    
    JPanel titlePanel = new JPanel();
    titlePanel.setOpaque(false);
    titlePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
    headingPanel.add(titlePanel);
    
    JLabel titleLabel = new JLabel(account.getNickname());
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 1, 2));
    titlePanel.add(titleLabel);
    
    JLabel typeLabel = new JLabel("Account Type: " + account.getAccountType().toString());
    typeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
    typeLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 1, 5));
    typeLabel.setForeground(Color.BLUE);
    titlePanel.add(typeLabel);
    
    JLabel amountLabel = new JLabel("Balance: " + US_DOLLAR_FORMAT.format(account.getBalance()));
    amountLabel.setFont(new Font("Arial", Font.BOLD, 18));
    amountLabel.setBorder(BorderFactory.createEmptyBorder(1, 25, 2, 2));
    headingPanel.add(amountLabel);

    JScrollPane mainScrollPane = new JScrollPane();
    mainScrollPane.setOpaque(false);
    mainScrollPane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(3, 3, 3, 3), 
        BorderFactory.createLineBorder(Color.GRAY, 1, true)));
    screen.add(mainScrollPane, BorderLayout.CENTER);

    JPanel btnPanel = new JPanel();
    btnPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
    btnPanel.setOpaque(false);
    screen.add(btnPanel, BorderLayout.SOUTH);

    showAccountDetail(user, account, mainScrollPane, btnPanel);

    setContentPane(screen);
    refresh();
  }

  private JPanel getSummaryPanel(Account account) {
    JPanel panel = new JPanel();
    panel.setOpaque(false);
    panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(1, 2, 1, 2), 
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true)));
    panel.setLayout(new BorderLayout());
    panel.setPreferredSize(new Dimension(FRAME_SIZE.width - 8, 60));
    panel.setMaximumSize(new Dimension(FRAME_SIZE.width - 8, 60));
    panel.setMinimumSize(new Dimension(FRAME_SIZE.width - 8, 60));

    JLabel nicknameLabel = new JLabel(account.getNickname() + " (" + account.getAccountType().toString() + ")");
    nicknameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
    nicknameLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
    panel.add(nicknameLabel, BorderLayout.CENTER);

    JLabel amountLabel = new JLabel(US_DOLLAR_FORMAT.format(account.getBalance()));
    amountLabel.setFont(new Font("Arial", Font.BOLD, 18));
    amountLabel.setBorder(BorderFactory.createEmptyBorder(0 , 1, 0, 2));
    panel.add(amountLabel, BorderLayout.EAST);

    return panel;
  }
  
  private JPanel getSummaryPanel(Transaction transaction) {
    JPanel panel = new JPanel();
    panel.setOpaque(true);
    panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(1, 2, 1, 2), 
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true)));
    panel.setLayout(new BorderLayout());
    panel.setPreferredSize(new Dimension(FRAME_SIZE.width - 8, 60));
    panel.setMaximumSize(new Dimension(FRAME_SIZE.width - 8, 60));
    panel.setMinimumSize(new Dimension(FRAME_SIZE.width - 8, 60));
    
    switch (transaction.getStatus()) {
      case COMPLETED:
        panel.setBackground(new Color(214, 255, 218));
        break;
      case IN_PROGRESS:
        panel.setBackground(new Color(251, 255, 209));
        break;
      case DENIED:
        panel.setBackground(new Color(247, 193, 198));
        break;
      default:
        panel.setBackground(Color.WHITE);
    }
    
    JPanel infoPanel = new JPanel();
    infoPanel.setOpaque(false);
    infoPanel.setLayout(new GridLayout(2, 1, 0, 0));
    panel.add(infoPanel, BorderLayout.CENTER);
    
    JLabel dateLabel = new JLabel(DATE_FORMAT.format(transaction.getDate()));
    dateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
    dateLabel.setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 0));
    infoPanel.add(dateLabel);
    
    JLabel memoLabel = new JLabel(transaction.getMemo());
    memoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    memoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 1, 0));
    infoPanel.add(memoLabel);
    
    JLabel amountLabel = new JLabel(US_DOLLAR_FORMAT.format(transaction.getAmount()));
    amountLabel.setFont(new Font("Arial", Font.BOLD, 18));
    amountLabel.setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));
    panel.add(amountLabel, BorderLayout.EAST);
    
    return panel;
  }

  private void showAccountDetail(User user , Account account, JScrollPane mainScrollPane, JPanel btnPanel) {
    JPanel accountPanel = new JPanel();
    accountPanel.setOpaque(false);
    accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
    mainScrollPane.setViewportView(accountPanel);

    List<Transaction> transactions = account.getTransactions();
    for (Transaction transaction : transactions) {
      JPanel summaryPanel = getSummaryPanel(transaction);
      accountPanel.add(summaryPanel);
    }

    btnPanel.removeAll();
    JButton depositBtn = new JButton("Deposit");
    depositBtn.setFont(new Font("Arial", Font.PLAIN, 14));
    depositBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        deposit(user, account);
      }
    });
    btnPanel.add(depositBtn);
    
    JButton withdrawBtn = new JButton("Withdraw");
    withdrawBtn.setFont(new Font("Arial", Font.PLAIN, 14));
    withdrawBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        withdraw(user, account);
      }
    });
    btnPanel.add(withdrawBtn);
    

    JButton backBtn = new JButton("Back");
    backBtn.setFont(new Font("Arial", Font.PLAIN, 14));
    backBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        viewMainMenu(user);
      }
    });
    btnPanel.add(backBtn);
  }
  
  private void login(String username, String password) {
    if (username.trim().isEmpty() || password.trim().isEmpty()) {
      String msg = "";

      if (username.trim().isEmpty()) {
        msg += "Username cannot be blank.\n";
      }

      if (password.trim().isEmpty()) {
        msg += "Password cannot be blank.";
      }
      JOptionPane.showMessageDialog(this, msg.trim());
      return;
    }

    User user = Bank.getUser(username);
    if (user == null) {
      JOptionPane.showMessageDialog(this, "User not found.");
    } else if (!user.isVerified(password)) {
      JOptionPane.showMessageDialog(this, "Passowrd Incorrect.");
    } else {
      viewMainMenu(user);
    }
  }

  private void create(String username, String password) {
    if (username.trim().isEmpty() || password.trim().isEmpty()) {
      String msg = "";

      if (username.trim().isEmpty()) {
        msg += "Username cannot be blank.\n";
      }

      if (password.trim().isEmpty()) {
        msg += "Password cannot be blank.";
      }
      JOptionPane.showMessageDialog(this, msg.trim());
      return;
    }

    User user = Bank.getUser(username);
    if (user == null) {
      String fName = "";
      String lName = "";

      while (fName.trim().isEmpty()) {
        fName = JOptionPane.showInputDialog(this, "Enter your first name: (Cannot be blank)");
        if (fName == null) {
          return;
        }
      }

      while (lName.trim().isEmpty()) {
        lName = JOptionPane.showInputDialog(this, "Enter your last name: (Cannot be blank)");
        if (lName == null) {
          return;
        }
      }

      user = new User(username, password, fName, lName);
      Bank.addUser(user);
      JOptionPane.showMessageDialog(this, "Account is created. You may log in.");
    } else {
      JOptionPane.showMessageDialog(this, "Username is created. Please try again with a different username.");
    }
  }

  private void addAccount(User user) {
    AccountType type = (AccountType) JOptionPane.showInputDialog(this, "Choose one of the following:", "Account Type",
        JOptionPane.PLAIN_MESSAGE, null, AccountType.values(), AccountType.CHECKING);
    if (type == null) {
      return;
    }

    String nickname = "";
    while (nickname.trim().isEmpty()) {
      nickname = JOptionPane.showInputDialog(this, "Enter nickname for this account: (Cannot be blank)");
      if (nickname == null) {
        return;
      }
    }

    Account account = new Account(type, nickname);
    Bank.addAccount(account);
    user.addAccount(account);
    viewMainMenu(user);
  }

  private void transfer(User user) {
    Map<UUID, Account> accList = user.getAccountList();

    if (accList.size() >= 2) {
      Account accFrom, accTo;
      double amount = 0.0;

      // From account.
      String[] accNames = new String[accList.size()];
      UUID[] idList = accList.keySet().toArray(new UUID[0]);

      for (int idx = 0; idx < idList.length; idx++) {
        Account acc = accList.get(idList[idx]);
        accNames[idx] = (idx + 1)
            + "-" + acc.getNickname() + " (" + acc.getAccountType().toString() + ")"
            + "-" + US_DOLLAR_FORMAT.format(acc.getBalance());
      }

      String option = (String) JOptionPane.showInputDialog(this, "Choose the account to transfer from:", "Transfer From", 
          JOptionPane.PLAIN_MESSAGE, null, accNames, accNames[0]);
      if (option == null) {
        return;
      }
      
      accFrom = user.getAccount(idList[Integer.parseInt(option.split("-")[0]) - 1]);

      // To account.
      accNames = new String[accList.size() - 1];
      UUID[] filteredIDs = new UUID[accList.size() - 1];
      for (int idx = 0, counter = 0; idx < idList.length; idx++) {
        if (!idList[idx].equals(accFrom.getID())) {
          Account acc = accList.get(idList[idx]);
          accNames[counter] = (counter + 1) 
              + "-" + acc.getNickname() + " (" + acc.getAccountType().toString() + ")"
              + "-" + US_DOLLAR_FORMAT.format(acc.getBalance());

          filteredIDs[counter] = acc.getID();
          counter++;
        }
      }

      option = (String) JOptionPane.showInputDialog(this,
          "From: " + accFrom.getNickname() + "-" + US_DOLLAR_FORMAT.format(accFrom.getBalance()) + "\nChoose the account transfer to:", 
          "Transfer To",
          JOptionPane.PLAIN_MESSAGE, null, accNames, accNames[0]);
      if (option == null) {
        return;
      }

      accTo = user.getAccount(filteredIDs[Integer.parseInt(option.split("-")[0]) - 1]);
      
      // Amount to transfer.
      while (amount <= 0) {
        String amtInput = JOptionPane.showInputDialog(this,
            "From:\t" + accFrom.getNickname() + "-" + US_DOLLAR_FORMAT.format(accFrom.getBalance())
            + "\nTo:\t" + accTo.getNickname() + "-" + US_DOLLAR_FORMAT.format(accTo.getBalance())
            + "\nEnter the amount to transfer ($):",
            "Transfer Amount",
            JOptionPane.PLAIN_MESSAGE);
        if (amtInput == null) {
          return;
        }
        
        try {
          amount = Double.parseDouble(amtInput);

          if (amount <= 0) {
            JOptionPane.showMessageDialog(this, "Please enter at least $0.01. Thank you.");
          }
        } catch (Exception e) {
          amount = 0;
          JOptionPane.showMessageDialog(this, "Incorrect amount entered. Please try again.");
        }
      }
      
      if (amount > accFrom.getBalance()) {
        JOptionPane.showMessageDialog(this, "Insufficient funds! Please check your balance.");
      } else {
        accFrom.withdraw(amount);
        accFrom.addTransaction(-1 * amount, "Transfer to " + accTo.getNickname(), Status.COMPLETED);
        
        accTo.deposit(amount);
        accTo.addTransaction(amount, "Transfer from " + accFrom.getNickname(), Status.COMPLETED);
        
        user.addAccount(accFrom);
        user.addAccount(accTo);
        
        Bank.addAccount(accFrom);
        Bank.addAccount(accTo);
        viewMainMenu(user);
      }
    } else {
      JOptionPane.showMessageDialog(this, "Please create more accounts in LLB. Thank you.");
    }
  }

  private void deposit(User user, Account account) {
    double amount = 0;
    while (amount <= 0) {
      String amtInput = JOptionPane.showInputDialog(this,
          "Enter your deposit amount ($):",
          "Deposit",
          JOptionPane.PLAIN_MESSAGE);
      if (amtInput == null) {
        return;
      }
      
      try {
        amount = Double.parseDouble(amtInput);

        if (amount <= 0) {
          JOptionPane.showMessageDialog(this, "Please enter at least $0.01. Thank you.");
        }
      } catch (Exception e) {
        amount = 0;
        JOptionPane.showMessageDialog(this, "Incorrect amount entered. Please try again.");
      }
    }

    account.deposit(amount);
    account.addTransaction(amount, "Cash deposit.", Status.IN_PROGRESS);
    
    user.addAccount(account);
    Bank.addAccount(account);
    
    viewAccountDetail(user, account);
  }
  
  private void withdraw(User user, Account account) {
    double amount = 0;
    while (amount <= 0) {
      String amtInput = JOptionPane.showInputDialog(this,
          "Enter your deposit amount ($):",
          "Deposit",
          JOptionPane.PLAIN_MESSAGE);
      if (amtInput == null) {
        return;
      }
      
      try {
        amount = Double.parseDouble(amtInput);

        if (amount <= 0) {
          JOptionPane.showMessageDialog(this, "Please enter at least $0.01. Thank you.");
        }
      } catch (Exception e) {
        amount = 0;
        JOptionPane.showMessageDialog(this, "Incorrect amount entered. Please try again.");
      }
    }

    if (amount > account.getBalance()) {
      JOptionPane.showMessageDialog(this, "Insufficient funds! Please check your balance.");
    } else {
      account.withdraw(amount);
      account.addTransaction(-1 * amount, "Cash withdrawal.", Status.COMPLETED);

      user.addAccount(account);
      Bank.addAccount(account);
      viewAccountDetail(user, account);
    }
  }
  
  private void refresh() {
    revalidate();
    repaint();
  }

  private void relocateWindow() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int scMidWidth = screenSize.width / 2;
    int scMidHeight = screenSize.height / 2;
    int frameMidWidth = this.getSize().width / 2;
    int frameMidHeight = this.getSize().height / 2;
    setLocation(scMidWidth - frameMidWidth, scMidHeight - frameMidHeight);
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
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new ATM();
      }
    });
  }
}
