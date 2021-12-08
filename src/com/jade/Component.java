package com.jade;

import java.awt.Graphics2D;

import com.file.Serialize;

public abstract class Component extends Serialize {

    public GameObject gameObject;

    public void update(double dt) {
        return;
    }

    public void draw(Graphics2D g2) {
        return;
    }

    public void start() {
        return;
    }

    public abstract Component copy();
}
