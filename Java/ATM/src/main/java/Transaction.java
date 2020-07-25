import java.util.Date;

public class Transaction {
  private Date date;
  private Status status;
  private double amount;
  private String memo;
  
  public Transaction(Date date, Status status, double amount, String memo) {
    this.date = date;
    this.status = status;
    this.amount = amount;
    this.memo = memo;
  }
  
  public Date getDate() {
    return date;
  }
  
  public Status getStatus() {
    return status;
  }
  
  public double getAmount() {
    return amount;
  }
  
  public String getMemo() {
    return memo;
  }
  
  public void setMemo(String memo) {
    this.memo = memo;
  }
}
