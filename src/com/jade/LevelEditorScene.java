package com.jade;

import java.awt.*;

import com.Component.BoxBounds;
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
		testObj = new GameObject("Some game object", new Transform(new Vector2(0.0f, 0.0f)));
		testObj.addComponent(new BoxBounds("Box"));
	}

	@Override
	public void update(double up) {
		System.out.print(testObj.getComponent(BoxBounds.class).name);
		
		
		testObj.update(up);
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.BLUE);
		g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
	}
	
}
