package com.hth.backend.beans;
import java.util.List;

import com.hth.backend.iSeries;
import com.hth.id_card.HTH_IDC;
import com.hth.util.IDMask;

public class IDCMASK {
  public static IDMask[] getIdCardMask(String grp, String div, String cardNum) {
    String alias = "QTEMP.IDCMASK";
    String file = "DFLIB.IDCMASK(" + HTH_IDC.member + ")";
    String sql;
    List<String[]> resultList;

    sql = "SELECT IFACE, ILINE, ICOL, ISTR, IEND, IMOD FROM " + alias + " WHERE IGRP='" + grp + "' AND IDIV='" + div + "' AND ICRD#='" + cardNum + "'";
    resultList = iSeries.executeSQLByAlias(sql, alias, file);

    IDMask[] masks = new IDMask[0];

    if (!resultList.isEmpty()) {
      masks = new IDMask[resultList.size()];

      String face, mod;
      int line, col, start, end;

      String[] result;
      for (int idx = 0; idx < resultList.size(); idx++) {
        result = resultList.get(idx);

        face = result[0].trim();
        line = Integer.parseInt(result[1].trim());
        col = Integer.parseInt(result[2].trim());
        start = Integer.parseInt(result[3].trim());
        end = Integer.parseInt(result[4].trim());
        mod = result[5].trim();

        masks[idx] = new IDMask(face, line, col, start, end, mod);
      }
    }

    return masks;
  }

  public static void cpyIdCardMask(String fromGrp, String fromDiv, String fromNum, 
      String toGrp, String toDiv, String toNum) {
    String alias = "QTEMP.IDCMASK";
    String file = "DFLIB.IDCMASK(" + HTH_IDC.member + ")";
    String sql = "INSERT INTO QTEMP.IDCMASK(IGRP, IDIV, ICRD#, IFACE, ILINE, ICOL, ISTR, IEND, IMOD) "
        + "SELECT '" + toGrp + "','" + toDiv + "','" + toNum + "',"
        + "IFACE, ILINE, ICOL, ISTR, IEND, IMOD "
        + "FROM QTEMP.IDCMASK WHERE IGRP='" + fromGrp + "' AND IDIV='" + fromDiv + "' AND ICRD#='" + fromNum + "'";
    iSeries.executeSQLByAlias(sql, alias, file);
  }

  public static void delIdCardMask(String grp, String div, String cardNum) {
    String alias = "QTEMP.IDCMASK";
    String file = "DFLIB.IDCMASK(" + HTH_IDC.member + ")";
    String sql = "DELETE FROM " + alias + " WHERE IGRP='" + grp + "' AND IDIV='" + div + "' AND ICRD#='" + cardNum + "'";
    iSeries.executeSQLByAlias(sql, alias, file);
  }

  public static void updateIdCardMask(String grp, String div, String num, IDMask[] frontMasks, IDMask[] backMasks) {
    delIdCardMask(grp, div, num);
    insertIdCardMask(grp, div, num, frontMasks, backMasks);
  }

  private static void insertIdCardMask(final String grp, final String div, final String num, final IDMask[] frontMasks, final IDMask[] backMasks) {
    String alias = "QTEMP.IDCMASK";
    String file = "DFLIB.IDCMASK(" + HTH_IDC.member + ")";

    final StringBuilder[] maskList = new StringBuilder[2];
    
    Thread frontLoader = new Thread(new Runnable() {
      @Override
      public void run() {
        if (frontMasks.length >= 1) {
          IDMask mask = frontMasks[0];
          
          StringBuilder values = new StringBuilder("('").append(grp).append("','").append(div).append("','").append(num).append("','")
          .append(mask.getFace()).append("','").append(mask.getLine()).append("','").append(mask.getCol()).append("','")
          .append(mask.getStartIdx()).append("','").append(mask.getEndIdx()).append("','").append(mask.getModifiers()).append("')");
          
          for (int idx = 1; idx < frontMasks.length; idx++) {
            mask = frontMasks[idx];
            values.append(",('").append(grp).append("','").append(div).append("','").append(num).append("','")
            .append(mask.getFace()).append("','").append(mask.getLine()).append("','").append(mask.getCol()).append("','")
            .append(mask.getStartIdx()).append("','").append(mask.getEndIdx()).append("','").append(mask.getModifiers()).append("')");
          }
          
          maskList[0] = values;
        } else {
          maskList[0] = null;
        }
      }
    });
    frontLoader.start();
    
    Thread backLoader = new Thread(new Runnable() {
      @Override
      public void run() {
        if (backMasks.length >= 1) {
          IDMask mask = backMasks[0];
          
          StringBuilder values = new StringBuilder("('").append(grp).append("','").append(div).append("','").append(num).append("','")
          .append(mask.getFace()).append("','").append(mask.getLine()).append("','").append(mask.getCol()).append("','")
          .append(mask.getStartIdx()).append("','").append(mask.getEndIdx()).append("','").append(mask.getModifiers()).append("')");
          
          for (int idx = 1; idx < backMasks.length; idx++) {
            mask = backMasks[idx];
            values.append(",('").append(grp).append("','").append(div).append("','").append(num).append("','")
            .append(mask.getFace()).append("','").append(mask.getLine()).append("','").append(mask.getCol()).append("','")
            .append(mask.getStartIdx()).append("','").append(mask.getEndIdx()).append("','").append(mask.getModifiers()).append("')");
          }
          
          maskList[1] = values;
        } else {
          maskList[1] = null;
        }
      }
    });
    backLoader.start();
    
    try {
      frontLoader.join();
      backLoader.join();

      if (maskList[0] != null || maskList[1] != null) {
        String values = "";
        if (maskList[0] == null ) {
          values = maskList[1].toString().trim();
        } else if (maskList[1] == null) {
          values = maskList[0].toString().trim();
        } else {
          values = maskList[0].toString().trim() + "," + maskList[1].toString().trim();
        }
        
        String sql = "INSERT INTO QTEMP.IDCMASK(IGRP, IDIV, ICRD#, IFACE, ILINE, ICOL, ISTR, IEND, IMOD) VALUES " + values.toString();
        iSeries.executeSQLByAlias(sql, alias, file);  
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
