package com.designproject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class WindowGraphics {
	
	private static int WIDTH = 800;
	private static int HEIGHT = 450;	
	
	private static void createAndShowGUI()
	{
		JFrame mainWindow = new JFrame("Test");
		
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		JLabel emptyLabel = new JLabel();
		emptyLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		mainWindow.getContentPane().add(emptyLabel, BorderLayout.CENTER);
		
		mainWindow.pack();
		mainWindow.setVisible(true);
	}
	
	private static void paint (Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;
	    g2.draw(new Line2D.Double(0, 0, 200, 200));

	}
	
	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
}
