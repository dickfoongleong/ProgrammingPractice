import java.text.Format;
import java.util.Date;

public class Data {
  private DataType type;
  private String value;
  private Format format;

  public Data(DataType type, String value, Format format) {
    this.type = type;
    this.value = value;
    this.format = format;
  }

  public DataType getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  public void setType(DataType type) {
    this.type = type;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setFormat(Format format) {
    this.format = format;
  }

  public float compareTo(Data data2) throws Exception {
    switch (type) {
      case INTEGER:
        int data1Int = format == null ? Integer.parseInt(value) : (int) format.parseObject(value);
        int data2Int = format == null ? Integer.parseInt(data2.getValue()) : (int) format.parseObject(data2.getValue());
        return data1Int - data2Int;
      case FLOAT:
        float data1Float = format == null ? Float.parseFloat(value) : (float) format.parseObject(value);
        float data2Float = format == null ? Float.parseFloat(data2.getValue()) : (float) format.parseObject(data2.getValue());
        return data1Float - data2Float;
      case DATE:
        if (format == null) {
          throw new Exception("Data/Time Format Not Specified");
        }
        Date data1Date = (Date) format.parseObject(value);
        Date data2Date = (Date) format.parseObject(data2.getValue());
        return data1Date.compareTo(data2Date);
      case CHARACTER:
      default:
        return this.value.compareTo(data2.getValue());
    }
  }
}
