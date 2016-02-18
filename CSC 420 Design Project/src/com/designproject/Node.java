package com.designproject;

import java.awt.Color;
import java.awt.Point;

public class Node {
	Color color;
	int key;
	Point coord;
	Node parent;
	Node leftChild;
	Node rightChild;
	boolean isLeaf;

	public Node(Node parent, Color color, int key) {
		this.parent = parent;
		this.color = color;
		this.key = key;
		coord = null;
		//this.name = name;
	}
	
	public Node(Node parent, Color color) {
		this.parent = parent;
		this.color = color;
		coord = null;
	}

	


	public String toString() {
		return color + " node has the key " + key;
		/*
		 * * return name + " has the key " + key + "\nLeft Child: " + leftChild
		 * + "\nRight Child: " + rightChild + "\n";
		 */
	}
}