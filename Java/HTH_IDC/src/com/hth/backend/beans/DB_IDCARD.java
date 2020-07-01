package com.hth.backend.beans;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import com.hth.backend.iSeries;
import com.hth.id_card.HTH_IDC;
import com.hth.util.IDCard;
import com.hth.util.IDMask;

public class DB_IDCARD {
	public static IDCard[] generateIDCARD(String[] grpList, String[] ssnList, String device) {
		String member = HTH_IDC.member;
		IDCard[] idCardList = new IDCard[0];

		String alias = "QTEMP.IDCPRV";
		String file = "DFLIB.IDCPRV(" + member + ")";
		String sql;
		List<String[]> resultList;

		StringBuilder whereClause = new StringBuilder("(");
		whereClause.append("(PGRP='").append(grpList[0]).append("' AND PSSN='").append(ssnList[0]).append("')");
		for (int idx = 1; idx < grpList.length; idx++) {
			whereClause.append(" OR (PGRP='").append(grpList[idx]).append("' AND PSSN='").append(ssnList[idx]).append("')");
		}
		whereClause.append(")");

		sql = "SELECT IGRP, IDIV, ICRD#, PLARF, PLARB, " // {idx 0 ~ 4}
				+ "PFL01, PFL02, PFL03, PFL04, PFL05, PFL06, PFL07, PFL08, PFL09, PFL10, PFL11, PFL12, PFL13, PFL14, PFL15, PFL16, PFL17, PFL18, " // {idx 5 ~ 22}
				+ "PBL01, PBL02, PBL03, PBL04, PBL05, PBL06, PBL07, PBL08, PBL09, PBL10, PBL11, PBL12, PBL13, PBL14, PBL15, PBL16, PBL17, PBL18, " // {idx 23 ~ 40}
				+ "PFM01, PFM02, PFM03, PFM04, PFM05, PFM06, PFM07, PFM08, PFM09, PFM10, PFM11, PFM12, PFM13, PFM14, PFM15, PFM16, PFM17, PFM18, " // {idx 41 ~ 58}
				+ "PBM01, PBM02, PBM03, PBM04, PBM05, PBM06, PBM07, PBM08, PBM09, PBM10, PBM11, PBM12, PBM13, PBM14, PBM15, PBM16, PBM17, PBM18, " // {idx 59 ~ 76}
				+ "PFLG1, PFLG2, PFLG3, PFLG4, PFLG5, PFLG6, PFLG7, PFLG8, PFLG9, " // {idx 77 ~ end}
				+ "PBLG1, PBLG2, PBLG3, PBLG4, PBLG5, PBLG6, PBLG7, PBLG8, PBLG9 "
				+ "FROM " + alias + " WHERE PDEV='" + device + "' AND " + whereClause
				+ " ORDER BY PGRP, PDIV, PLNAM, PFNAM";
		resultList = iSeries.executeSQLByAlias(sql, alias, file);

		if (!resultList.isEmpty()) {
			idCardList = new IDCard[resultList.size()];
			for (int rsIdx = 0; rsIdx < resultList.size(); rsIdx++) {
				String[] idCardInfo = resultList.get(rsIdx);

				final String iGrp = idCardInfo[0];
                final String iDiv = idCardInfo[1];
                final String iCrdNum = idCardInfo[2];
                String frontLarge = idCardInfo[3].trim();
                String backLarge = idCardInfo[4].trim();
                String[] frontTexts = new String[18];
                String[] backTexts = new String[18];
                String[] frontTextModifiers = new String[18];
                String[] backTextModifiers = new String[18];
                String[] logos = new String[18];
                List<IDMask> frontMask = new ArrayList<>();
                List<IDMask> backMask = new ArrayList<>();

                final IDMask[][] idMaskList = new IDMask[1][1];
                Thread maskLoader = new Thread(new Runnable() {
                	public void run() {
                		idMaskList[0] = IDCMASK.getIdCardMask(iGrp, iDiv, iCrdNum);
                	}
                });
                maskLoader.start();

                for (int idx = 5; idx < idCardInfo.length; idx++) {
                    // Front texts.
                    if (idx <= 22) {
                        frontTexts[idx - 5] = idCardInfo[idx].trim();
                    }

                    // Back texts.
                    if (idx >= 23 && idx <= 40) {
                        backTexts[idx - 23] = idCardInfo[idx].trim();
                    }

                    // Front text modifiers.
                    if (idx >= 41 && idx <= 58) {
                        frontTextModifiers[idx - 41] = idCardInfo[idx].trim();
                    }

                    // Back text modifiers.
                    if (idx >= 59 && idx <= 76) {
                        backTextModifiers[idx - 59] += "," + idCardInfo[idx].trim();
                    }

                    // Logos
                    if (idx >= 77 && idx <= 94) {
                        if (!idCardInfo[idx].trim().equals("")) {
                            logos[idx - 77] = "/MobileApp/" + member + "/Logos/" + idCardInfo[idx].trim();
                        } else {
                            logos[idx - 77] = idCardInfo[idx].trim();
                        }
                    }
                }
                
                try {
                	maskLoader.join();
                } catch (InterruptedException ie) {
                	ie.printStackTrace();
                }
                
                for (IDMask mask : idMaskList[0]) {
                	if (mask.getFace().equals("F")) {
                		frontMask.add(mask);
                	} else {
                		backMask.add(mask);
                	}
                }

                IDCard idCard = new IDCard(frontTexts, backTexts, frontTextModifiers, backTextModifiers, frontMask.toArray(new IDMask[0]), backMask.toArray(new IDMask[0]), frontLarge, backLarge);
                Thread newTask = new Thread(new DownloadLogoTask(idCard, logos));
                newTask.start();
                
                idCardList[rsIdx] = idCard;
			}
		}

		clearIDCARD(grpList, ssnList, device);

		return idCardList;
	}

	private static void clearIDCARD(String[] grpList, String[] ssnList, String device) {
		String alias = "QTEMP.IDCPRV";
		String file = "DFLIB.IDCPRV(" + HTH_IDC.member + ")";
		String sql;

		StringBuilder whereClause = new StringBuilder("(");
		whereClause.append("(PGRP='").append(grpList[0]).append("' AND PSSN='").append(ssnList[0]).append("')");
		for (int idx = 1; idx < grpList.length; idx++) {
			whereClause.append(" OR (PGRP='").append(grpList[idx]).append("' AND PSSN='").append(ssnList[idx]).append("')");
		}
		whereClause.append(")");

		sql = "DELETE FROM " + alias + " WHERE PDEV='" + device + "' AND " + whereClause;
		iSeries.executeSQLByAlias(sql, alias, file);
	}

	private static class DownloadLogoTask implements Runnable {
		IDCard idCard;
		String[] logoPaths;

		DownloadLogoTask(IDCard idCard, String[] logoPaths) {
			this.idCard = idCard;
			this.logoPaths = logoPaths;
		}

		@Override
		public void run() {
			Image[] logos = iSeries.downloadImages(logoPaths);
			Image[] frontLogos = new Image[9];
			Image[] backLogos = new Image[9];

			for (int idx = 0; idx < logos.length; idx++) {
				if (idx <9) {
					frontLogos[idx] = logos[idx];
				} else {
					backLogos[idx - 9] = logos[idx];
				}
			}

			idCard.setFrontLogo(frontLogos);
			idCard.setBackLogo(backLogos);
			idCard.changeReadyState(true);
		}
	}
}
