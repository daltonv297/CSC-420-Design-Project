package com.designproject;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static final double aspectRatio = 16D / 9D;				//sets window resolution and aspect ratio
	public static final int HEIGHT = 720;
	public static final int WIDTH = (int) (HEIGHT * aspectRatio);
	public static final String NAME = "TreeFiddy";		//name of window
	
	private JFrame frame;
	public static Menu menu;
	
	private BinaryTree tree;
	//private ArrayList<Node> leaves;		//arraylist of leaves in the tree
	
	private Pointer pointer;	//red arrow that designates the selected node
	private Hand hand;   //the tutorial hand
	
	public boolean running = false;
	public int tickCount = 0;
	
	int nodeSize = 40;		//size of each node
	
	Timer timer = new Timer();
	
	public enum STATE {
		MENU,
		GAME,
		SOLVED,
		TUTORIAL
	};
	
	public static STATE state = STATE.MENU;		//start game in menu
	
	Image pointerImg; //image of our arrow
	Image sam1Img;  //tutorial image
	
	public Input input;
	
	private Font font2 = new Font("arial", Font.BOLD, 30);
	private Font fontTut = new Font("arial", Font.PLAIN, 15);
	static int enterCounter = -3;
	
	public Game() {
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		frame = new JFrame(NAME);
		menu = new Menu();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		
		frame.add(this, BorderLayout.CENTER);
		
		frame.pack();
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		
	}
	
	public void init() {
		input = new Input(this);
		
		try {
			pointerImg = ImageIO.read(new File("res/pointer.png"));
		
			sam1Img = ImageIO.read(new File("res/sam1.png"));
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	
	/* 
	 * The following method allows for separation of game updates (ticks) and the amount of frames rendered.
	 * This way, game logic will not speed up or slow down when run on different systems, but the framerate
	 * is not locked. (Yay, more FPS!)
	 */

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;
		
		int ticks = 0;
		int frames = 0;
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		
		init();
		
		while (running) {
			
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;	//change to false to limit FPS
			
			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			
			try {
				Thread.sleep(2);					//causes the thread to sleep for 2ms and lowers the amount of frames
			} catch (InterruptedException e) {		//rendered (may not be necessary)
				e.printStackTrace();
			}
			
			if (shouldRender) {
				frames++;
				render();
			}
		
			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				//System.out.println("ticks: " + ticks + "  frames: " + frames);
				frames = ticks = 0;
			}
		}
	}

	public void waitForInput() {
		if (input.up.isPressed() || input.down.isPressed() || input.left.isPressed() || input.right.isPressed()) {
			input.up.setReleased();
			input.down.setReleased();
			input.left.setReleased();
			input.right.setReleased();

			tree = null;
			pointer = null;
			state = STATE.GAME;
		} 
	}

	public int counter = 0;
	
	public void tick() {
		
		if (state == STATE.GAME) {
			if (tree == null)
				tree = new BinaryTree(menu.getHeight(), getWidth(), getHeight(), nodeSize);		//initializes tree when game starts
			if (pointer == null)
				pointer = new Pointer(tree.getLeaf(0), 0);
			tickCount++;
			counter++;
			pollInputs();
			
			if (tree.isSolved()) {
				 state = STATE.SOLVED;
			}
		}
		else if (state == STATE.TUTORIAL){  
			if (tree == null)
				tree = new BinaryTree(menu.getHeight(), getWidth(), getHeight(), nodeSize);		//initializes tree when game starts
			if (pointer == null)
				pointer = new Pointer(tree.getLeaf(0), 0);
			if (hand == null)
				hand = new Hand(tree.getLeaf(0), 0);
			tickCount++;
			counter++;
			pollInputs();
			
			
			 
		}
		else if (state == STATE.SOLVED) {
			 waitForInput();
		}
		else if (state == STATE.MENU && tree != null && pointer != null) {
			tree = null;
			pointer = null;
		}
		 

	}
	
	public void pollInputs() {
		if (input.up.isPressed()) {
			tree.shiftUp(pointer.getNode());
			tree.addMove("up", pointer.getIndex());
			input.up.setReleased();
		}
		if (input.down.isPressed()) {
			tree.shiftDown(pointer.getNode());
			tree.addMove("down", pointer.getIndex());
			input.down.setReleased();
		}
		if (input.left.isPressed()) {
			if (pointer.getIndex() == 0)
				pointer.setNode(tree.getLeaf(tree.getLeaves().size() - 1), tree.getLeaves().size() - 1);
			else
				pointer.setNode(tree.getLeaf(pointer.getIndex() - 1), pointer.getIndex() - 1);
			input.left.setReleased();
		}
		if (input.right.isPressed()) {
			if (pointer.getIndex() == tree.getLeaves().size() - 1)
				pointer.setNode(tree.getLeaf(0), 0);
			else
				pointer.setNode(tree.getLeaf(pointer.getIndex() + 1), pointer.getIndex() + 1);
			input.right.setReleased();
		}
		if (input.enter.isPressed()){
			if (state == STATE.TUTORIAL){
				enterCounter++;
			}
			input.enter.setReleased();
		}
		if (input.esc.isPressed()){
			if (state == STATE.TUTORIAL || state == STATE.GAME){
				state = STATE.MENU;
				enterCounter = -3;
				counter = 0;
			}
			
			
			input.esc.setReleased();
		}
	}
	
	
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();	//a BufferStrategy object contains the mechanism of how complex 
													//memory will be organized on our Canvas
		if (bs == null) {
			createBufferStrategy(2);				//create a double-buffering strategy (change to 3 if tearing occurs)
			return;
		}
		setBackground(Color.BLACK);
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();	//gets our graphics context for drawing to the Canvas
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paint(g);
		
		if (state == STATE.GAME && tree != null) {		//rendering for game state
			drawTree(g, tree.getRoot());
			g.setColor(Color.WHITE);
			int pointerX = (int) (pointer.getNode().coord.getX() + nodeSize / 2 - pointerImg.getWidth(this) / 2);
			int pointerY = (int) (pointer.getNode().coord.getY() + 50);
			g.drawImage(pointerImg, pointerX, pointerY, 
					pointerImg.getWidth(this), pointerImg.getHeight(this), this);		//draws red pointer on screen
			if (counter < 30){			
				if(menu.getHeight() == 1){
					ArrayList<Node> array = tree.getPath(pointer.getNode());
					for(int i = 0; i<2;i++){
						g.drawOval(array.get(i).coord.x-5, array.get(i).coord.y-5, 50, 50);
					}
					for(int z = 0; z<1;z++){
					g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
							tree.getPath(pointer.getNode()).get(z+1).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
					g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
							tree.getPath(pointer.getNode()).get(z+1).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
					}
				}
				if(menu.getHeight() == 2){												//handles flashing of edges in path
					ArrayList<Node> array = tree.getPath(pointer.getNode());
					for(int i = 0; i<3;i++){
						g.drawOval(array.get(i).coord.x-5, array.get(i).coord.y-5, 50, 50);
					}
					for(int z = 0; z<2;z++){
						g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
							tree.getPath(pointer.getNode()).get(z+1).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
						g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
							tree.getPath(pointer.getNode()).get(z+1).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
					}
					
				}
				if(menu.getHeight() == 3){
					ArrayList<Node> array = tree.getPath(pointer.getNode());
					for(int i = 0; i<4;i++){
						g.drawOval(array.get(i).coord.x-5, array.get(i).coord.y-5, 50, 50);
					}
					for(int z = 0; z<3;z++){
							g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
								tree.getPath(pointer.getNode()).get(z+1).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
							g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
								tree.getPath(pointer.getNode()).get(z+1).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
					}
				}
				if(menu.getHeight() == 4){
					ArrayList<Node> array = tree.getPath(pointer.getNode());
					for(int i = 0; i<5;i++){
						g.drawOval(array.get(i).coord.x-5, array.get(i).coord.y-5, 50, 50);
					}
					for(int z = 0; z<4;z++){
						g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
								tree.getPath(pointer.getNode()).get(z+1).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
						g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
								tree.getPath(pointer.getNode()).get(z+1).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
						}
				}
				if(menu.getHeight() == 5){
					ArrayList<Node> array = tree.getPath(pointer.getNode());
					for(int i = 0; i<6;i++){
						g.drawOval(array.get(i).coord.x-5, array.get(i).coord.y-5, 50, 50);
					}
					for(int z = 0; z<5;z++){
						g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
								tree.getPath(pointer.getNode()).get(z+1).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
						g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
								tree.getPath(pointer.getNode()).get(z+1).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
						}
				}
		}
			if(counter==60){
				counter = 0;
			}
		
		
		} else if (state == STATE.MENU) {
			menu.render(g);
		}
		else if (state == STATE.TUTORIAL && tree != null){
			drawTree(g, tree.getRoot());
			g.setColor(Color.WHITE);
			drawCenteredString(g, "NEXT", font2, 500, 100);
			int pointerX = (int) (pointer.getNode().coord.getX() + nodeSize / 2 - pointerImg.getWidth(this) / 2);
			int pointerY = (int) (pointer.getNode().coord.getY() + 50);
			g.drawImage(pointerImg, pointerX, pointerY, 
					pointerImg.getWidth(this), pointerImg.getHeight(this), this);
			if (enterCounter == -3){
				g.drawImage(sam1Img, tree.getRoot().coord.x - (sam1Img.getWidth(this)/2) + nodeSize/2 , tree.getRoot().coord.y+150,
						sam1Img.getWidth(this), sam1Img.getHeight(this), this);
				g.setColor(Color.MAGENTA);
				g.setFont(fontTut);
				g.drawString("Hi and welcome to Tree Fiddy™!", tree.getRoot().coord.x + nodeSize/2 -getTextCenter(g, "Hi and welcome to Tree Fiddy™!").x, tree.getRoot().coord.y+100);
				g.drawString("My name is Sam! I'll explain the game to you!", tree.getRoot().coord.x + nodeSize/2 -getTextCenter(g, "My name is Sam! I'll explain the game to you!").x, tree.getRoot().coord.y+130);
				g.drawString("Let's go!", tree.getRoot().coord.x + nodeSize/2 -getTextCenter(g, "Let's go!").x, tree.getRoot().coord.y+160);
				g.setColor(Color.WHITE);
				g.setFont(font2);
			}
			if (enterCounter == -2){
				g.drawImage(sam1Img, tree.getRoot().coord.x - (sam1Img.getWidth(this)/2) + nodeSize/2 , tree.getRoot().coord.y+150,
						sam1Img.getWidth(this), sam1Img.getHeight(this), this);
				g.setColor(Color.MAGENTA);
				g.setFont(fontTut);
				g.drawString("Press ENTER or click the NEXT button", tree.getRoot().coord.x + nodeSize/2 -getTextCenter(g, "Press ENTER or click the NEXT button").x, tree.getRoot().coord.y+130);
				g.drawString("to advance the tutorial!", tree.getRoot().coord.x + nodeSize/2 -getTextCenter(g, "to advance the tutorial!").x, tree.getRoot().coord.y+160);
				g.setColor(Color.WHITE);
				g.setFont(font2);
			}
			if (enterCounter == -1){
				g.drawImage(sam1Img, pointerX+50, pointerY-120,
						sam1Img.getWidth(this), sam1Img.getHeight(this), this);
					g.setColor(Color.MAGENTA);
					g.setFont(fontTut);
					g.drawString("Use LEFT and RIGHT arrow keys", pointerX-getTextCenter(g, "Use LEFT and RIGHT arrow keys").x+50+sam1Img.getWidth(this)/2, pointerY-getTextCenter(g, "Use LEFT and RIGHT arrow keys").y-130);
					g.drawString("to move the red arrow on the bottom!", pointerX-getTextCenter(g, "to move the red arrow on the bottom!").x+50+sam1Img.getWidth(this)/2, pointerY-115);
					g.setColor(Color.WHITE);
					g.setFont(font2);
			}
			if (enterCounter == 0){
				g.drawImage(sam1Img, pointerX+50, pointerY-120,
					sam1Img.getWidth(this), sam1Img.getHeight(this), this);
				g.setColor(Color.MAGENTA);
				g.setFont(fontTut);
				g.drawString("This red arrow is the pointer.", pointerX-getTextCenter(g, "This red arrow is the pointer.").x+50+sam1Img.getWidth(this)/2, pointerY-getTextCenter(g, "This is the pointer").y-130);
				g.drawString("It points to a node that you have selected!", pointerX-getTextCenter(g, "It points to a node that you have selected!").x+50+sam1Img.getWidth(this)/2, pointerY-115);
				g.setColor(Color.WHITE);
				g.setFont(font2);
			}
			if (enterCounter == 1){
				g.drawImage(sam1Img, pointer.getNode().parent.coord.x, pointer.getNode().parent.coord.y-100,
						sam1Img.getWidth(this), sam1Img.getHeight(this), this);
					g.setColor(Color.MAGENTA);
					g.setFont(fontTut);
					g.drawString("These circles are the nodes.", pointer.getNode().parent.coord.x -getTextCenter(g, "These circles are the nodes.").x+sam1Img.getWidth(this)/2, pointerY-350);
					g.drawString("Pressing UP will move every",pointer.getNode().parent.coord.x- getTextCenter(g, "Pressing UP will move every").x+sam1Img.getWidth(this)/2, pointerY-335);
					g.drawString("flashing number with the one above it!",pointer.getNode().parent.coord.x- getTextCenter(g, "flashing number with the one above it!").x+sam1Img.getWidth(this)/2, pointerY-320);
					g.setColor(Color.WHITE);
					g.setFont(font2);
			}
			if (enterCounter == 2){
				g.drawImage(sam1Img, tree.getRoot().coord.x - (sam1Img.getWidth(this)/2) + nodeSize/2 , tree.getRoot().coord.y+90,
						sam1Img.getWidth(this), sam1Img.getHeight(this), this);
					g.setColor(Color.MAGENTA);
					g.setFont(fontTut);
					g.drawString("When you press UP, this number at the top will always go to where", tree.getRoot().coord.x + nodeSize/2 -getTextCenter(g, "When you press UP, this number at the top will always go to where").x, tree.getRoot().coord.y+70);
					g.drawString("the red arrow is pointing!", tree.getRoot().coord.x + nodeSize/2 -getTextCenter(g, "the red arrow is pointing!").x, tree.getRoot().coord.y+100);
					g.setColor(Color.WHITE);
					g.setFont(font2);
			}
			if (enterCounter == 3){
				g.drawImage(sam1Img, tree.getRoot().coord.x - (sam1Img.getWidth(this)/2) + nodeSize/2 , tree.getRoot().coord.y+90,
						sam1Img.getWidth(this), sam1Img.getHeight(this), this);
					g.setColor(Color.MAGENTA);
					g.setFont(fontTut);
					g.drawString("When you press DOWN, it will work", tree.getRoot().coord.x + nodeSize/2 -getTextCenter(g, "When you press DOWN, it will work").x, tree.getRoot().coord.y+70);
					g.drawString("identical to UP, but backwards.", tree.getRoot().coord.x + nodeSize/2 -getTextCenter(g, "identical to UP, but backwards.").x, tree.getRoot().coord.y+100);
					g.setColor(Color.WHITE);
					g.setFont(font2);
			}
			if (enterCounter == 4){
				g.drawImage(sam1Img, tree.getRoot().coord.x - (sam1Img.getWidth(this)/2) + nodeSize/2 , tree.getRoot().coord.y+170,
						sam1Img.getWidth(this), sam1Img.getHeight(this), this);
				g.setColor(Color.MAGENTA);
				g.setFont(fontTut);
				g.drawString("Win the game by rearranging the numbers in", tree.getRoot().coord.x + nodeSize/2 -getTextCenter(g, "Win the game by rearranging the numbers in").x, tree.getRoot().coord.y+120);
				g.drawString("an ascending order from top to bottom", tree.getRoot().coord.x + nodeSize/2 -getTextCenter(g, "an ascending order from top to bottom").x, tree.getRoot().coord.y+150);
				g.drawString("and left to right!", tree.getRoot().coord.x + nodeSize/2 -getTextCenter(g, "and left to right!").x, tree.getRoot().coord.y+180);
				g.setColor(Color.WHITE);
				g.setFont(font2);
			}
			if (enterCounter == 5){
				g.drawImage(sam1Img, tree.getRoot().coord.x - (sam1Img.getWidth(this)/2) + nodeSize/2 , tree.getRoot().coord.y+150,
						sam1Img.getWidth(this), sam1Img.getHeight(this), this);
				g.setColor(Color.MAGENTA);
				g.setFont(fontTut);
				g.drawString("Now you know everything!", tree.getRoot().coord.x + nodeSize/2 -getTextCenter(g, "Now you know everything!").x, tree.getRoot().coord.y+130);
				g.drawString("Press ENTER or click NEXT to jump in!", tree.getRoot().coord.x + nodeSize/2 -getTextCenter(g, "Press ENTER or click NEXT to jump in!").x, tree.getRoot().coord.y+160);
				g.setColor(Color.WHITE);
				g.setFont(font2);
			}
			if (enterCounter == 6){
				state = STATE.GAME;
			}
			
			if (counter < 30){
				if(menu.getHeight() == 1){
					ArrayList<Node> array = tree.getPath(pointer.getNode());
					for(int i = 0; i<2;i++){
						g.drawOval(array.get(i).coord.x-5, array.get(i).coord.y-5, 50, 50);
					}
					for(int z = 0; z<1;z++){
					g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
							tree.getPath(pointer.getNode()).get(z+1).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
					g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
							tree.getPath(pointer.getNode()).get(z+1).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
					}
				}
				if(menu.getHeight() == 2){
					ArrayList<Node> array = tree.getPath(pointer.getNode());
					for(int i = 0; i<3;i++){
						g.drawOval(array.get(i).coord.x-5, array.get(i).coord.y-5, 50, 50);
					}
					for(int z = 0; z<2;z++){
					g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
							tree.getPath(pointer.getNode()).get(z+1).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
					g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
							tree.getPath(pointer.getNode()).get(z+1).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
					}
					
				}
				if(menu.getHeight() == 3){
					ArrayList<Node> array = tree.getPath(pointer.getNode());
					for(int i = 0; i<4;i++){
						g.drawOval(array.get(i).coord.x-5, array.get(i).coord.y-5, 50, 50);
					}
					for(int z = 0; z<3;z++){
						g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
								tree.getPath(pointer.getNode()).get(z+1).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
						g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
								tree.getPath(pointer.getNode()).get(z+1).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
						}
				}
				if(menu.getHeight() == 4){
					ArrayList<Node> array = tree.getPath(pointer.getNode());
					for(int i = 0; i<5;i++){
						g.drawOval(array.get(i).coord.x-5, array.get(i).coord.y-5, 50, 50);
					}
					for(int z = 0; z<4;z++){
						g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
								tree.getPath(pointer.getNode()).get(z+1).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
						g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
								tree.getPath(pointer.getNode()).get(z+1).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
						}
				}
				if(menu.getHeight() == 5){
					ArrayList<Node> array = tree.getPath(pointer.getNode());
					for(int i = 0; i<6;i++){
						g.drawOval(array.get(i).coord.x-5, array.get(i).coord.y-5, 50, 50);
					}
					for(int z = 0; z<5;z++){
						g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
								tree.getPath(pointer.getNode()).get(z+1).coord.x-1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
						g.drawLine(tree.getPath(pointer.getNode()).get(z).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z).coord.y+nodeSize,
								tree.getPath(pointer.getNode()).get(z+1).coord.x+1 + nodeSize/2, tree.getPath(pointer.getNode()).get(z+1).coord.y);
					}
				}

				
			}
		} else if (state == STATE.SOLVED) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial", Font.PLAIN, 50));
			String won = "YOU WON!";
			g.drawString(won, (int) (getWidth() / 2 - getTextCenter(g, won).getX()),
					(int) (getHeight() / 2 - getTextCenter(g, won).getY()));
		}

		g.dispose();
		bs.show();
		if (counter == 60) {
			counter = 0;
		}
	}
	
	public void drawTree(Graphics2D g, Node focusNode) {		//preorder traversal to get node coordinates and draw them to the screen
		if (focusNode != null) {
			g.setColor(Color.WHITE);
			g.fillOval(focusNode.coord.x, focusNode.coord.y, nodeSize, nodeSize);
			g.setColor(Color.BLACK);
			g.setFont(new Font("arial", Font.BOLD, 25));
			String number = "" + focusNode.value;
			g.drawString(number, (int) (focusNode.coord.getX() + nodeSize / 2 - getTextCenter(g, number).getX()), 
					(int) (focusNode.coord.getY() + nodeSize / 2 + getTextCenter(g, number).getY()) - 4);
			if (focusNode.leftChild!=null){
				g.setColor(Color.WHITE);
				g.drawLine(focusNode.coord.x + nodeSize / 2, focusNode.coord.y + nodeSize, focusNode.leftChild.coord.x + nodeSize / 2, focusNode.leftChild.coord.y);
			}
			if(focusNode.rightChild!=null){
				g.setColor(Color.WHITE);
				g.drawLine(focusNode.coord.x + nodeSize / 2, focusNode.coord.y + nodeSize , focusNode.rightChild.coord.x + nodeSize / 2, focusNode.rightChild.coord.y);
			}

			drawTree(g, focusNode.leftChild);
			drawTree(g, focusNode.rightChild);
		}
	}
	
	public Point getTextCenter(Graphics2D g, String s) {	//gets center of rectangle around text
		Rectangle2D rect = g.getFontMetrics().getStringBounds(s, g);
		return new Point((int) (rect.getWidth() / 2), (int) (rect.getHeight() / 2));
	}

	
	
	public Point getMiddle(int widthOfShape, int heightOfShape) {	//gets middle of screen, compensating for width and height of the shape
		return new Point(getWidth() / 2 - widthOfShape / 2, getHeight() / 2 - heightOfShape / 2);
	}
	
	public int getMiddle(int widthOfShape) {
		return getWidth() / 2 - widthOfShape / 2;
	}
	
	public void drawCenteredString(Graphics2D g, String s, Font font, int xOffset, int yPos) {
		g.setFont(font);
		Rectangle2D rect = g.getFontMetrics().getStringBounds(s, g);
		g.drawString(s, (int) (Game.WIDTH / 2 - rect.getWidth() / 2) + xOffset, yPos);
	}
	
	public static void main(String[] args) {
		new Game().start();
	}
	
}