package com.designproject;

public class Hand {
	private Node pointsTo;
	private int listIndex;
	
	public Hand() {
		pointsTo = null;
	}
	
	public Hand(Node pointsTo, int listIndex) {
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
