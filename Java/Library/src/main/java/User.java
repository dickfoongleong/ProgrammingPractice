
public class User {
  private int id;
  private String fName;
  private String lName;
  private String username;
  private boolean isAdmin;
  
  public User(int id, String fName, String lName, String username, boolean isAdmin) {
    this.id = id;
    this.fName = fName;
    this.lName = lName;
    this.username = username;
    this.isAdmin = isAdmin;
  }
  
  public int getID() {
    return id;
  }
  
  public String getFName() {
    return fName;
  }
  
  public String getLName() {
    return lName;
  }
  
  public String getUsername() {
    return username;
  }
  
  public boolean isAdmin() {
    return isAdmin;
  }
}
