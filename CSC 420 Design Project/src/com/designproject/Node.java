package com.designproject;

import java.awt.Point;

public class Node {
	int value;
	int tempValue;
	int key;
	Point coord;
	Node parent;
	Node leftChild;
	Node rightChild;
	boolean visited = false;

	public Node(Node parent, int value, int key) {
		this.parent = parent;
		this.key = key;
		this.value = value;
		tempValue = value;
		coord = new Point();
	}

	public String toString() {
		return "Node with value of " + value + " has the key " + key + ", at x=" + coord.getX() + " y=" + coord.getY();
	}
}