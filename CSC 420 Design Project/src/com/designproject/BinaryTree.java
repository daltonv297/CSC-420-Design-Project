package com.designproject;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class BinaryTree {

	private Node root;
	private boolean isRoot = false;

	public BinaryTree() {
		
		//root = new Node(null, Color.ORANGE, 50);
		//root.leftChild = new Node(root, Color.BLUE, 25);
		//root.rightChild = new Node(root, Color.YELLOW, 75);
		//root.leftChild.rightChild = new Node(root.leftChild, Color.RED, 15);
		
		generateTree(3);
		
	}

	public ArrayList<ArrayList<Node>> levelOrder(Node root) {
	    ArrayList<ArrayList<Node>> listOfLevels = new ArrayList<ArrayList<Node>>();
	    ArrayList<Node> nodes = new ArrayList<Node>();
	    if(root == null)
	        return listOfLevels;
	 
	    LinkedList<Node> current = new LinkedList<Node>();
	    LinkedList<Node> next = new LinkedList<Node>();
	    current.add(root);
	 
	    while(!current.isEmpty()){
	        Node node = current.remove();
	 
	        if(node.leftChild != null)
	            next.add(node.leftChild);
	        if(node.rightChild != null)
	            next.add(node.rightChild);
	 
	        nodes.add(node);
	        if(current.isEmpty()){
	            current = next;
	            next = new LinkedList<Node>();
	            listOfLevels.add(nodes);
	            nodes = new ArrayList<Node>();
	        }
	 
	    }
	    return listOfLevels;
	}
	
	public void addNodeByParent(Node addThis, Node parent, boolean isLeftChild) {
		
	}
	
	public void generateTree(int height) {
		Random rand = new Random();
		
		Queue<Integer> q = new LinkedList<Integer>();
		Stack<Integer> s = new Stack<Integer>();
		
		int refInt = 0;
		int tempInt = 0;
		boolean addToStack = true;
		
		int numberOfNodes = 0;
		
		while (height >= 0) {
			numberOfNodes += Math.pow(2, height--);
		}
		
		for (int i = numberOfNodes - 1; i >= 0; i--) {
			q.add(i);
		}
		
		if (!q.isEmpty()) {
			s.add(q.remove());
		} else {
			System.out.println("q is empty");
		}
		
		if (!q.isEmpty()) {
			refInt = tempInt = q.remove();
			q.add(tempInt);
		} else {
			System.out.println("q is empty");
		}
		
		
		while (!q.isEmpty())  {
			
			if (addToStack) {
				s.add(q.remove());
				addToStack = false;
			}
			else {
				if (q.peek() != refInt) {
					tempInt = q.remove();
					q.add(tempInt);
					addToStack = true;
				} else {
					s.add(q.remove());
					refInt = q.peek();
					addToStack = true;	//just changed
				}
			}
				
			
		}
	}
	
	/*

	public void addNode(Color color, int key) {

		// Create a new Node and initialize it

		Node newNode = new Node(null, color, key);

		// If there is no root this becomes root

		if (root == null) {

			root = newNode;

		} else {

			// Set root as the Node we will start
			// with as we traverse the tree

			Node focusNode = root;

			// Future parent for our new Node

			Node parent;

			while (true) {

				// root is the top parent so we start
				// there

				parent = focusNode;

				// Check if the new node should go on
				// the left side of the parent node

				if (key < focusNode.key) {

					// Switch focus to the left child

					focusNode = focusNode.leftChild;

					// If the left child has no children

					if (focusNode == null) {

						// then place the new node on the left of it

						parent.leftChild = newNode;
						newNode.parent = parent;
						return; // All Done

					}

				} else { // If we get here put the node on the right

					focusNode = focusNode.rightChild;

					// If the right child has no children

					if (focusNode == null) {

						// then place the new node on the right of it

						parent.rightChild = newNode;
						newNode.parent = parent;
						return; // All Done

					}

				}

			}
		}

	}

	public void addNode(Node addThis) {

		// Create a new Node and initialize it

		// If there is no root this becomes root

		if (root == null) {

			root = addThis;

		} else {

			// Set root as the Node we will start
			// with as we traverse the tree

			Node focusNode = root;

			// Future parent for our new Node

			Node parent;

			while (true) {

				// root is the top parent so we start
				// there

				parent = focusNode;

				// Check if the new node should go on
				// the left side of the parent node

				if (addThis.key < focusNode.key) {

					// Switch focus to the left child

					focusNode = focusNode.leftChild;

					// If the left child has no children

					if (focusNode == null) {

						// then place the new node on the left of it

						parent.leftChild = addThis;
						addThis.parent = parent;
						return; // All Done

					}

				} else { // If we get here put the node on the right

					focusNode = focusNode.rightChild;

					// If the right child has no children

					if (focusNode == null) {

						// then place the new node on the right of it

						parent.rightChild = addThis;
						addThis.parent = parent;
						return; // All Done

					}

				}

			}
		}

	}

	public void addInOrder(Node addThis) {

		if (root == null) {
			root = addThis;
			
		}
		int height = findHeight(root);
	}

	// All nodes are visited in ascending order
	// Recursion is used to go to one node and
	// then go to its child nodes and so forth

	public void inOrderTraverseTree(Node focusNode) {

		if (focusNode != null) {

			// Traverse the left node

			inOrderTraverseTree(focusNode.leftChild);

			// Visit the currently focused on node

			System.out.println(focusNode);

			// Traverse the right node

			inOrderTraverseTree(focusNode.rightChild);

		}

	}

	public void preOrderTraverseTree(Node focusNode) {

		if (focusNode != null) {
			System.out.println(focusNode);

			preOrderTraverseTree(focusNode.leftChild);

			preOrderTraverseTree(focusNode.rightChild);
		}
	}

	public void postOrderTraverseTree(Node focusNode) {

		if (focusNode != null) {
			postOrderTraverseTree(focusNode.leftChild);

			postOrderTraverseTree(focusNode.rightChild);

			System.out.println(focusNode);
		}
	}

	public Node findNode(int key) {

		// Start at the top of the tree

		Node focusNode = root;

		// While we haven't found the Node
		// keep looking

		while (focusNode.key != key) {

			// If we should search to the left

			if (key < focusNode.key) {

				// Shift the focus Node to the left child

				focusNode = focusNode.leftChild;

			} else {

				// Shift the focus Node to the right child

				focusNode = focusNode.rightChild;

			}

			// The node wasn't found

			if (focusNode == null)
				return null;

		}

		return focusNode;

	}

	public Node findParent(int key) {

		// Start at the top of the tree

		Node focusNode = root;

		// While we haven't found the Node
		// keep looking

		if (focusNode.key == key) {
			System.out.println("This node has no parent.");
			isRoot = true;
			return null;
		}

		while (true) {

			if (focusNode == null)
				return null;

			else if (focusNode.leftChild == null && focusNode.rightChild == null) {

			}

			else if (focusNode.leftChild == null) {
				if (focusNode.rightChild.key != key) {
					focusNode = focusNode.rightChild;
				} else {
					break;
				}
			}

			else if (focusNode.rightChild == null) {
				if (focusNode.leftChild.key != key) {
					focusNode = focusNode.leftChild;
				} else {
					break;
				}
			}

			else if (focusNode.leftChild.key != key && focusNode.rightChild.key != key) {
				// If we should search to the left

				if (key < focusNode.key) {

					// Shift the focus Node to the left child

					focusNode = focusNode.leftChild;

				} else {

					// Shift the focus Node to the right child

					focusNode = focusNode.rightChild;

				}
			} else {
				break;
			}

			// The node wasn't found

		}

		return focusNode;

	}

	public Node getRoot() {
		return root;
	}

	public int findHeight(Node focusNode) {
		if (focusNode != null) {
			if (findHeight(focusNode.leftChild) > findHeight(focusNode.rightChild)) {
				return findHeight(focusNode.leftChild) + 1;
			} else {
				return findHeight(focusNode.rightChild) + 1;
			}
		}

		return 0;
	}

	public int leafCount(Node focusNode) {
		if (focusNode == null)
			return 0;

		if (focusNode.leftChild == null && focusNode.rightChild == null) {
			return 1;
		} else {
			return leafCount(focusNode.leftChild) + leafCount(focusNode.rightChild);
		}
	}

	public int smallCount(Node focusNode, int k) {
		if (focusNode == null)
			return 0;
		if (focusNode.key <= k)
			return 1 + smallCount(focusNode.leftChild, k) + smallCount(focusNode.rightChild, k);
		else
			return smallCount(focusNode.leftChild, k);
	}

	public int findDepth(Node focusNode, Node depthNode) {
		if (depthNode == null)
			return -1;
		if (focusNode != null && focusNode != depthNode) {

			if (focusNode.key < depthNode.key)
				return 1 + findDepth(focusNode.rightChild, depthNode);
			else if (focusNode.key > depthNode.key)
				return 1 + findDepth(focusNode.leftChild, depthNode);
		}

		return 0;
	}

	public void addLevel(Node focusNode, int level) {
		if (focusNode != null) {

			if (findDepth(root, focusNode) == level) {
				tempList.add(focusNode);
			}
			addLevel(focusNode.leftChild, level);
			addLevel(focusNode.rightChild, level);
		}
	}

	public void levelOrderTraverseTree() {

		for (int i = 0; i < findHeight(root); i++) {
			addLevel(root, i);
			levelList.add(new ArrayList<Node>(tempList));
		}
		tempList.clear();

	}
	
	public ArrayList<Node> getLevel(Node focusNode, int level, ArrayList<Node> list) {
		if (focusNode != null) {
			
			if (findDepth(root, focusNode) == level) {
				list.add(focusNode);
			}
			
			getLevel(focusNode.leftChild, level, list);
			getLevel(focusNode.rightChild, level, list);
			
			return list;
			
		}
		return null;
		
	}

	public ArrayList<ArrayList<Node>> getList() {
		return levelList;
	}

	public void reverseInOrder(Node focusNode) {
		reverseInOrder(focusNode.rightChild);

		System.out.println(focusNode.toString());

		reverseInOrder(focusNode.leftChild);
	}

	public Node deleteMin(Node focusNode) {
		if (focusNode.leftChild == null)
			return focusNode.rightChild;
		focusNode.leftChild = deleteMin(focusNode.leftChild);
		return focusNode;
	}

	public Node getMin(Node focusNode) {
		if (focusNode.leftChild == null)
			return focusNode;
		return getMin(focusNode.leftChild);
	}

	public Node deleteNode(int key) {

		Node parentKey = findParent(key);

		Node savedNode = null;

		Node tempLeft = null;
		Node tempRight = null;

		if (parentKey == null && isRoot == true) {
			isRoot = false;
			Node rootTemp = root;
			if (root.rightChild != null) {
				Node temp = getMin(root.rightChild);
				root.key = temp.key;
				root.rightChild = deleteMin(root.rightChild);
				System.out.println("root has been replaced: " + root.toString());
				rootTemp.leftChild = null;
				rootTemp.rightChild = null;
				return rootTemp;
			} else if (root.leftChild != null) {
				root = root.leftChild;
				rootTemp.leftChild = null;
				rootTemp.rightChild = null;
				return rootTemp;
			} else {
				root = null;
				rootTemp.leftChild = null;
				rootTemp.rightChild = null;
				return rootTemp;
			}

		} else {
			System.out.println("parentkey = " + parentKey.toString());

			if (parentKey.leftChild != null && parentKey.leftChild.key == key) {
				if (parentKey.leftChild.leftChild != null)
					tempLeft = parentKey.leftChild.leftChild;
				if (parentKey.leftChild.rightChild != null)
					tempRight = parentKey.leftChild.rightChild;
				savedNode = parentKey.leftChild;
				parentKey.leftChild = null;
				System.out.println("parentkey.leftchild = " + parentKey.leftChild);
			} else if (parentKey.rightChild != null && parentKey.rightChild.key == key) {
				if (parentKey.rightChild.leftChild != null)
					tempLeft = parentKey.rightChild.leftChild;
				if (parentKey.rightChild.rightChild != null)
					tempRight = parentKey.rightChild.rightChild;
				savedNode = parentKey.rightChild;
				parentKey.rightChild = null;
			} else {
				System.out.println("error");
				return null;
			}

			if (tempLeft != null)
				addNode(tempLeft);
			if (tempRight != null)
				addNode(tempRight);

			return savedNode;
		}

	}

	public void rearrangeTree() {
		Queue<Node> q = new LinkedList<Node>();
		while (getHighestKey(root) != null) {
			q.add(deleteNode(getHighestKey(root).key));
		}

		int qSize = q.size();

		System.out.println("queue: " + q);
		System.out.println("tree:");
		preOrderTraverseTree(root);

		for (int i = 0; i < qSize; i++) {
			addNode(q.remove());
			System.out.println("queue: " + q);
			preOrderTraverseTree(root);
		}
	}

	public Node getHighestKey(Node focusNode) {
		if (focusNode == null)
			return null;
		while (focusNode.rightChild != null) {
			focusNode = focusNode.rightChild;
		}

		return focusNode;
	}
	*/

}
