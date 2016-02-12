package com.designproject;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static final double aspectRatio = 16 / 9;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = (int) (WIDTH * aspectRatio);
	
	public static final String NAME = "TreeFiddy";
	
	private JFrame frame;
	
	public boolean running = false;
	
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
	
	public synchronized void start() {
		new Thread(this).start();
		running = true;
	}
	
	public synchronized void stop() {
		
	}

	public void run() {
		
	}
	
	public static void main(String[] args) {
		new Game().start();
	}
	
}