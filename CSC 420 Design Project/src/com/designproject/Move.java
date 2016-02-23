package com.designproject;

public class Move {
	private String type = "";
	private int listIndex;
	
	public Move (String type, int index) {
		
		if (type.equals("up"))
			this.type = type;
		else if (type.equals("down"))
			this.type = type;
		else
			System.out.println("invalid move");
		
		listIndex = index;
	}
	
	public String getType() {
		return type;
	}
	
	public int getIndex() {
		return listIndex;
	}
}
