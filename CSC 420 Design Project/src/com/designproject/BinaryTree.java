package com.designproject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class BinaryTree {

	private Node root;
	private int height;		//height of tree
	private int windowHeight;
	private int windowWidth;
	private int nodeSize;
	private ArrayList<Node> leaves = new ArrayList<Node>();
	private ArrayList<Move> moves = new ArrayList<Move>();

	public BinaryTree(int height, int wWidth, int wHeight, int nodeSize) {
		this.height = height;
		this.nodeSize = nodeSize;
		windowHeight = wHeight;
		windowWidth = wWidth;
		generateTree(height);
	}
	
	public Node getRoot() {
		return root;
	}

	public ArrayList<ArrayList<Node>> getLevelOrder() {		//returns a list of every level in the tree
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
	
	public ArrayList<Node> getLevel(int level) {
	    ArrayList<ArrayList<Node>> listOfLevels = new ArrayList<ArrayList<Node>>();
	    ArrayList<Node> nodes = new ArrayList<Node>();
	    if(root == null)
	        return null;
	 
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
	    return listOfLevels.get(level);
	}
	
	public void generateTree(int height) {
		
		/*
		 * This generateTree algorithm adds nodes using addNode(), while maintaining a binary search tree.
		 * It does this by rearranging a queue of ordered keys into a stack that can be added to the tree sequentially.
		 */
		
		Queue<Integer> q = new LinkedList<Integer>();		//starting queue
		Stack<Integer> s = new Stack<Integer>();			//final stack
		
		int refInt;		//this is the key that tells the method that it has wrapped around
		int tempInt = 0;
		boolean addToStack = true;
		boolean skipRef = true;
		
		int numberOfNodes = 0;
		
		while (height >= 0) {
			numberOfNodes += Math.pow(2, height--);
		}
		
		for (int i = numberOfNodes - 1; i >= 0; i--) {
			q.add(i);
		}
		
		refInt = numberOfNodes - 2;
		
		while (!q.isEmpty())  {		//adds every other key in the queue to the stack, all others get re-added into the queue
			
			if (addToStack) {
				s.push(q.remove());
				addToStack = false;
			}
			else {
				if (q.peek() != refInt || skipRef) {
					tempInt = q.remove();
					q.add(tempInt);
					addToStack = true;
					skipRef = false;
				} else {
					s.push(q.remove());
					if (!q.isEmpty())
						refInt = q.peek();
					addToStack = false;
					skipRef = true;
				}
			}
		}
		int stackSize = s.size();
		for (int i = 0; i < stackSize; i++) {		//stack is now in the correct order, adds nodes
			addNode(i, s.pop());
		}
		
		setLeaves(root);
		
		if(leaves.size() > 30)
			setNodeLocations(50, 50);
		else
			setNodeLocations(100, 100);
		
		randomizeTree(20);
	}
	
	public void setNodeLocations(int xMargin, int yMargin) {		//sets coordinates of nodes (in terms of pixels)
		ArrayList<ArrayList<Node>> list = getLevelOrder();
		
		int minHorizontalSpace = (windowWidth - xMargin * 2) / (leaves.size() - 1);		//horizontal space between leaves
		
		int verticalSpace = (windowHeight - yMargin * 2) / height + 1;		//vertical space between all nodes
		
		for (int i = 0; i < leaves.size(); i++) {
			if (i == 0)
				leaves.get(i).coord.setLocation(xMargin - nodeSize / 2, windowHeight - yMargin - nodeSize);
			else
				leaves.get(i).coord.setLocation(leaves.get(i - 1).coord.getX() + minHorizontalSpace, windowHeight - yMargin - nodeSize);
		}
		
		for (int i = list.size() - 2; i >= 0; i--) {
			
			for (int j = 0; j < list.get(i).size(); j++) {
				Node node = list.get(i).get(j);
				node.coord.setLocation((node.leftChild.coord.getX() + node.rightChild.coord.getX()) / 2, 
						node.leftChild.coord.getY() - verticalSpace);
			}
		}
	}
	
	public void addNode(int value, int key) {
		 

    	Node newNode = new Node(null, value, key);
 
        if (root == null) {
 
            root = newNode;
        } else {
 
            Node focusNode = root;
 
            Node parent;
 
            while (true) {
 
                parent = focusNode;
 
                if (key < focusNode.key) {
 
                    focusNode = focusNode.leftChild;
 
                    if (focusNode == null) {
 
                        parent.leftChild = newNode;
                        newNode.parent = parent;
                        return;
 
                    }
 
                } else {
 
                    focusNode = focusNode.rightChild;
 
                    if (focusNode == null) {
 
                        parent.rightChild = newNode;
                        newNode.parent = parent;
                        return;
 
                    }
 
                }
 
            }
        }
	}

	public void addNodeAndCoords(int value, int key) {		//not used
		 

    	Node newNode = new Node(null, value, key);
 
        if (root == null) {
 
            root = newNode;
            root.coord.setLocation((windowWidth/2)-20, 5);
        } else {
 
            Node focusNode = root;
 
            Node parent;
 
            while (true) {
 
                parent = focusNode;
 
                if (key < focusNode.key) {
 
                    focusNode = focusNode.leftChild;
 
                    if (focusNode == null) {
 
                        parent.leftChild = newNode;
                        int depth = findDepth(root, newNode);
                        int depthP = (int) Math.pow(depth, 2);
                        newNode.coord.setLocation(parent.coord.x - (windowWidth/(depthP+3)), parent.coord.y + 167);
                        newNode.parent = parent;
                        return;
 
                    }
 
                } else {
 
                    focusNode = focusNode.rightChild;
 
                    if (focusNode == null) {
 
                        parent.rightChild = newNode;
                        int depth = findDepth(root, newNode);
                        int depthP = (int) Math.pow(depth, 2);
                        newNode.coord.setLocation(parent.coord.x + (windowWidth/(depthP+3)), parent.coord.y + 167);
                        newNode.parent = parent;
                        return;
 
                    }
 
                }
 
            }
        }
 
    }
	
	public void setLeaves(Node focusNode) {		//uses a preorder traversal to add all leaves to the list
		if (focusNode != null) {
			if (focusNode.leftChild == null && focusNode.rightChild == null)
				leaves.add(focusNode);
			
			setLeaves(focusNode.leftChild);
			setLeaves(focusNode.rightChild);
		}
	}
	
	public ArrayList<Node> getLeaves() {
		return leaves;
	}
	
	public Node getLeaf(int index) {
		return leaves.get(index);
	}
	
	public void preOrderTraversal(Node focusNode) {

		if (focusNode != null) {
			System.out.println(focusNode);

			preOrderTraversal(focusNode.leftChild);

			preOrderTraversal(focusNode.rightChild);
		}
	}
	
	public void printLevelOrder() {
		ArrayList<ArrayList<Node>> list = getLevelOrder();
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).size(); j++) {
				System.out.println(list.get(i).get(j));
			}
		}
	}
	
	public void shiftUp(Node leaf) {		//shifts values up
		Node focusNode = leaf;
		while (focusNode != null) {
			focusNode.tempValue = focusNode.value;
			focusNode = focusNode.parent;
		}
		
		focusNode = leaf;
		while (focusNode != root) {
			focusNode.parent.value = focusNode.tempValue;
			focusNode = focusNode.parent;
		}
		leaf.value = focusNode.tempValue;
		
		setLeaves(root);
	}
	
	public void shiftDown(Node leaf) {		//shifts values down
		
		Node focusNode = leaf;
		
		while (focusNode != null) {
			focusNode.tempValue = focusNode.value;
			focusNode = focusNode.parent;
		}
		
		focusNode = leaf;
		
		while (focusNode != root) {
			focusNode.value = focusNode.parent.tempValue;
			focusNode = focusNode.parent;
		}
		
		focusNode.value = leaf.tempValue; 	//sets root value from selected leaf
		
		setLeaves(root);
		
	}
	
	public boolean isSolved() {		//checks if the game is solved
		ArrayList<ArrayList<Node>> list = getLevelOrder();
		int counter = 0;
		boolean isSolved = true;

		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).size(); j++) {
				if (list.get(i).get(j).value != counter) {
					isSolved = false;
					break;
				}
				counter++;
			}
		}

		return isSolved;
	}
	
	   public ArrayList<Node> getPath(Node pathNode) {
	        if (pathNode == null)
	            return null;
	       
	        Stack<Node> s = new Stack<Node>();
	        s.push(root);
	       
	        while (!s.isEmpty() && s.peek() != pathNode) {
	           
	            if (s.peek().leftChild != null && !s.peek().leftChild.visited) {
	                s.push(s.peek().leftChild);
	                s.peek().visited = true;
	            }
	            else if (s.peek().rightChild != null && !s.peek().rightChild.visited) {
	                s.push(s.peek().rightChild);
	                s.peek().visited = true;
	            }
	            else {
	                s.pop();
	            }
	        }
	       
	        ArrayList<Node> list = new ArrayList<Node>(s);
	       
	        ArrayList<ArrayList<Node>> allNodes = getLevelOrder();
	       
	        for (int i = 0; i < allNodes.size(); i++) {
	            for (int j = 0; j < allNodes.get(i).size(); j++) {
	                allNodes.get(i).get(j).visited = false;
	            }
	        }
	 
	        return list;
	       
	    }
	
	public void randomizeTree(int amount) {
		Random rand = new Random();
		int index;
		
		for (int i = 0; i < amount; i++) {
			index = rand.nextInt(leaves.size());
			for (int j = 0; j < rand.nextInt(height) + 1; j++) {
				shiftUp(leaves.get(index));
				moves.add(new Move("up", index));
			}
		}
		
		if (isSolved()) {
			randomizeTree(amount);
		}
	}
	
	public void addMove(String move, int index) {		//adds moves to a list, not used
		moves.add(new Move(move, index));
	}
	
	public ArrayList<Move> getMoves() {
		return moves;
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
}
