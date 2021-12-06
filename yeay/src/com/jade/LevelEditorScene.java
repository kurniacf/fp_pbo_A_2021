package com.jade;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.Component.CameraControls;
import com.Component.Grid;
import com.Component.Ground;
import com.Component.Player;
import com.Component.SnapToGrid;
import com.Component.Spritesheet;
import com.dataStructure.AssetPool;
import com.dataStructure.Transform;
import com.file.Parser;
import com.ui.MainContainer;
import com.util.Constants;
import com.util.Vector2;

public class LevelEditorScene extends Scene{
	public GameObject player;
	private GameObject ground;
	private Grid grid;
	private CameraControls cameraControls;
	public GameObject mouseCursor;
	private MainContainer editingButtons;
	//SnapToGrid snapToGrid;
	
	public LevelEditorScene(String name) {
		super.Scene(name);
		//grid = new Grid();
	}
	
	@Override
	public void init() {
		
		initAssetPool();
		editingButtons = new MainContainer();
		
		grid = new Grid();
		cameraControls = new CameraControls();
		
		editingButtons.start();
		
		mouseCursor = new GameObject("Mouse Cursor", new Transform(new Vector2()));
		mouseCursor.addComponent(new SnapToGrid(Constants.TILE_WIDTH, Constants.TILE_HEIGHT));
		
		player = new GameObject("Some game object", new Transform(new Vector2(500.0f, 350.0f)));
		Spritesheet layerOne = AssetPool.getSpritesheet("assets/player/layerOne.png");
		Spritesheet layerTwo = AssetPool.getSpritesheet("assets/player/layerTwo.png");
		Spritesheet layerThree = AssetPool.getSpritesheet("assets/player/layerThree.png");
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

		ground.setNonserializeable();
		player.setNonserializeable();
		addGameObject(player);
		addGameObject(ground);
		/*
		Parser.openFile("Test");
		Parser.skipWhiteSpace();
		
		System.out.println(Parser.parseInt());
		System.out.println(Parser.parseInt());
		System.out.println(Parser.parseDouble());
		System.out.println(Parser.parseFloat());
		System.out.println(Parser.parseBoolean());
		System.out.println(Parser.parseBoolean());
		System.out.println(Parser.parseString());
		System.out.println(Parser.parseString());
		*/
		}
	
	public void initAssetPool() {
		AssetPool.addSpritesheet("assets/player/layerOne.png", 42, 42, 2, 13, 13*5);
		AssetPool.addSpritesheet("assets/player/layerTwo.png", 42, 42, 2, 13, 13*5);
		AssetPool.addSpritesheet("assets/player/layerThree.png", 42, 42, 2, 13, 13*5);
		AssetPool.addSpritesheet("assets/groundSprites.png", 42, 42, 2, 6, 12);
		AssetPool.addSpritesheet("assets/buttonSprites.png", 60, 60, 2, 6, 2);
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
		editingButtons.update(up);
		mouseCursor.update(up);
		
		if(Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
			export("Test");
		}else if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_SHIFT)) {
			importLevel("Test");
		}
	}
	
	private void importLevel(String filename) {
		Parser.openFile(filename);
		
		GameObject go = Parser.parseGameObject();
		while(go != null) {
			addGameObject(go);
			go = Parser.parseGameObject();
		}
	}
	
	private void export (String filename) {
		try {
			FileOutputStream fos = new FileOutputStream("levels/"+filename+".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);
			
			zos.putNextEntry(new ZipEntry(filename + ".json"));
			
			int i=0;
			for(GameObject go : gameObjects) {
				String str = go.serialize(0);
				if(str.compareTo("") != 0) {
					zos.write(str.getBytes());
					if(i != gameObjects.size()-1) {
						zos.write(",\n".getBytes());
					}
				}
				i++;
			}
			
			zos.closeEntry();
			zos.close();
			fos.close();
			
		}catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(new Color(46, 91, 238));
		g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
	
		renderer.render(g2);
		grid.draw(g2);
		editingButtons.draw(g2);
		
		//Should be drawn last
		mouseCursor.draw(g2);
	}
	
}
