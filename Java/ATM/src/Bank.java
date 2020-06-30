import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Bank {
  public static final String NAME = "Loy Loy Bank (LLB)";
  private static Map<String, User> users = new HashMap<String, User>();
  private static Map<UUID, Account> accounts = new HashMap<UUID, Account>();
  
  public static UUID getNewAccountID() {
    UUID newID;
    
    do {
      newID = UUID.randomUUID();
    } while (accounts.containsKey(newID));
    
    return newID;
  }
  
  public static void addUser(User user) {
    users.put(user.getUsername(), user);
  }
  
  public static void removeUser(User user) {
    users.remove(user.getUsername());
  }
  
  public static void addAccount(Account account) {
    accounts.put(account.getID(), account);
  }
  
  public static void removeAccount(Account account) {
    accounts.remove(account.getID());
  }
  
  public static User getUser(String username) {
    return users.get(username);
  }
  
  public static Account getAccount(UUID id) {
    return accounts.get(id);
  }
}
