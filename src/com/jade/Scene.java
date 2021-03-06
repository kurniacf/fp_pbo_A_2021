package com.jade;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import com.util.Vector2;

public abstract class Scene {
    String name;
    public Camera camera;
    List<GameObject> gameObjects;
    List<GameObject> objsToRemove;
    Renderer renderer;

    public void Scene(String name) {
        this.name = name;
        this.camera = new Camera(new Vector2());
        this.gameObjects = new ArrayList<>();
        this.objsToRemove = new ArrayList<>();
        this.renderer = new Renderer(this.camera);
    }

    public void init() {

    }

    public List<GameObject> getAllGameObjects() {
        return gameObjects;
    }

    public void removeGameObject(GameObject go) {
        objsToRemove.add(go);
    }

    public void addGameObject(GameObject g) {
        gameObjects.add(g);
        renderer.submit(g);
        for (Component cmp : g.getAllComponents()) {
            cmp.start();
        }
    }

    public abstract void update(double up);
    public abstract void draw(Graphics2D graph2);
}
