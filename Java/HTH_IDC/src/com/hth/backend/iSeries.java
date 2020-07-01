package com.hth.backend;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;

public class iSeries {
  private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
  private static final String SERVER = "SERVER";
  private static final String URL = "jdbc:mysql://localhost:3306/MILLION_TEST?serverTimezone=UTC";
  private static final String HOSTNAME = "HOST";
  private static final String PASSWORD = "PASS";

  private static AS400 system = null;

  public static List<String[]> executeSQL(String sql) {
    List<String[]> resultList = new ArrayList<>();
    String[] result;
    Statement statement;
    ResultSet resultSet;
    ResultSetMetaData resultSetMetaData;
    Connection connection = null;

    try {
      Class.forName(DRIVER);
      connection = DriverManager.getConnection(URL, HOSTNAME, PASSWORD);
      statement = connection.createStatement();

      if (sql.substring(0, 6).equalsIgnoreCase("SELECT")) {
        resultSet = statement.executeQuery(sql);
        resultSetMetaData = resultSet.getMetaData();

        while (resultSet.next()) {
          result = new String[resultSetMetaData.getColumnCount()];
          for (int idx = 0; idx < result.length; idx++) {
            result[idx] = resultSet.getString(idx + 1);
          }
          resultList.add(result);
        }
      } else {
        String rowCount = Integer.toString(statement.executeUpdate(sql));
        result = new String[] {rowCount};
        resultList.add(result);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException sqle) {
        sqle.printStackTrace();
      }
    }

    return resultList;
  }

  public static List<String[]> executeSQLByAlias(String sql, String alias, String file) {
    String aliasSQL = "CREATE ALIAS " + alias + " FOR " + file;
    List<String[]> resultList = new ArrayList<>();
    String[] result;
    Statement statement;
    ResultSet resultSet;
    ResultSetMetaData resultSetMetaData;
    Connection connection = null;

    try {
      Class.forName(DRIVER);
      connection = DriverManager.getConnection(URL, HOSTNAME, PASSWORD);
      statement = connection.createStatement();
      statement.execute(aliasSQL);

      if (sql.substring(0, 6).equalsIgnoreCase("SELECT")) {
        resultSet = statement.executeQuery(sql);
        resultSetMetaData = resultSet.getMetaData();

        while (resultSet.next()) {
          result = new String[resultSetMetaData.getColumnCount()];
          for (int idx = 0; idx < result.length; idx++) {
            result[idx] = resultSet.getString(idx + 1);
          }
          resultList.add(result);
        }
      } else {
        String rowCount = Integer.toString(statement.executeUpdate(sql));
        result = new String[] {rowCount};
        resultList.add(result);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException sqle) {
        sqle.printStackTrace();
      }
    }

    return resultList;
  }

  public static void executeCL(String clCommand) {
    try {
      if (system == null) {
        system = new AS400(SERVER, HOSTNAME, PASSWORD);
      }

      CommandCall command = new CommandCall(system);
      command.run(clCommand);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void uploadImage(String path, File file) {
    try {
      if (system == null) {
        system = new AS400(SERVER, HOSTNAME, PASSWORD);
      }

      FileInputStream inputStream = new FileInputStream(file);
      
      IFSFile output = new IFSFile(system, path);
      if (!output.exists()) {
        output.createNewFile();
      }

      IFSFileOutputStream outStream = new IFSFileOutputStream (output);
      
      byte data[] = new byte[2048 * 4];
      int byteContent;
      while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
        outStream.write(data, 0, byteContent);
      }
      outStream.close();
      inputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Image[] downloadImages(String[] paths) {
    boolean isLoaded;
    String currPath = "";
    Image[] imgList = new Image[paths.length];
    Image img;

    try {
      if (system == null) {
        system = new AS400(SERVER, HOSTNAME, PASSWORD);
      }

      for (int currIdx = 0; currIdx < paths.length; currIdx++) {
        isLoaded = false;
        img = null;
        if (!paths[currIdx].trim().equals("")) {
          currPath = paths[currIdx];

          for (int prevIdx = currIdx - 1; prevIdx >= 0; prevIdx--) {
            if (paths[prevIdx].equals(currPath)) {
              img = imgList[prevIdx];
              isLoaded = true;
              break;
            }
          }

          if (!isLoaded) {
            IFSFileInputStream imgFile = new IFSFileInputStream(system, currPath);
            img = ImageIO.read(imgFile);
          }
        }
        imgList[currIdx] = img;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return imgList;
  }
}
