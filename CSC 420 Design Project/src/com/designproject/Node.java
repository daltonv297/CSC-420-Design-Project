package com.designproject;

import java.awt.Color;

public class Node {
	Color color;
	int key;
	//String name;
	Node leftChild;
	Node rightChild;

	public Node(Color color, int key) {
		this.color = color;
		this.key = key;
		//this.name = name;
	}

	


	public String toString() {
		return color + " node has the key " + key;
		/*
		 * * return name + " has the key " + key + "\nLeft Child: " + leftChild
		 * + "\nRight Child: " + rightChild + "\n";
		 */
	}
}