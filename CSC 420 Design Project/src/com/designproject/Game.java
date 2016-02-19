package com.designproject;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static final double aspectRatio = 16D / 9D;				//sets window resolution and aspect ratio
	public static final int HEIGHT = 720;
	public static final int WIDTH = (int) (HEIGHT * aspectRatio);
	public static final String NAME = "TreeFiddy";
	
	private JFrame frame;
	
	private BinaryTree tree;
	
	public boolean running = false;
	public int tickCount = 0;
	
	int xPos = 0;
	int yPos = 0;
	
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
		/*
		try {
			testimg = ImageIO.read(getClass().getResource("/testimg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		//xPos = getWidth() / 2 - testimg.getWidth() / 4;
		//yPos = getHeight() / 2 - testimg.getHeight() / 4;
		
		
		input = new Input(this);
		
		tree = new BinaryTree();
		
		//tree.addNode(new Node(null, Color.BLUE, 50));
		//tree.addNode(new Node(null, Color.GREEN, 25));
		//tree.addNode(new Node(null, Color.YELLOW, 75));
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
			yPos -= 5;
		}
		if (input.down.isPressed()) {
			yPos += 5;
		}
		if (input.left.isPressed()) {
			xPos -= 5;
		}
		if (input.right.isPressed()) {
			xPos += 5;
		}
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();	//a BufferStrategy object contains the mechanism of how complex 
													//memory will be organized on our Canvas
		if (bs == null) {
			createBufferStrategy(2);				//create a double-buffering strategy (change to 3 if tearing occurs)
			return;
		}
		setBackground(Color.RED);
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();			//gets our graphics context for drawing to the Canvas
		paint(g);
		
		drawTree(g);
		
		
		g.dispose();
		bs.show();
	}
	
	public void drawTree(Graphics2D g) {
		//if (tree.getRoot() != null) {
			
		//}
	}
	
	public Point getMiddle(int widthOfShape, int heightOfShape) {
		return new Point(getWidth() / 2 - widthOfShape / 2, getHeight() / 2 - heightOfShape / 2);
	}
	
	/*
	public void fillOvalInMiddle(Graphics2D g, Shape s, int xOffset, int yOffset) {
		s.getBounds().setLocation(new Point((int) (getWidth() / 2 - s.getBounds().getWidth()), 
				(int) (getHeight() / 2 - s.getBounds().getHeight())));
		//s.getBounds().setBounds(new Rectangle((int) (getWidth() / 2 - s.getBounds().getWidth()), 
			//	(int) (getHeight() / 2 - s.getBounds().getHeight())));
		System.out.println(s.getBounds2D().getBounds());
		
		g.fill(s);
	}
	*/
	
	public static void main(String[] args) {
		new Game().start();
	}
	
}