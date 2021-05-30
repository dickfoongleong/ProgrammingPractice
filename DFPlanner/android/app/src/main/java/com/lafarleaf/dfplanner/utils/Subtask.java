package com.lafarleaf.dfplanner.utils;

public class Subtask {
	private long id;
	
	private String title;
	
	private boolean isDone;
	
	public long getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
}
