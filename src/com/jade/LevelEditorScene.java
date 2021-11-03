package com.jade;

import java.awt.*;

import com.util.Constants;

public class LevelEditorScene extends Scene{
	public LevelEditorScene(String name) {
		super.Scene(name);
	}
	
	
	@Override
	public void init() {
		
	}

	@Override
	public void update(double up) {
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.BLUE);
		g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
	}
	
}
