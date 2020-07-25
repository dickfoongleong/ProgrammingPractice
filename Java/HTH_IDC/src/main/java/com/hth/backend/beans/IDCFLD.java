package com.hth.backend.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.hth.backend.iSeries;
import com.hth.util.IDHeader;

public class IDCFLD {
  private static Map<String, String> idHeaderMap = null;
  private static IDHeader[] idHeaderList = null;
  
  
  public static Map<String, String> getIDHeaderMap() {
    if (idHeaderMap == null) {
      loadHeaders();
    }
    return idHeaderMap;
  }
  
  public static IDHeader[] getIDHeaderList() {
    if (idHeaderList == null) {
      loadHeaders();
    }
    return idHeaderList;
  }

  private static void loadHeaders() {

      String sql = "SELECT CFID, CFHD, CFDSC FROM HTHDATV1.IDCFLD";
      List<String[]> resultList = iSeries.executeSQL(sql);

      idHeaderMap = new HashMap<String, String>(); // Create HashMap instance.
      idHeaderList = new IDHeader[resultList.size()];
      
      String[] result;
      String key, value, desc;

      // Loop for all key-valu results and add to HashMap.
      for (int idx = 0; idx < resultList.size(); idx++) {
          result = resultList.get(idx);
          key = result[0].trim().replaceAll("@@", ""); // Replace "@@KEY@@" to "KEY".
          value = (result[1].trim().contains("*")) ? "" : result[1].trim(); // If the value contains "*", the value is ignored. Otherwise add the value to key.
          desc = result[2].trim();
          
          idHeaderMap.put(key, value); // Adding the Key-Value pair.
          idHeaderList[idx] = new IDHeader(key, value, desc);
      }
  }
}
