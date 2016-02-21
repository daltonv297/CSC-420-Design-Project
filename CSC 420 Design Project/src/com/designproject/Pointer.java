package com.designproject;

public class Pointer {
	
	private Node pointsTo;
	private int listIndex;
	
	public Pointer() {
		pointsTo = null;
	}
	
	public Pointer(Node pointsTo, int listIndex) {
		this.pointsTo = pointsTo;
		this.listIndex = listIndex;
	}
	
	public void setNode(Node node, int index) {
		pointsTo = node;
		listIndex = index;
	}
	
	public Node getNode() {
		return pointsTo;
	}
	
	public int getIndex() {
		return listIndex;
	}
}
