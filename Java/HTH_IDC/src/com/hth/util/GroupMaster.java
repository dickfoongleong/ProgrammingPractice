package com.hth.util;

import com.hth.util.enums.GroupStatus;

public class GroupMaster {
  private String id;
  private String name;
  private String carrier;
  private boolean isVIP;
  private GroupStatus status;

  public GroupMaster(String id, String name, String carrier, boolean isVIP, GroupStatus status) {
    this.id = id;
    this.name = name;
    this.carrier = carrier;
    this.isVIP = isVIP;
    this.status = status;
  }

  public String getID() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCarrier() {
    return carrier;
  }

  public boolean isVIP() {
    return isVIP;
  }

  public GroupStatus getStatus() {
    return status;
  }
  
}
