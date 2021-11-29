package com.jade;

import java.awt.*;

import com.Component.BoxBounds;
import com.Component.Player;
import com.Component.Spritesheet;
import com.dataStructure.AssetPool;
import com.dataStructure.Transform;
import com.util.Constants;
import com.util.Vector2;

public class LevelEditorScene extends Scene{
	GameObject player;
	public LevelEditorScene(String name) {
		super.Scene(name);
	}
	
	
	@Override
	public void init() {
		player = new GameObject("Some game object", new Transform(new Vector2(100.0f, 300.0f)));
		Spritesheet layerOne = new Spritesheet("assets/player/layerOne.png", 42, 42, 2, 13, 13*5);
		Spritesheet layerTwo = new Spritesheet("assets/player/layerTwo.png", 42, 42, 2, 13, 13*5);
		Spritesheet layerThree = new Spritesheet("assets/player/layerThree.png", 42, 42, 2, 13, 13*5);
		Player playerComp = new Player(
				layerOne.sprites.get(0),
				layerTwo.sprites.get(0),
				layerThree.sprites.get(0),
				Color.RED,
				Color.MAGENTA
		);
		player.addComponent(playerComp);
		
		renderer.submit(player);
		
		//player.transform.rotation = 45;
		//player.transform.scale.x = 2.0f;
		//player.transform.scale.y = 2.0f;
	}

	@Override
	public void update(double up) {
		player.update(up);
		player.transform.rotation += up * 1f;
		camera.position.x += up * 60f;
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(new Color(46, 91, 238));
		g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
	
		renderer.render(g2);
	}
	
}
