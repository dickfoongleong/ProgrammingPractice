package com.hth.util;

public class IDMask {
	private String face;
	private int line;
	private int col;
	private int startIdx;
	private int endIdx;
	private String modifiers;
	
	public IDMask(String face, int line, int col, int startIdx, int endIdx, String modifiers) {
		this.face = face;
		this.line = line;
		this.col = col;
		this.startIdx = startIdx;
		this.endIdx = endIdx;
		this.modifiers = modifiers;
	}

	public String getFace() {
		return face;
	}

	public int getLine() {
		return line;
	}

	public int getCol() {
		return col;
	}

	public int getStartIdx() {
		return startIdx;
	}

	public int getEndIdx() {
		return endIdx;
	}

	public String getModifiers() {
	  return modifiers;
	}
	
	public String[] getModifierList() {
		return modifiers.split(",");
	}
	
	
}
