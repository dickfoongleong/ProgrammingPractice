package com.hth.util.enums;

public enum GroupStatus {
  RUN_OUT("X"),
  TERMINATED("T"),
  ACTIVE("A");
  
private final String code;
  
  GroupStatus(String code) {
    this.code = code;
  }
  
  public static GroupStatus getStatus(String code) {
    for (GroupStatus status : GroupStatus.values()) {
      if (status.code.equals(code)) {
        return status;
      }
    }
    
    return ACTIVE;
  }
}
