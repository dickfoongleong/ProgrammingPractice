import java.io.File;
import java.util.Map;

public abstract class Parser {
  public abstract Map<Long, Data[]> parseData() throws Exception;
  public abstract File saveCopy() throws Exception;
  public abstract File saveCopy(File distFile) throws Exception;

  protected static Map<Long, Data[]> dataMap = null;
  protected File orgFile;

  protected Parser(File orgFile) {
    this.orgFile = orgFile;
  }
  
  public Map<Long, Data[]> getDataMap() {
    return dataMap;
  }
  
  public void setDataMap(Map<Long, Data[]> dataMap) {
    Parser.dataMap = dataMap;
  }
}
