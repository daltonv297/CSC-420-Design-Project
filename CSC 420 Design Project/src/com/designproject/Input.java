package com.designproject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import com.designproject.Game.STATE;

public class Input implements KeyListener, MouseListener {
	
	public Input(Game game) {
		game.addKeyListener(this);
		game.addMouseListener(this);
	}
	
	public class Key {
		private boolean isPressed = false;
		
		public boolean isPressed() {
			return isPressed;
		}
		
		public void setPressed() {
			isPressed = true;
		}
		
		public void setReleased() {
			isPressed = false;
		}
	}
	

	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {
		//toggleKey(e.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent e) {

	}
	
	public void mouseClicked(MouseEvent e) {
		
		if (Game.menu.b1.contains(e.getPoint())) {
			Game.state = STATE.GAME;
		}
		
		if (Game.menu.b2.contains(e.getPoint())) {
			if (Game.menu.getHeight() > 4) {
				Game.menu.setHeight(1);
			} else {
				Game.menu.setHeight(Game.menu.getHeight() + 1);
			}
		}
		
		if (Game.menu.b3.contains(e.getPoint())) {
			System.exit(1);
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}
	
	public void toggleKey(int keyCode) {
		if (keyCode == KeyEvent.VK_UP) {
			up.setPressed();
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			down.setPressed();
		}
		if (keyCode == KeyEvent.VK_LEFT) {
			left.setPressed();
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			right.setPressed();
		}
	}

}
