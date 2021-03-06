package com.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

public class Constants {
    public static final int SCREEN_WIDTH = 1270;
    public static final int SCREEN_HEIGHT = 720;
    public static final String SCREEN_TITLE = "Shape Rush";
    public static final int PLAYER_WIDTH = 50;
    public static final int PLAYER_HEIGHT = 50;
    public static final float JUMP_FORCE = -650;
    public static final float PLAYER_SPEED = 400;

    public static final int GROUND_Y = 714;
    public static final int CAMERA_OFFSET_X = 300;
    public static final int CAMERA_OFFSET_Y = 325;
    public static final int CAMERA_OFFSET_GROUND_Y = 150;

    public static final float GRAVITY = 2900;
    public static final float TERMINAL_VELOCITY = 1900;
    public static final int TILE_WIDTH = 42;
    public static final int TILE_HEIGHT = 42;

    public static final int BUTTON_OFFSET_X = 400;
    public static final int BUTTON_OFFSET_Y = 560;
    public static final int BUTTON_SPACING_HZ = 10;
    public static final int BUTTON_SPACING_VT = 5;
    public static final int BUTTON_WIDTH = 60;
    public static final int BUTTON_HEIGHT = 60;
    public static final Color BG_COLOR = new Color(255.0f / 255.0f, 16.0f / 189.0f, 240.0f / 255.0f, 1.0f);
    public static final Color GROUND_COLOR = new Color(60.0f / 100.0f, 120.0f / 200.0f, 170.0f / 178.0f, 1.0f);
    public static final Color BOX_OUT = new Color(31.0f / 255.0f, 81.0f / 255.0f, 255.0f / 255.0f, 1.0f);
    public static final Color BOX_IN = new Color(250.0f / 255.0f, 237.0f / 255.0f, 39.0f / 255.0f, 1.0f);

    public static final int CONTAINER_OFFSET_Y = 535;
    public static final int TAB_WIDTH = 75;
    public static final int TAB_HEIGHT = 38;
    public static final int TAB_OFFSET_X = 400;
    public static final int TAB_OFFSET_Y = 500;
    public static final int TAB_HORIZONTAL_SPACING = 10;

    public static final Stroke LINE = new BasicStroke(1.0f);
    public static final Stroke THICK_LINE = new BasicStroke(2.0f);
    public static final float FLY_TERMINAL_VELOCITY = 1900;
}