package com.jade;

import java.awt.*;

import com.Component.BoxBounds;
import com.Component.Spritesheet;
import com.dataStructure.AssetPool;
import com.dataStructure.Transform;
import com.util.Constants;
import com.util.Vector2;

public class LevelEditorScene extends Scene{
	GameObject testObj;
	public LevelEditorScene(String name) {
		super.Scene(name);
	}
	
	
	@Override
	public void init() {
		testObj = new GameObject("Some game object", new Transform(new Vector2(100.0f, 300.0f)));
		Spritesheet spritesheet = new Spritesheet("assets/player/layerOne.png", 42, 42, 2, 13, 13*5);
		testObj.addComponent(spritesheet.sprites.get(43));
	}

	@Override
	public void update(double up) {
		testObj.update(up);
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.BLUE);
		g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
	
		testObj.draw(g2);
	}
	
}
