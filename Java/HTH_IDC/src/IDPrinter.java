import com.hth.id_card.HTH_IDC;

public class IDPrinter {
  public static void main(String[] args) {
//  String[] parm = new String[2];
//  parm[0] = args[0];
//  parm[1] = "M";
    
    if (args.length != 2) {
      System.exit(0);
    }
    HTH_IDC.begin(args);
  }
}
