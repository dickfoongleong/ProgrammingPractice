package com.hth.id_card;

import java.util.List;
import javax.swing.JOptionPane;
import com.hth.backend.iSeries;
import com.hth.backend.beans.IDCPRV;
import com.hth.backend.beans.GRPMST;
import com.hth.backend.beans.IDWORK;
import com.hth.id_card.user_interface.decorator.ID_Decorator;
import com.hth.id_card.user_interface.printer.ID_Printer;
import com.hth.util.GroupMaster;
import com.hth.util.IDCard;

public class HTH_IDC {
  /**
   * Device code.
   */
  private static final String DEVICE = "PRT";

  /**
   * Member code.
   */
  public static String member = "";

  /**
   * HTH Printer window frame.
   */
  private static ID_Printer printerWindow = null;
  
  /**
   * HTH Decorator window frame.
   */
  private static ID_Decorator decoratorWindow = null;

  /**
   * Configure the member code by the given user ID.
   * 
   * @param usrID
   *        User ID whom running the ID program.
   *        
   * @return
   *        Member code found in iSeries.
   */
  private static String config(String usrID) {
    String sql = "SELECT USRMBR FROM HTHDATV1.SYSUSRP WHERE USRID='" + usrID.toUpperCase() + "'";
    List<String[]> resultList = iSeries.executeSQL(sql);
    if (resultList.isEmpty()) {
      return "   ";
    } else {
      return resultList.get(0)[0].trim();
    }
  }

  /**
   * Entry point of the ID card program.
   * 
   * @param args
   * 		[0] User ID
   * 		[1] Flag indicates Maintenance (M) or Printer (P).
   */
  public static void begin(String[] args) {
    // Check if the arguments are passed correctly.
    if (args.length != 2) {
      System.exit(0);
    }

    // Assign passed arguments.
    String userID = args[0].trim();
    String flag = args[1].trim();

    // Configure member code.
    member = config(userID); // "BW1";
    
    if (flag.equalsIgnoreCase("M")) {
      // TODO: Change Data Loading.
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          decoratorWindow = new ID_Decorator();
        }
      });
      
      GroupMaster[] groupList = GRPMST.getGroupList();

      // Wait up to 2 minutes if window is not ready.
      int timer = 0;
      while (decoratorWindow == null && timer <= 120) {
        try {
          Thread.sleep(1000);
          timer++;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      // If window is not loaded, show error message for invalid parameters; Otherwise starts ID program.
      if (decoratorWindow == null) {
        JOptionPane.showInternalMessageDialog(null, "Invalid parameters passed.", "Error Occurred", JOptionPane.ERROR_MESSAGE);
      } else {
        decoratorWindow.begin(groupList);
      }
    } else if (flag.equalsIgnoreCase("P")) {
      // Loading ID numbers from database queue.
      String[] idList = IDWORK.getInQueueID(userID); // new String[] {"235336390", "402789016", "233198953"}; // new String[] {"003565315", "012521333", "237359347", "036607230"};
      String[] grpList = IDWORK.getGrpList(); // new String[] {"ALLIANCE", "000WJS834", "MORGANTOWN"}; // new String[] {"ARCB", "ARCB", "FLT", "FLT"};  
      String clCommand = "CALL DFLIB/LOADIDC PARM('" + member + "' '" + DEVICE + "' '%s')";
      
      // Check if there exists any ID to print.
      if (idList.length == 0) {
        // No ID in queue for printing.
        System.exit(0);
      } else {
        // Initial UI while data is loading.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            // Printer program.
            printerWindow = new ID_Printer();
          }
        });

        // Compile CL statement to create 100 ID cards at a time.
        StringBuilder idValue = new StringBuilder(idList[0]);
        for (int idx = 1; idx < idList.length; idx++) {
          if (idx % 100 == 0) {
            while (idValue.length() < 1100) {
              idValue.append(" ");
            }
            iSeries.executeCL(String.format(clCommand, idValue));
            idValue = new StringBuilder(idList[idx]);
          } else {
            idValue.append("\\&").append(idList[idx]);
          }
        }

        // Finish the left over ID cards.
        while (idValue.length() < 1100) {
          idValue.append(" ");
        }
        iSeries.executeCL(String.format(clCommand, idValue));
        
        // Query out the ID cards that created for printing.
        IDCard[] cardList = IDCPRV.generateIDCARD(grpList, idList, DEVICE);

        // Wait up to 2 minutes if window is not ready.
        int timer = 0;
        while (printerWindow == null && timer <= 120) {
          try {
            Thread.sleep(1000);
            timer++;
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        // If window is not loaded, show error message for invalid parameters; Otherwise starts ID program.
        if (printerWindow == null) {
          JOptionPane.showInternalMessageDialog(null, "Invalid parameters passed.", "Error Occurred", JOptionPane.ERROR_MESSAGE);
        } else {
          printerWindow.begin(cardList);
        }
      }
    }

  }
}
