package com.panpen.family.houseiot.utils;

public class User {
	private String id;
	private String fName;
	private String lName;
	
	public User(String id, String fName, String lName) {
		this.id = id;
		this.fName = fName;
		this.lName = lName;
	}
	
	public String getID() {
		return id;
	}
	
	public String getFName() {
		return fName;
	}
	
	public String getLName() {
		return lName;
	}
}
