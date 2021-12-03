package com.jade;

import java.awt.Graphics2D;

public abstract class Component<T> {
	
	public GameObject gameObject;
	
	public void update(double up) {
		return;
	}
	
	public void draw(Graphics2D g2) {
		return;
	}
	public abstract Component copy();
}
