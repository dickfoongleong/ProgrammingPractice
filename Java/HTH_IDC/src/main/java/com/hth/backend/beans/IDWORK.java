package com.hth.backend.beans;
import java.util.List;

import com.hth.backend.iSeries;
import com.hth.id_card.HTH_IDC;

public class IDWORK {
	private static String[] grpList = null;
	
	public static String[] getInQueueID(String usr) {
		String alias = "QTEMP.IDWORK";
		String file = "HTHDATV1.IDWORK(" + HTH_IDC.member + ")";

		String sql = "SELECT IEMPID, IGRP FROM QTEMP.IDWORK WHERE IUSER='" + usr + "'";
		List<String[]> resultList = iSeries.executeSQLByAlias(sql, alias, file);

		String[] idList = new String[resultList.size()];
		grpList = new String[resultList.size()];
		for (int idx = 0; idx < idList.length; idx++) {
			idList[idx] = resultList.get(idx)[0].trim();
			grpList[idx] = resultList.get(idx)[1].trim();
		}
		
		cleanInQueueID(usr);
		
		return idList;
	}

    public static String[] getGrpList() {
        return grpList;
    }
    
	private static void cleanInQueueID(String usr) {
		String alias = "QTEMP.IDWORK";
		String file = "HTHDATV1.IDWORK(" + HTH_IDC.member + ")";

		String sql = "DELETE FROM QTEMP.IDWORK WHERE IUSER='" + usr + "'";
		iSeries.executeSQLByAlias(sql, alias, file);
	}
}
