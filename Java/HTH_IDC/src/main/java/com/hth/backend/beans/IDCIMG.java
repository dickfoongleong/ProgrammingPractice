package com.hth.backend.beans;

import java.io.File;
import java.util.List;

import com.hth.backend.iSeries;
import com.hth.id_card.HTH_IDC;
import com.hth.util.IDLogo;

public class IDCIMG {
	private static IDLogo[] logoList = null;
	
	public static IDLogo[] getLogoList() {
		if (logoList == null) {
			refreshList();
		}
		return logoList;
	}
	
	public static void refreshList() {
		String alias = "QTEMP.IDCIMG";
		String file = "DFLIB.IDCIMG(" + HTH_IDC.member + ")";
		String sql = "SELECT INAME, IDESC FROM " + alias + " ORDER BY IDESC";
		
		List<String[]> resultList = iSeries.executeSQLByAlias(sql, alias, file);
		logoList = new IDLogo[resultList.size()];
		
		if (!resultList.isEmpty()) {
			for (int idx = 0; idx < logoList.length; idx++) {
				logoList[idx] = new IDLogo(resultList.get(idx)[0].trim(), resultList.get(idx)[1].trim());
			}
		}
	}
	
	public static void uploadFile(File inputFile, String name, String desc) {
	  String path = "/MobileApp/" + HTH_IDC.member + "/Logos/" + name + ".PNG";
	  iSeries.uploadImage(path, inputFile);
	  
	  String alias = "QTEMP.IDCIMG";
	  String file = "DFLIB.IDCIMG(" + HTH_IDC.member + ")";
	  String sql = "INSERT INTO QTEMP.IDCIMG (INAME, IDESC) VALUES ('&%" + name + "','" + desc + "')";
	  iSeries.executeSQLByAlias(sql, alias, file);
	}
}
