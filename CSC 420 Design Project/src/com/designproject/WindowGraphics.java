package com.designproject;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
 
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
 

public class WindowGraphics extends JFrame {
	
	private int WIDTH = 1280;
	private int HEIGHT = 720;
 
    public WindowGraphics() {
        super("Lines Drawing Demo");
 
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
 
    void drawToScreen(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
 
        g2d.drawLine(120, 50, 460, 90);
 
        g2d.draw(new Line2D.Double(59.2d, 99.8d, 419.1d, 99.8d));
 
        g2d.draw(new Line2D.Float(21.50f, 132.50f, 459.50f, 132.50f));
 
    }
 
    public void paint(Graphics g) {
        super.paint(g);
        drawToScreen(g);
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WindowGraphics().setVisible(true);
            }
        });
    }
}
