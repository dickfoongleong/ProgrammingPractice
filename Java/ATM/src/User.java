import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User {
  private static MessageDigest md;
  static {
    try {
      md = MessageDigest.getInstance("MD5");
    }catch (NoSuchAlgorithmException nsae) {
      nsae.printStackTrace();
    }
  }

  private String username;
  private byte[] password;
  private Map<UUID, Account> accountList;
  private String fName;
  private String lName;

  public User(String username, String password, String fName, String lName) {
    this.username = username;
    this.accountList = new HashMap<UUID, Account>();
    this.fName = fName;
    this.lName = lName;
    this.password = md.digest(password.getBytes());
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public byte[] getPassowrd() {
    return password;
  }

  public void setPassword(String password) {
    this.password = md.digest(password.getBytes());
  }
  
  public boolean isVerified(String passIn) {
    byte[] passInBytes = md.digest(passIn.getBytes());
    return Arrays.equals(password, passInBytes);
  }
  
  public Map<UUID, Account> getAccountList() {
    return accountList;
  }
  
  public Account getAccount(UUID id) {
    return accountList.get(id);
  }
  
  public void addAccount(Account account) {
    accountList.put(account.getID(), account);
  }
  
  public void removeAccount(Account account) {
    accountList.remove(account.getID());
  }
  
  public String getFName() {
    return fName;
  }
  
  public void setFName(String fName) {
    this.fName = fName;
  }
  
  public String getLName() {
    return lName;
  }
  
  public void setLName(String lName) {
    this.lName = lName;
  }
}
