package com.hth.backend.beans;

import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hth.backend.iSeries;
import com.hth.id_card.HTH_IDC;
import com.hth.util.IDCard;
import com.hth.util.IDMask;
import com.hth.util.enums.IDCardHeader;

public class IDCARD {
  public static IDCard[] getCardList(String group, String div) {
    String alias = "QTEMP.IDCARD";
    String file = "DFLIB.IDCARD2(" + HTH_IDC.member + ")";
    String sql = "SELECT ICRD#, ICRDDS FROM " + alias + " WHERE IGRP='" + group + "' AND IDIV='" + div + "'";
    List<String[]> resultList = iSeries.executeSQLByAlias(sql, alias, file);

    IDCard[] crdList = new IDCard[resultList.size()];
    String[] result;
    String number, description;

    for (int idx = 0; idx < crdList.length; idx++) {
      result = resultList.get(idx);
      number = result[0].trim();
      description = result[1].trim();

      crdList[idx] = new IDCard(group, div, number, description);
    }

    return crdList;
  }

  public static IDCard getCard(final String group, final String div, final String num) {
    String alias = "QTEMP.IDCARD";
    String file = "DFLIB.IDCARD2(" + HTH_IDC.member + ")";
    String sql = "SELECT ICRDDS, "
        + "IF1, IF2, IF3, IF4, IF5, IF6, IF7, IF8, IF9, IF10, IF11, IF12, IF13, IF14, IF15, IF16, IF17, IF18, "
        + "IB1, IB2, IB3, IB4, IB5, IB6, IB7, IB8, IB9, IB10, IB11, IB12, IB13, IB14, IB15, IB16, IB17, IB18, "
        + "IFM1, IFM2, IFM3, IFM4, IFM5, IFM6, IFM7, IFM8, IFM9, IFM10, IFM11, IFM12, IFM13, IFM14, IFM15, IFM16, IFM17, IFM18, "
        + "IFM1B, IFM2B, IFM3B, IFM4B, IFM5B, IFM6B, IFM7B, IFM8B, IFM9B, IFM10B, IFM11B, IFM12B, IFM13B, IFM14B, IFM15B, IFM16B, IFM17B, IFM18B, "
        + "IFM1C, IFM2C, IFM3C, IFM4C, IFM5C, IFM6C, IFM7C, IFM8C, IFM9C, IFM10C, IFM11C, IFM12C, IFM13C, IFM14C, IFM15C, IFM16C, IFM17C, IFM18C, "
        + "IBM1, IBM2, IBM3, IBM4, IBM5, IBM6, IBM7, IBM8, IBM9, IBM10, IBM11, IBM12, IBM13, IBM14, IBM15, IBM16, IBM17, IBM18, "
        + "IBM1B, IBM2B, IBM3B, IBM4B, IBM5B, IBM6B, IBM7B, IBM8B, IBM9B, IBM10B, IBM11B, IBM12B, IBM13B, IBM14B, IBM15B, IBM16B, IBM17B, IBM18B, "
        + "IBM1C, IBM2C, IBM3C, IBM4C, IBM5C, IBM6C, IBM7C, IBM8C, IBM9C, IBM10C, IBM11C, IBM12C, IBM13C, IBM14C, IBM15C, IBM16C, IBM17C, IBM18C, "
        + "IFLG1, IFLG2, IFLG3, IFLG4, IFLG5, IFLG6, IFLG7, IFLG8, IFLG9, "
        + "IBLG1, IBLG2, IBLG3, IBLG4, IBLG5, IBLG6, IBLG7, IBLG8, IBLG9, "
        + "ILARF, ILARB "
        + "FROM QTEMP.IDCARD WHERE IGRP='" + group + "' AND IDIV='" + div + "' AND ICRD#='" + num + "'";
    List<String[]> resultList = iSeries.executeSQLByAlias(sql, alias, file);

    String[] info = resultList.get(0);
    String desc = "";
    String[] frontTxt = new String[18];
    String[] backTxt = new String[18];
    String[] frontMod = new String[18];
    String[] backMod = new String[18];
    String frontLarge = "";
    String backLarge = "";
    String[] logos = new String[18];
    String[] logoVars = new String[18];
    String[] frontLogoVars = new String[9];
    String[] backLogoVars = new String[9];
    List<IDMask> frontMask = new ArrayList<>();
    List<IDMask> backMask = new ArrayList<>();

    final IDMask[][] idMaskList = new IDMask[1][1];
    Thread maskLoader = new Thread(new Runnable() {
      public void run() {
        idMaskList[0] = IDCMASK.getIdCardMask(group, div, num);
      }
    });
    maskLoader.start();

    for (int idx = 0; idx < info.length; idx++) {
      if (idx == 0) {
        desc = info[idx].trim();
      }

      // Front 18 Lines of texts.
      if (idx >= 1 && idx <= 18) {
        frontTxt[idx - 1] = info[idx].trim();
      }

      // Back 18 Lines of texts.
      if (idx >= 19 && idx <= 36) {
        backTxt[idx - 19] = info[idx].trim();
      }

      // Front 18 Modifiers of each line. (1)
      if (idx >= 37 && idx <= 54) {
        frontMod[idx - 37] = info[idx].trim();
      }

      // Front 18 Modifiers of each line. (2)
      if (idx >= 55 && idx <= 72) {
        frontMod[idx - 55] += "," + info[idx].trim();
      }

      // Front 18 Modifiers of each line. (3)
      if (idx >= 73 && idx <= 90) {
        frontMod[idx - 73] += "," + info[idx].trim();
      }

      // Back 18 Modifiers of each line. (1)
      if (idx >= 91 && idx <= 108) {
        backMod[idx - 91] = info[idx].trim();
      }

      // Back 18 Modifiers of each line. (1)
      if (idx >= 109 && idx <= 126) {
        backMod[idx - 109] += "," + info[idx].trim();
      }

      // Back 18 Modifiers of each line. (1)
      if (idx >= 127 && idx <= 144) {
        backMod[idx - 127] += "," + info[idx].trim();
      }

      // Front 9 logos.
      if (idx >= 145 && idx <= 162) {
        // If the logo is not empty, reformat the file name with *.BMP.
        if (!info[idx].trim().isEmpty()) {
          logos[idx - 145] = "/MobileApp/" + HTH_IDC.member + "/Logos/" + info[idx].trim().replace("&%", "") + ".PNG";
        } else {
          logos[idx - 145] = info[idx].trim();
        }
        logoVars[idx - 145] = info[idx].trim();
      }

      // Indicator to use large logo in the front.
      if (idx == 163) {
        frontLarge = info[idx].trim();
      }

      // Indicator to use large logo in the back.
      if (idx == 164) {
        backLarge = info[idx].trim();
      }
    }

    for (int idx = 0; idx < logoVars.length; idx++) {
      if (idx < 9) {
        frontLogoVars[idx] = logoVars[idx];
      } else {
        backLogoVars[idx - 9] = logoVars[idx];
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

    IDCard card = new IDCard(group, div, num, desc,
        frontTxt, backTxt, frontMod, backMod, frontMask.toArray(new IDMask[0]), backMask.toArray(new IDMask[0]), frontLarge, backLarge,
        frontLogoVars, backLogoVars);

    Thread newTask = new Thread(new DownloadLogoTask(card, logos));
    newTask.start();

    return card;
  }

  public static void savCard(IDCard card) {
    boolean isCardExisted = isCardExisted(card.getGroup(), card.getDiv(), card.getCardNumber());
    if (isCardExisted) {
      updateCard(card);
    } else {
      insertCard(card);
    }
  }

  public static void cpyCard(final String fromGrp, final String fromDiv, final String fromNum, 
      final String toGrp, final String toDiv, final String toNum) {
    Thread maskCopier = new Thread(new Runnable() {
      @Override
      public void run() {
        IDCMASK.cpyIdCardMask(fromGrp, fromDiv, fromNum, toGrp, toDiv, toNum);
      }
    });
    maskCopier.start();

    String alias = "QTEMP.IDCARD";
    String file = "DFLIB.IDCARD2(" + HTH_IDC.member + ")";
    String sql = "INSERT INTO QTEMP.IDCARD(IGRP, IDIV, ICRD#, ICRDDS, "
        + "IF1, IF2, IF3, IF4, IF5, IF6, IF7, IF8, IF9, IF10, IF11, IF12, IF13, IF14, IF15, IF16, IF17, IF18, "
        + "IB1, IB2, IB3, IB4, IB5, IB6, IB7, IB8, IB9, IB10, IB11, IB12, IB13, IB14, IB15, IB16, IB17, IB18, "
        + "IFM1, IFM2, IFM3, IFM4, IFM5, IFM6, IFM7, IFM8, IFM9, IFM10, IFM11, IFM12, IFM13, IFM14, IFM15, IFM16, IFM17, IFM18, "
        + "IFM1B, IFM2B, IFM3B, IFM4B, IFM5B, IFM6B, IFM7B, IFM8B, IFM9B, IFM10B, IFM11B, IFM12B, IFM13B, IFM14B, IFM15B, IFM16B, IFM17B, IFM18B, "
        + "IFM1C, IFM2C, IFM3C, IFM4C, IFM5C, IFM6C, IFM7C, IFM8C, IFM9C, IFM10C, IFM11C, IFM12C, IFM13C, IFM14C, IFM15C, IFM16C, IFM17C, IFM18C, "
        + "IBM1, IBM2, IBM3, IBM4, IBM5, IBM6, IBM7, IBM8, IBM9, IBM10, IBM11, IBM12, IBM13, IBM14, IBM15, IBM16, IBM17, IBM18, "
        + "IBM1B, IBM2B, IBM3B, IBM4B, IBM5B, IBM6B, IBM7B, IBM8B, IBM9B, IBM10B, IBM11B, IBM12B, IBM13B, IBM14B, IBM15B, IBM16B, IBM17B, IBM18B, "
        + "IBM1C, IBM2C, IBM3C, IBM4C, IBM5C, IBM6C, IBM7C, IBM8C, IBM9C, IBM10C, IBM11C, IBM12C, IBM13C, IBM14C, IBM15C, IBM16C, IBM17C, IBM18C, "
        + "IFLG1, IFLG2, IFLG3, IFLG4, IFLG5, IFLG6, IFLG7, IFLG8, IFLG9, "
        + "IBLG1, IBLG2, IBLG3, IBLG4, IBLG5, IBLG6, IBLG7, IBLG8, IBLG9, "
        + "ILARF, ILARB) "
        + "SELECT '" + toGrp + "','" + toDiv + "','" + toNum + "',"
        + "ICRDDS, "
        + "IF1, IF2, IF3, IF4, IF5, IF6, IF7, IF8, IF9, IF10, IF11, IF12, IF13, IF14, IF15, IF16, IF17, IF18, "
        + "IB1, IB2, IB3, IB4, IB5, IB6, IB7, IB8, IB9, IB10, IB11, IB12, IB13, IB14, IB15, IB16, IB17, IB18, "
        + "IFM1, IFM2, IFM3, IFM4, IFM5, IFM6, IFM7, IFM8, IFM9, IFM10, IFM11, IFM12, IFM13, IFM14, IFM15, IFM16, IFM17, IFM18, "
        + "IFM1B, IFM2B, IFM3B, IFM4B, IFM5B, IFM6B, IFM7B, IFM8B, IFM9B, IFM10B, IFM11B, IFM12B, IFM13B, IFM14B, IFM15B, IFM16B, IFM17B, IFM18B, "
        + "IFM1C, IFM2C, IFM3C, IFM4C, IFM5C, IFM6C, IFM7C, IFM8C, IFM9C, IFM10C, IFM11C, IFM12C, IFM13C, IFM14C, IFM15C, IFM16C, IFM17C, IFM18C, "
        + "IBM1, IBM2, IBM3, IBM4, IBM5, IBM6, IBM7, IBM8, IBM9, IBM10, IBM11, IBM12, IBM13, IBM14, IBM15, IBM16, IBM17, IBM18, "
        + "IBM1B, IBM2B, IBM3B, IBM4B, IBM5B, IBM6B, IBM7B, IBM8B, IBM9B, IBM10B, IBM11B, IBM12B, IBM13B, IBM14B, IBM15B, IBM16B, IBM17B, IBM18B, "
        + "IBM1C, IBM2C, IBM3C, IBM4C, IBM5C, IBM6C, IBM7C, IBM8C, IBM9C, IBM10C, IBM11C, IBM12C, IBM13C, IBM14C, IBM15C, IBM16C, IBM17C, IBM18C, "
        + "IFLG1, IFLG2, IFLG3, IFLG4, IFLG5, IFLG6, IFLG7, IFLG8, IFLG9, "
        + "IBLG1, IBLG2, IBLG3, IBLG4, IBLG5, IBLG6, IBLG7, IBLG8, IBLG9, "
        + "ILARF, ILARB "
        + "FROM QTEMP.IDCARD WHERE IGRP='" + fromGrp + "' AND IDIV='" + fromDiv + "' AND ICRD#='" + fromNum + "'";
    iSeries.executeSQLByAlias(sql, alias, file);

    try {
      maskCopier.join();	
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void delCard(final String group, final String div, final String num) {
    Thread maskRemover = new Thread(new Runnable() {
      @Override
      public void run() {
        IDCMASK.delIdCardMask(group, div, num);
      }
    });
    maskRemover.start();

    String alias = "QTEMP.IDCARD";
    String file = "DFLIB.IDCARD2(" + HTH_IDC.member + ")";
    String sql = "DELETE FROM QTEMP.IDCARD WHERE IGRP='" + group + "' AND IDIV='" + div + "' AND ICRD#='" + num + "'";
    iSeries.executeSQLByAlias(sql, alias, file);

    try {
      maskRemover.join();	
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean isCardExisted(String group, String div, String num) {
    String alias = "QTEMP.IDCARD";
    String file = "DFLIB.IDCARD2(" + HTH_IDC.member + ")";
    String sql = "SELECT * FROM QTEMP.IDCARD WHERE IGRP='" + group + "' AND IDIV='" + div + "' AND ICRD#='" + num + "'";
    List<String[]> resultList = iSeries.executeSQLByAlias(sql, alias, file);

    return !resultList.isEmpty();
  }

  public static String mergeVar(String lineTxt) {
    Pattern pattern = Pattern.compile("@@[A-Z0-9#]+@@"); // Compile the regular expression to search for ID-Variables.
    Matcher matcher = pattern.matcher(lineTxt); // Apply the expression to the line.
    List<String> varList = new ArrayList<>(); // List of variables. 
    while (matcher.find()) {
      varList.add(matcher.group());
    }

    String mergedTxt = lineTxt; // Copy of the original line to modify.
    String mergedVal; // Value of the variable.
    for (String var : varList) {
      mergedVal = assignVal(var.replaceAll("@@", "")); // Assign value to the variable. 
      mergedTxt = mergedTxt.replace(var, mergedVal.trim()); // Replace the variable with the value into the copied line.
    }

    return mergedTxt.trim(); // Return the modified.
  }

  private static void updateCard(final IDCard card) {
    delCard(card.getGroup(), card.getDiv(), card.getCardNumber());
    insertCard(card);
  }

  private static void insertCard(final IDCard card) {
    Thread maskModifier = new Thread(new Runnable() {
      @Override
      public void run() {
        IDCMASK.updateIdCardMask(card.getGroup(), card.getDiv(), card.getCardNumber(), card.getFrontMask(), card.getBackMask());
      }
    });
    maskModifier.start();

    String alias = "QTEMP.IDCARD";
    String file = "DFLIB.IDCARD2(" + HTH_IDC.member + ")";

    final String[] values = new String[3];

    Thread textBuilder = new Thread(new Runnable() {
      @Override
      public void run() {
        String[] lines = card.getFrontTexts();
        String line = lines[0].replaceAll("'", "''");
        StringBuilder textValues = new StringBuilder("'").append(line).append("'");
        for (int idx = 1; idx < lines.length; idx++) {
          line = lines[idx].replaceAll("'", "''");
          textValues.append(",'").append(line).append("'");
        }

        lines = card.getBackTexts();
        for (String l : lines) {
          line = l.replaceAll("'", "''");
          textValues.append(",'").append(line).append("'");
        }

        values[0] = textValues.toString().trim();
      }
    });
    textBuilder.start();

    Thread modBuilder = new Thread(new Runnable() {
      @Override
      public void run() {
        StringBuilder fontMod = new StringBuilder("'");
        StringBuilder sizeMod = new StringBuilder("'");
        StringBuilder colorMod = new StringBuilder("'");
        String[] mods = card.getFrontTextModifiers();

        String part;
        String[] parts = mods[0].split(",");
        if (parts.length >= 1) {
          for (int pIdx = 0; pIdx < parts.length; pIdx++) {
            part = parts[pIdx];
            if (part.equals("center") || part.equals("left") || part.equals("right")
                || part.equals("b") || part.equals("u") || part.equals("i")) {
              fontMod.append(part).append(",");
            } else if (part.equals("small") || part.equals("large") || part.equals("normal")) {
              sizeMod.append(part).append(",");
            } else if (part.equals("RED") || part.equals("BLUE") || part.equals("GREEN")
                || part.equals("BLACK") || part.equals("WHITE")
                || part.startsWith("#") || part.startsWith("BG#")) {
              colorMod.append(part).append(",");
            }
          }
        }

        if (fontMod.toString().endsWith(",")) {
          fontMod = fontMod.replace(fontMod.length() - 1, fontMod.length(), "'");
        } else {
          fontMod.append("'");
        }
        if (sizeMod.toString().endsWith(",")) {
          sizeMod = sizeMod.replace(sizeMod.length() - 1, sizeMod.length(), "'");
        } else {
          sizeMod.append("'");	
        }
        if (colorMod.toString().endsWith(",")) {
          colorMod = colorMod.replace(colorMod.length() - 1, colorMod.length(), "'");
        } else {
          colorMod.append("'");	
        }

        for (int mIdx = 1; mIdx < mods.length; mIdx++) {
          fontMod.append(",'");
          sizeMod.append(",'");
          colorMod.append(",'");

          parts = mods[mIdx].split(",");
          if (parts.length >= 1) {
            for (int pIdx = 0; pIdx < parts.length; pIdx++) {
              part = parts[pIdx];
              if (part.equals("center") || part.equals("left") || part.equals("right")
                  || part.equals("b") || part.equals("u") || part.equals("i")) {
                fontMod.append(part).append(",");
              } else if (part.equals("small") || part.equals("large") || part.equals("normal")) {
                sizeMod.append(part).append(",");
              } else if (part.equals("RED") || part.equals("BLUE") || part.equals("GREEN")
                  || part.equals("BLACK") || part.equals("WHITE")
                  || part.startsWith("#") || part.startsWith("BG#")) {
                colorMod.append(part).append(",");
              }
            }
          }

          if (fontMod.toString().endsWith(",")) {
            fontMod = fontMod.replace(fontMod.length() - 1, fontMod.length(), "'");
          } else {
            fontMod.append("'");
          }
          if (sizeMod.toString().endsWith(",")) {
            sizeMod = sizeMod.replace(sizeMod.length() - 1, sizeMod.length(), "'");
          } else {
            sizeMod.append("'");	
          }
          if (colorMod.toString().endsWith(",")) {
            colorMod = colorMod.replace(colorMod.length() - 1, colorMod.length(), "'");
          } else {
            colorMod.append("'");	
          }
        }
        values[1] = fontMod.toString() + "," + sizeMod.toString() + "," + colorMod.toString();

        fontMod = new StringBuilder("'");
        sizeMod = new StringBuilder("'");
        colorMod = new StringBuilder("'");
        mods = card.getBackTextModifiers();
        parts = mods[0].split(",");
        if (parts.length >= 1) {
          for (int pIdx = 0; pIdx < parts.length; pIdx++) {
            part = parts[pIdx];
            if (part.equals("center") || part.equals("left") || part.equals("right")
                || part.equals("b") || part.equals("u") || part.equals("i")) {
              fontMod.append(part).append(",");
            } else if (part.equals("small") || part.equals("large") || part.equals("normal")) {
              sizeMod.append(part).append(",");
            } else if (part.equals("RED") || part.equals("BLUE") || part.equals("GREEN")
                || part.equals("BLACK") || part.equals("WHITE")
                || part.startsWith("#") || part.startsWith("BG#")) {
              colorMod.append(part).append(",");
            }
          }
        }

        if (fontMod.toString().endsWith(",")) {
          fontMod = fontMod.replace(fontMod.length() - 1, fontMod.length(), "'");
        } else {
          fontMod.append("'");
        }
        if (sizeMod.toString().endsWith(",")) {
          sizeMod = sizeMod.replace(sizeMod.length() - 1, sizeMod.length(), "'");
        } else {
          sizeMod.append("'");	
        }
        if (colorMod.toString().endsWith(",")) {
          colorMod = colorMod.replace(colorMod.length() - 1, colorMod.length(), "'");
        } else {
          colorMod.append("'");	
        }

        for (int mIdx = 1; mIdx < mods.length; mIdx++) {
          fontMod.append(",'");
          sizeMod.append(",'");
          colorMod.append(",'");

          parts = mods[mIdx].split(",");
          if (parts.length >= 1) {
            for (int pIdx = 0; pIdx < parts.length; pIdx++) {
              part = parts[pIdx];
              if (part.equals("center") || part.equals("left") || part.equals("right")
                  || part.equals("b") || part.equals("u") || part.equals("i")) {
                fontMod.append(part).append(",");
              } else if (part.equals("small") || part.equals("large") || part.equals("normal")) {
                sizeMod.append(part).append(",");
              } else if (part.equals("RED") || part.equals("BLUE") || part.equals("GREEN")
                  || part.equals("BLACK") || part.equals("WHITE")
                  || part.startsWith("#") || part.startsWith("BG#")) {
                colorMod.append(part).append(",");
              }
            }
          }

          if (fontMod.toString().endsWith(",")) {
            fontMod = fontMod.replace(fontMod.length() - 1, fontMod.length(), "'");
          } else {
            fontMod.append("'");
          }
          if (sizeMod.toString().endsWith(",")) {
            sizeMod = sizeMod.replace(sizeMod.length() - 1, sizeMod.length(), "'");
          } else {
            sizeMod.append("'");	
          }
          if (colorMod.toString().endsWith(",")) {
            colorMod = colorMod.replace(colorMod.length() - 1, colorMod.length(), "'");
          } else {
            colorMod.append("'");	
          }
        }
        values[1] = values[1] + "," + fontMod.toString() + "," + sizeMod.toString() + "," + colorMod.toString();
      }
    });
    modBuilder.start();

    Thread logoBuilder = new Thread(new Runnable() {
      @Override
      public void run() {
        String[] vars = card.getFrontLogoVars();
        String var = vars[0].toUpperCase();
        StringBuilder logoValues = new StringBuilder("'").append(var).append("'");
        for (int idx = 1; idx < vars.length; idx++) {
          var = vars[idx].toUpperCase();
          logoValues.append(",'").append(var).append("'");
        }

        vars = card.getBackLogoVars();
        for (String v : vars) {
          var = v.toUpperCase();
          logoValues.append(",'").append(var).append("'");
        }

        values[2] = logoValues.toString().trim();
      }
    });
    logoBuilder.start();

    try {
      textBuilder.join();
      modBuilder.join();
      logoBuilder.join();

      String sql = "INSERT INTO QTEMP.IDCARD(IGRP, IDIV, ICRD#, ICRDDS, "
          + "IF1, IF2, IF3, IF4, IF5, IF6, IF7, IF8, IF9, IF10, IF11, IF12, IF13, IF14, IF15, IF16, IF17, IF18, "
          + "IB1, IB2, IB3, IB4, IB5, IB6, IB7, IB8, IB9, IB10, IB11, IB12, IB13, IB14, IB15, IB16, IB17, IB18, "
          + "IFM1, IFM2, IFM3, IFM4, IFM5, IFM6, IFM7, IFM8, IFM9, IFM10, IFM11, IFM12, IFM13, IFM14, IFM15, IFM16, IFM17, IFM18, "
          + "IFM1B, IFM2B, IFM3B, IFM4B, IFM5B, IFM6B, IFM7B, IFM8B, IFM9B, IFM10B, IFM11B, IFM12B, IFM13B, IFM14B, IFM15B, IFM16B, IFM17B, IFM18B, "
          + "IFM1C, IFM2C, IFM3C, IFM4C, IFM5C, IFM6C, IFM7C, IFM8C, IFM9C, IFM10C, IFM11C, IFM12C, IFM13C, IFM14C, IFM15C, IFM16C, IFM17C, IFM18C, "
          + "IBM1, IBM2, IBM3, IBM4, IBM5, IBM6, IBM7, IBM8, IBM9, IBM10, IBM11, IBM12, IBM13, IBM14, IBM15, IBM16, IBM17, IBM18, "
          + "IBM1B, IBM2B, IBM3B, IBM4B, IBM5B, IBM6B, IBM7B, IBM8B, IBM9B, IBM10B, IBM11B, IBM12B, IBM13B, IBM14B, IBM15B, IBM16B, IBM17B, IBM18B, "
          + "IBM1C, IBM2C, IBM3C, IBM4C, IBM5C, IBM6C, IBM7C, IBM8C, IBM9C, IBM10C, IBM11C, IBM12C, IBM13C, IBM14C, IBM15C, IBM16C, IBM17C, IBM18C, "
          + "IFLG1, IFLG2, IFLG3, IFLG4, IFLG5, IFLG6, IFLG7, IFLG8, IFLG9, "
          + "IBLG1, IBLG2, IBLG3, IBLG4, IBLG5, IBLG6, IBLG7, IBLG8, IBLG9, "
          + "ILARF, ILARB) VALUES ("
          + "'" + card.getGroup() + "','" + card.getDiv() + "','" + card.getCardNumber() + "','" + card.getDescription().toUpperCase() + "',"
          + values[0] + "," + values[1] + "," + values[2] + ","
          + "'" + card.getFrontLarge() + "','" + card.getBackLarge() + "')";
      iSeries.executeSQLByAlias(sql, alias, file);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      maskModifier.join();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static String assignVal(String varCode) {
    Map<String, String> idHeaderMap = IDCFLD.getIDHeaderMap();
    
    String headerTxt = idHeaderMap.get(varCode); // Header of the value.
    IDCardHeader header = IDCardHeader.getHeader(varCode); // Header constant of the variable.

    // Switch-case on ID card header constant.
    switch (header) {
      case AREA: // Area covered.
        return headerTxt;
      case COINSURANCE_IN_NETWORK: // Coinsurance/Out of pocket maximum on In-Network plan.
      case COINSURANCE_OUT_NETWORK: // Coinsurance/Out of pocket maximum on Out-Network plan.
      case COPAY: // Copay of the first plan.
      case COPAY_ER: // Copay on ER of the first plan.
      case COPAY_HOSPITAL: // Copay on Hospital of the first plan.
      case COPAY_OFFICE_VISIT: // Copay on Offive Visit of the first plan.
      case DEDUCTIBLE_IN_NETWORK: // Deductible maximum on In-Network plan.
      case DEDUCTIBLE_OUT_NETWORK: // Deductible maximum on Out-Network plan.
        return headerTxt + " $9999.99";
      case COVERAGE_TYPE: // Coverage type of the first plan.
      case DENT_COVERAGE: // Coverage type of Dental plan.
      case MED_COVERAGE: // Coverage type on Medical plan.
      case VIS_COVERAGE: // Coverage type for Vision plan.
        return headerTxt + " SINGLE";
      case DENT_PLAN: // Name of dental plan.
        return headerTxt + " DENTAL PLAN NAME";
      case DEPENDENT_1: // Dependent 1 information: Full name, birthday, effective date, employee id.
      case DEPENDENT_2: // Dependent 2 information: Full name, birthday, effective date, employee id.
      case DEPENDENT_3: // Dependent 3 information: Full name, birthday, effective date, employee id.
      case DEPENDENT_4: // Dependent 4 information: Full name, birthday, effective date, employee id.
      case DEPENDENT_5: // Dependent 5 information: Full name, birthday, effective date, employee id.
      case DEPENDENT_6: // Dependent 6 information: Full name, birthday, effective date, employee id.
        String depInfo = "DEPENDENT_NAME 01/01/1990 01/01/2020 DEPENDENT_ID";
        return headerTxt + " " + depInfo;
      case DEPENDENT_1_DOB: // Dependent 1 date of birth.
      case DEPENDENT_2_DOB: // Dependent 2 date of birth.
      case DEPENDENT_3_DOB: // Dependent 3 date of birth.
      case DEPENDENT_4_DOB: // Dependent 4 date of birth.
      case DEPENDENT_5_DOB: // Dependent 5 date of birth.
      case DEPENDENT_6_DOB: // Dependent 6 date of birth.
      case DEPENDENT_7_DOB: // Dependent 7 date of birth.
      case DEPENDENT_8_DOB: // Dependent 8 date of birth.
      case DEPENDENT_9_DOB: // Dependent 9 date of birth.
      case DEPENDENT_10_DOB: // Dependent 10 date of birth.
        String depDOB = "MARY DOE 01/01/1990";
        return headerTxt + " " + depDOB;
      case DEPENDENT_1_PCD: // Dependent 1 primary care doctor.
      case DEPENDENT_2_PCD: // Dependent 2 primary care doctor.
      case DEPENDENT_3_PCD: // Dependent 3 primary care doctor.
      case DEPENDENT_4_PCD: // Dependent 4 primary care doctor.
      case DEPENDENT_5_PCD: // Dependent 5 primary care doctor.
      case DEPENDENT_6_PCD: // Dependent 6 primary care doctor.
      case DEPENDENT_7_PCD: // Dependent 7 primary care doctor.
      case DEPENDENT_8_PCD: // Dependent 8 primary care doctor.
      case DEPENDENT_9_PCD: // Dependent 9 primary care doctor.
      case DEPENDENT_10_PCD: // Dependent 10 primary care doctor.
        return headerTxt + " SP-MARY PCP-Dr.JACKY";
      case DEPENDENT_NAME: // First dependent name
        return headerTxt + " MARY DOE";
      case DIV_NAME: // Division name.
        return headerTxt + " DIVISION NAME";
      case DIV_NUM: // Division number.
        return headerTxt + " DIV";
      case EFF_DATE: // Effective Date.
        return headerTxt + " 01/01/2020";
      case EFF_DATE_MMM_FORMAT: // Effective date in MMM.DD.YY format.
        return headerTxt + " Jan.01.20";
      case EFF_DATE_YYYY_FORMAT: // Effective date in YYYY/MM/DD format
        return headerTxt + " 2020/01/01";
      case ELECTED_COUNTRY: // Elected country.
        return headerTxt + " US";
      case EMP_DOB: // Employee date of birth.
        return headerTxt + " 01/01/1990";
      case EMP_NAME: // Employee name.
        return headerTxt + " JOHN DOE";
      case EMP_PCD: // Employee primary care doctor.
        return headerTxt + " EE-JOHN DOE PCP-Dr.JACKY";
      case GRP_NAME: // Group name.
        return headerTxt + " GROUP NAME";
      case GRP_NUM: // Group number.
        return headerTxt + " GROUP ID";
      case GRP_NUM_DIV: // Group number and Division number.
        return headerTxt + " GROUP ID - DIV";
      case INTERNATIONAL_DOB: // Insure date of birth in DD/MM/YYYY format.
        return headerTxt + " 01/01/1990";
      case MED_PLAN: // Name of Medical plan.
        return headerTxt + " MEDICAL PLAN NAME";
      case MED_PLAN_CODE: // Plan code of the Medical plan.
        return headerTxt + " MEDICAL PLAN CODE";
      case MEM_ID: // Employee ID.
        return headerTxt + " MEMBER_ID";
      case MEM_ID_GGH: // Employee ID with GGH prefix.
        return headerTxt + " GGH MEMBER_ID";
      case NETWORK: // In-Network Vendor name.
        return headerTxt + " IN NETWORK VENDOR NAME";
      case ORIG_EFF_DATE: // Original effective date.
        return headerTxt + " 01/01/20";
      case PLAN_1: // Plan 1 information: Name, Effective Date, Coverage Type.
      case PLAN_2: // Plan 2 information: Name, Effective Date, Coverage Type.
      case PLAN_3: // Plan 3 information: Name, Effective Date, Coverage Type.
      case PLAN_4: // Plan 4 information: Name, Effective Date, Coverage Type.
      case PLAN_5: // Plan 5 information: Name, Effective Date, Coverage Type.
      case PLAN_6: // Plan 6 information: Name, Effective Date, Coverage Type.
        return headerTxt + " PLAN NAME/01/01/20/SINGLE";
      case PLAN_ID: // Plan code.
        return headerTxt + " PLAN ID";
      case POLICY_ID: // Policy ID.
        return headerTxt + " POLICY ID";
      case POLICY_ID_9: // First 9 characters of policy ID.
        return headerTxt + " FIRST 9 POLICY ID";
      case POLICY_ID_10: // First 10 characters of policy ID.
        return headerTxt + " FIRST 10 POLICY ID";
      case POLICY_ID_EL_MEM_ID: // If policy ID exists, use policy ID; Else use Employee ID.
        return headerTxt + " POLICY ID";
      case PPO_ADD_1: // Vendor Address 1.
      case PPO_ADD_2: // Vendor Address 2.
      case PPO_ADD_3: // Vendor Address 3.
      case PPO_ADD_4: // Vendor Address 4.
      case PPO_ADD_5: // Vendor Address 5.
      case PPO_ADD_6: // Vendor Address 6.
        return headerTxt + " VENDOR ADDRESS";
      case PPO_CONTACT: // Vendor contact.
        return headerTxt + " VENDOR CONTACT";
      case PPO_EMAIL: // Vendor email.
        return headerTxt + " VENDOR EMAIL";
      case PPO_FAX: // Vendor fax.
        return headerTxt + " VENDOR FAX";
      case PPO_PHONE: // Vendor phone.
        return headerTxt + " VENDOR PHONE";
      case PPO_TAX_ID: // Vendor tax ID.
        return headerTxt + " VENDOR TAX ID";
      case PPO_VENDOR_NAME: // Vendor name.
        return headerTxt + " VENDOR NAME";
      case SEX: // Gender.
        return headerTxt + " M";
      case SSN: // SSN.
        return headerTxt + " EMPLOYEE ID";
      case TERM_DATE: // Termination date.
        return headerTxt + " 03/31/20";
      case TODAY_DATE: // Current date.
        String today = new SimpleDateFormat("MM/dd/yy").format(Calendar.getInstance().getTime());
        return headerTxt + " " + today;
      default: // Not found.
    } // End of Switch-Case.

    if (headerTxt == null) {
      return varCode;
    } else {
      return headerTxt;
    }
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
