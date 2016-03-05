package com.designproject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Menu {
	
	public Rectangle b1 = new Rectangle(getMiddle(150)-500, 150, 150, 75);
	public Rectangle b2 = new Rectangle(getMiddle(150)-500, 350, 150, 75);
	public Rectangle b3 = new Rectangle(getMiddle(150)-500, 450, 150, 75);
	public Rectangle b4 = new Rectangle(getMiddle(150)-500, 250, 150, 75);
	private int height = 3;
	
	private Font font1 = new Font("arial", Font.PLAIN, 50);
	private Font font2 = new Font("arial", Font.BOLD, 30);
	
	
	public Menu() {
		
	}
	
	public void render(Graphics2D g) {
		
		g.setColor(Color.WHITE);
		drawCenteredString(g, "Tree Fiddy™", font1, -450, 100);
		g.setColor(Color.BLACK);
		g.fill(b1);
		g.fill(b2);
		g.fill(b3);
		g.fill(b4);
		
		g.setColor(Color.WHITE);
		drawCenteredString(g, "PLAY", font2, -500, (int) (b1.getY() + 50));
		drawCenteredString(g, "HEIGHT: " + height, font2, -500, (int) (b2.getY() + 45));
		drawCenteredString(g, "QUIT", font2, -500, (int) (b3.getY() + 50));
		drawCenteredString(g, "TUTORIAL", font2, -500, (int) (b4.getY() + 50));
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int h) {
		height = h;
	}

	
	public void drawCenteredString(Graphics2D g, String s, Font font, int xOffset, int yPos) {
		g.setFont(font);
		Rectangle2D rect = g.getFontMetrics().getStringBounds(s, g);
		g.drawString(s, (int) (Game.WIDTH / 2 - rect.getWidth() / 2) + xOffset, yPos);
	}
	
	public Point getMiddle(int widthOfShape, int heightOfShape) {
		return new Point(Game.WIDTH / 2 - widthOfShape / 2, Game.HEIGHT / 2 - heightOfShape / 2);
	}
	
	public int getMiddle(int widthOfShape) {
		return Game.WIDTH / 2 - widthOfShape / 2;
	}
	
	public void setMiddle(Rectangle rect, int xOffset, int yOffset) {
		rect.setLocation((int) (Game.WIDTH / 2 - rect.getWidth() / 2 + xOffset), (int) (Game.HEIGHT / 2 - rect.getHeight() / 2 + yOffset));
	}
}
