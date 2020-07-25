package com.hth.backend.beans;

import java.util.List;

import com.hth.backend.iSeries;
import com.hth.id_card.HTH_IDC;
import com.hth.util.Division;

public class DIVISN {
	public static Division[] getDivList(String group) {
		String alias = "QTEMP.DIVISN";
	    String file = "HTHDATV1.DIVISN(" + HTH_IDC.member + ")";
	    String sql = "SELECT DDIV, DDIVNA FROM " + alias + " WHERE DGRPNO='" + group + "'";
	    List<String[]> resultList = iSeries.executeSQLByAlias(sql, alias, file);
	    
	    Division[] divList = new Division[resultList.size()];
	    String[] result;
	    String id, name;

	    for (int idx = 0; idx < divList.length; idx++) {
	      result = resultList.get(idx);
	      id = result[0].trim();
	      name = result[1].trim();
	      
	      divList[idx] = new Division(id, name);
	    }
	    
	    return divList;
	}

}
