import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CSVParser extends Parser {
  private char delimiter;
  private char encloseChar;

  protected CSVParser(File orgFile, char delimiter, char encloseChar) {
    super(orgFile);
    this.delimiter = delimiter == '\0' ? ',' : delimiter;
    this.encloseChar = encloseChar == '\0' ? '"' : encloseChar;
  }

  @Override
  public void parseData() throws Exception {
    dataMap = new HashMap<Long, Data[]>();
    
    long counter = 1; // Rows counter
    List<Data> fieldList = new ArrayList<Data>(); // Columns/Fields Array.
    StringBuilder valueBuilder = new StringBuilder(""); // Columns/Fields Value.
    boolean inEnclosed = false; // Indicates within the enclose characters column/field.
    boolean collectChar = false; // Indicates collecting characters for column/field in enclose characters.

    FileReader fReader = new FileReader(orgFile);
    BufferedReader bReader = new BufferedReader(fReader);
    int c = 0;
    while ((c = bReader.read()) != -1) {
      char character = (char) c;
      
      if (inEnclosed) {
        collectChar = true;
        if (character == encloseChar) {
          inEnclosed = false;
        } else {
          valueBuilder.append(character);
        }
      } else {
        if (character == encloseChar) {
          inEnclosed = true;
          if (collectChar) {
            valueBuilder.append(encloseChar);
          }
        } else if (character == delimiter) {
          Data field = new Data(DataType.CHARACTER, valueBuilder.toString(), null);
          fieldList.add(field);
          
          valueBuilder = new StringBuilder("");
          collectChar = false;
        } else if (character == '\r' || character == '\n') {
          Data field = new Data(DataType.CHARACTER, valueBuilder.toString(), null);
          fieldList.add(field);
          dataMap.put(counter, fieldList.toArray(new Data[0]));
          
          counter++;
          fieldList.clear();
          valueBuilder = new StringBuilder("");
          inEnclosed = false;
          collectChar = false;
        } else {
          valueBuilder.append(character);
        }
      }
    }

    bReader.close();
  }
  
  @Override
  public File saveCopy() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public File saveCopy(File distFile) {
    // TODO Auto-generated method stub
    return null;
  }
}
