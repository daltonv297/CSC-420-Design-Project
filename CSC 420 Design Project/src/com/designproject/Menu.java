package com.designproject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Menu {
	
	private Rectangle b1 = new Rectangle(Game.WIDTH / 2 + 120, 150, 100, 50);
	private Rectangle b2 = new Rectangle(Game.WIDTH / 2 + 120, 250, 100, 50);
	private Rectangle b3 = new Rectangle(Game.WIDTH / 2 + 120, 350, 100, 50);
	
	public Menu() {
		
	}
	
	public void render(Graphics2D g) {
		Font font1 = new Font("arial", Font.BOLD, 50);
		String s = "hello";
		g.setColor(Color.PINK);
		drawCenteredString(g, s, font1, 0, 75);
		
		g.draw(b1);
		g.draw(b2);
		g.draw(b3);
	}
	
	public void drawCenteredString(Graphics2D g, String s, Font font, int xOffset, int yPos) {
		g.setFont(font);
		Rectangle2D rect = g.getFontMetrics().getStringBounds(s, g);
		g.drawString(s, (int) (Game.WIDTH / 2 - rect.getWidth() / 2) + xOffset, yPos);
	}
}
