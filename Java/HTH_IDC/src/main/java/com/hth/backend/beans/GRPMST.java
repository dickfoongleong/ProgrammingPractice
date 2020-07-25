package com.hth.backend.beans;

import java.util.List;
import com.hth.backend.iSeries;
import com.hth.id_card.HTH_IDC;
import com.hth.util.GroupMaster;
import com.hth.util.enums.GroupStatus;

public class GRPMST {
  public static GroupMaster[] getGroupList() {
    String alias = "QTEMP.GRPMST";
    String file = "HTHDATV1.GRPMST(" + HTH_IDC.member + ")";
    String sql = "SELECT GGRPNO, GVIPST, GGRPNM, GCARR, GSTAT FROM " + alias + " ORDER BY GGRPNM";
    List<String[]> resultList = iSeries.executeSQLByAlias(sql, alias, file);
    
    GroupMaster[] groupList = new GroupMaster[resultList.size()];
    String[] result;
    String id, name, carrier;
    boolean isVIP;
    GroupStatus status;
    for (int idx = 0; idx < groupList.length; idx++) {
      result = resultList.get(idx);
      id = result[0].trim();
      isVIP = result[1].trim().equals("Y");
      name = result[2].trim();
      carrier = result[3].trim();
      status = GroupStatus.getStatus(result[4].trim());
      
      groupList[idx] = new GroupMaster(id, name, carrier, isVIP, status);
    }
    
    return groupList;
  }
}
