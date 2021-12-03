package com.jade;

import java.awt.*;

import com.Component.BoxBounds;
import com.Component.CameraControls;
import com.Component.Grid;
import com.Component.Ground;
import com.Component.Player;
import com.Component.Rigidbody;
import com.Component.Spritesheet;
import com.dataStructure.AssetPool;
import com.dataStructure.Transform;
import com.util.Constants;
import com.util.Vector2;

public class LevelEditorScene extends Scene{
	public GameObject player;
	GameObject ground;
	Grid grid;
	CameraControls cameraControls;
	
	public LevelEditorScene(String name) {
		super.Scene(name);
	}
	
	@Override
	public void init() {
		grid = new Grid();
		cameraControls = new CameraControls();
		player = new GameObject("Some game object", new Transform(new Vector2(500.0f, 350.0f)));
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

		
		ground = new GameObject("Ground", new Transform(new Vector2(0, Constants.GROUND_Y)));
		ground.addComponent(new Ground());

		
		addGameObject(player);
		addGameObject(ground);
		
		//player.transform.rotation = 45;
		//player.transform.scale.x = 2.0f;
		//player.transform.scale.y = 2.0f;
	}

	@Override
	public void update(double up) {
		
		if(camera.position.y > Constants.CAMERA_OFFSET_GROUND_Y) {
			camera.position.y = Constants.CAMERA_OFFSET_GROUND_Y;
		}
		
		for(GameObject g : gameObjects) {
			g.update(up);
		}
		cameraControls.update(up);
		grid.update(up);
		
		
		//player.transform.scale = new Vector2(3, 3);
		//camera.position.y += up * 60f;
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(new Color(46, 91, 238));
		g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
	
		renderer.render(g2);
		grid.draw(g2);
	}
	
}
