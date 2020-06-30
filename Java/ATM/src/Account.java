import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Account {
  private AccountType accountType;
  private double balance;
  private UUID id;
  private String nickname;
  private List<Transaction> transactions;
  
  public Account(AccountType accountType, String nickname) {
    this.accountType = accountType;
    this.balance = 0.0;
    this.id = Bank.getNewAccountID();
    this.nickname = nickname;
    this.transactions = new ArrayList<Transaction>();
  }
  
  public AccountType getAccountType() {
    return accountType;
  }
  
  public double getBalance() {
    return balance;
  }
  
  public void deposit(double amount) {
    balance += amount;
  }
  
  public void withdraw(double amount) {
    balance -= amount;
  }
  
  public UUID getID() {
    return id;
  }
  
  public String getNickname() {
    return nickname;
  }
  
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }
  
  public List<Transaction> getTransactions() {
    return transactions;
  }
  
  public void addTransaction(double amount, String memo, Status status) {
    Date currentDate = Calendar.getInstance().getTime();
    Transaction transaction = new Transaction(currentDate, status, amount, memo);
    transactions.add(transaction);
  }
}
