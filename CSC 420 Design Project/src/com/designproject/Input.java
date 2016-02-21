package com.designproject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Input implements KeyListener {
	
	public Input(Game game) {
		game.addKeyListener(this);
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
