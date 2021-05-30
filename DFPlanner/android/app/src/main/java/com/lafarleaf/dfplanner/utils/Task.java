package com.lafarleaf.dfplanner.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {
	private String code;
	
	private String title;
	
	private Date dueDate;
	
	private String color;
	
	private boolean isDone;
	
	private List<Subtask> subtasks = new ArrayList<>();
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Date getDueDate() {
		return dueDate;
	}
	
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
	
	public List<Subtask> getSubtaskList() {
		return subtasks;
	}
	
	public void setSubtaskList(List<Subtask> subtasks) {
		this.subtasks = subtasks;
	}
	
	public void addSubtask(Subtask subtask) {
		subtasks.add(subtask);
	}
}
