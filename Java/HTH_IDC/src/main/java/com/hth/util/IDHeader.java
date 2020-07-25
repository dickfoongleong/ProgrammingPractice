package com.hth.util;

public class IDHeader {
  private String id;
  private String header;
  private String desc;
  
  public IDHeader(String id, String header, String desc) {
    this.id = id;
    this.header = header;
    this.desc = desc;
  }

  public String getID() {
    return id;
  }

  public void setID(String id) {
    this.id = id;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
  
}
