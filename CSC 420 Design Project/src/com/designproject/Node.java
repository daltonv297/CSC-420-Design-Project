package com.designproject;

import java.awt.Point;

public class Node {
	int value;
	int key;
	Point coord;
	Node parent;
	Node leftChild;
	Node rightChild;
	//boolean isLeaf;

	public Node(Node parent, int value, int key) {
		this.parent = parent;
		this.key = key;
		this.value = value;
		coord = null;
	}

	public String toString() {
		return "Node with value of " + value + " has the key " + key;
	}
}