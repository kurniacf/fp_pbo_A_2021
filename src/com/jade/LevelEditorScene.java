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
import com.Component.LevelEditorControls;
import com.Component.ParallaxBackground;
import com.Component.Player;
import com.Component.Spritesheet;
import com.dataStructure.AssetPool;
import com.dataStructure.Transform;
import com.file.Parser;
import com.ui.MainContainer;
import com.util.Constants;
import com.util.Vector2;

public class LevelEditorScene extends Scene {
    public GameObject player;
    private GameObject ground;
    private Grid grid;
    private CameraControls cameraControls;
    public GameObject mouseCursor;
    private MainContainer editingButtons;

    public LevelEditorScene(String name) {
        super.Scene(name);
    }

    @Override
    public void init() {
        initAssetPool();
        editingButtons = new MainContainer();
        grid = new Grid();
        cameraControls = new CameraControls();
        editingButtons.start();
        mouseCursor = new GameObject("Mouse Cursor", new Transform(new Vector2()), 10);
        mouseCursor.addComponent(new LevelEditorControls(Constants.TILE_WIDTH, Constants.TILE_HEIGHT));
        player = new GameObject("Some game object", new Transform(new Vector2(500, 350.0f)), 0);
        Spritesheet layerOne = AssetPool.getSpritesheet("assets/player/layerOne.png");
        Spritesheet layerTwo = AssetPool.getSpritesheet("assets/player/layerTwo.png");
        Spritesheet layerThree = AssetPool.getSpritesheet("assets/player/layerThree.png");
        Player playerComp = new Player(layerOne.sprites.get(0), layerTwo.sprites.get(0), layerThree.sprites.get(0), Constants.BOX_IN, Constants.BOX_OUT);
        player.addComponent(playerComp);
        player.setNonserializable();
        addGameObject(player);
        initBackgrounds();
    }

    public void initAssetPool() {
        AssetPool.addSpritesheet("assets/player/layerOne.png", 42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/player/layerTwo.png", 42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/player/layerThree.png", 42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/groundSprites.png", 42, 42, 2, 6, 12);
        AssetPool.addSpritesheet("assets/ui/buttonSprites.png", 60, 60, 2, 2, 2);
        AssetPool.addSpritesheet("assets/ui/tabs.png", Constants.TAB_WIDTH, Constants.TAB_HEIGHT, 2, 6, 6);
        AssetPool.addSpritesheet("assets/spikes.png", 42, 42, 2, 6, 4);
        AssetPool.addSpritesheet("assets/bigSprites.png", 84, 84, 2, 2, 2);
        AssetPool.addSpritesheet("assets/smallBlocks.png", 42, 42, 2, 6, 1);
        AssetPool.addSpritesheet("assets/portal.png", 44, 85, 2, 2, 2);
    }

    public void initBackgrounds() {
        ground = new GameObject("Ground", new Transform(new Vector2(0, Constants.GROUND_Y)), 1);
        ground.addComponent(new Ground());
        ground.setNonserializable();
        addGameObject(ground);
        int numBackgrounds = 5;
        GameObject[] backgrounds = new GameObject[numBackgrounds];
        GameObject[] groundBgs = new GameObject[numBackgrounds];
        for (int i=0; i < numBackgrounds; i++) {
            ParallaxBackground bg = new ParallaxBackground("assets/backgrounds/bg03.png", null, ground.getComponent(Ground.class), false);
            int x = i * bg.sprite.width;
            int y = 0;

            GameObject go = new GameObject("Background", new Transform(new Vector2(x, y)), -10);
            go.setUi(true);
            go.addComponent(bg);
            go.setNonserializable();
            backgrounds[i] = go;

            ParallaxBackground groundBg = new ParallaxBackground("assets/grounds/ground03.png", null, ground.getComponent(Ground.class), true);
            x = i * groundBg.sprite.width;
            y = (int)ground.transform.position.y;
            GameObject groundGo = new GameObject("GroundBg", new Transform(new Vector2(x, y)), -9);
            groundGo.addComponent(groundBg);
            groundGo.setUi(true);
            groundGo.setNonserializable();
            groundBgs[i] = groundGo;
            addGameObject(go);
            addGameObject(groundGo);
        }
    }

    @Override
    public void update(double up) {
        if (camera.position.y > Constants.CAMERA_OFFSET_GROUND_Y + 70) {
            camera.position.y = Constants.CAMERA_OFFSET_GROUND_Y + 70;
        }

        for (GameObject g : gameObjects) {
            g.update(up);
        }
        cameraControls.update(up);
        grid.update(up);
        editingButtons.update(up);
        mouseCursor.update(up);

        if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_ENTER)) {
            export("Test");
        } else if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_SHIFT)) {
            importLevel("Test");
        } else if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_ALT)) {
            Window.getWindow().changeScene(1);
        }

        if (objsToRemove.size() > 0) {
            for (GameObject go : objsToRemove) {
                gameObjects.remove(go);
                renderer.gameObjects.get(go.zIndex).remove(go);
            }
            objsToRemove.clear();
        }
    }

    private void importLevel(String filename) {
        for (GameObject go : gameObjects) {
            if (go.serializable) {
                objsToRemove.add(go);
            }
        }
        for (GameObject go : objsToRemove) {
            renderer.gameObjects.remove(go.zIndex, go);
            gameObjects.remove(go);
        }
        Parser.openFile(filename);
        GameObject go = Parser.parseGameObject();
        while (go != null) {
            addGameObject(go);
            go = Parser.parseGameObject();
        }
    }

    private void export(String filename) {
        try {
            FileOutputStream fos = new FileOutputStream("levels/" + filename + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            zos.putNextEntry(new ZipEntry(filename + ".json"));
            int i = 0;
            for (GameObject go : gameObjects) {
                String str = go.serialize(0);
                if (str.compareTo("") != 0) {
                    zos.write(str.getBytes());
                    if (i != gameObjects.size() - 1) {
                        zos.write(",\n".getBytes());
                    }
                }
                i++;
            }
            zos.closeEntry();
            zos.close();
            fos.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void draw(Graphics2D graph2) {
    	graph2.setColor(Constants.BG_COLOR);
    	graph2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        renderer.render(graph2);
        grid.draw(graph2);
        editingButtons.draw(graph2);
        mouseCursor.draw(graph2);
    }
}