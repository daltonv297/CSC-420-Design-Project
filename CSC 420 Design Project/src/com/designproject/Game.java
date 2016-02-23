package com.designproject;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static final double aspectRatio = 16D / 9D;				//sets window resolution and aspect ratio
	public static final int HEIGHT = 720;
	public static final int WIDTH = (int) (HEIGHT * aspectRatio);
	public static final String NAME = "TreeFiddy";
	
	private JFrame frame;
	
	private BinaryTree tree;
	private ArrayList<Node> leaves;
	
	private Pointer pointer;
	
	public boolean running = false;
	public int tickCount = 0;
	
	int nodeSize = 40;
	
	//private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	//private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();	//gets pixel array from raster image
	
	//BufferedImage testimg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	public Input input;
	
	public Game() {
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		frame = new JFrame(NAME);
		
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
		tree = new BinaryTree(3, WIDTH, HEIGHT, nodeSize);
		pointer = new Pointer(tree.getLeaf(0), 0);
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
	
	public void tick() {
		tickCount++;
		
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
			System.out.println(pointer.getNode());
			input.left.setReleased();
		}
		if (input.right.isPressed()) {
			if (pointer.getIndex() == tree.getLeaves().size() - 1)
				pointer.setNode(tree.getLeaf(0), 0);
			else
				pointer.setNode(tree.getLeaf(pointer.getIndex() + 1), pointer.getIndex() + 1);
			System.out.println(pointer.getNode());
			input.right.setReleased();
		}
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();	//a BufferStrategy object contains the mechanism of how complex 
													//memory will be organized on our Canvas
		if (bs == null) {
			createBufferStrategy(2);				//create a double-buffering strategy (change to 3 if tearing occurs)
			return;
		}
		setBackground(Color.GRAY);
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();			//gets our graphics context for drawing to the Canvas
		paint(g);
		
		
		drawTree(g, tree.getRoot());
		
		
		g.dispose();
		bs.show();
	}
	
	public void drawTree(Graphics2D g, Node focusNode) {
		if (focusNode != null) {
			g.setColor(Color.YELLOW);
			g.fillOval(focusNode.coord.x, focusNode.coord.y, nodeSize, nodeSize);
			g.setColor(Color.BLACK);
			if (focusNode.leftChild!=null){
				g.drawLine(focusNode.coord.x + nodeSize / 2, focusNode.coord.y + nodeSize, focusNode.leftChild.coord.x + nodeSize / 2, focusNode.leftChild.coord.y);
			}
			if(focusNode.rightChild!=null){
				g.drawLine(focusNode.coord.x + nodeSize / 2, focusNode.coord.y + nodeSize , focusNode.rightChild.coord.x + nodeSize / 2, focusNode.rightChild.coord.y);
			}

			drawTree(g, focusNode.leftChild);
			drawTree(g, focusNode.rightChild);
		}
	}

	
	
	public Point getMiddle(int widthOfShape, int heightOfShape) {
		return new Point(getWidth() / 2 - widthOfShape / 2, getHeight() / 2 - heightOfShape / 2);
	}
	
	public static void main(String[] args) {
		new Game().start();
	}
	
}