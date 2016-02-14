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
		private int numTimesPressed = 0;
		private boolean isPressed = false;
		
		public boolean isPressed() {
			return isPressed;
		}
		
		public int getNumTimesPressed() {
			return numTimesPressed;
		}
		
		public void toggle(boolean isPressed) {
			this.isPressed = isPressed;
			if (this.isPressed)
				numTimesPressed++;
		}
	}
	

	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent e) {

	}
	
	public void toggleKey(int keyCode, boolean isPressed) {
		if (keyCode == KeyEvent.VK_UP) {
			up.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			down.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_LEFT) {
			left.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			right.toggle(isPressed);
		}
	}

}
