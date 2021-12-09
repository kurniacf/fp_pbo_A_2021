package com.Component;

import java.awt.Graphics2D;
import com.dataStructure.AssetPool;
import com.jade.Component;
import com.jade.GameObject;
import com.jade.Window;
import com.util.Constants;

public class ParallaxBackground extends Component {
    public int width, height, timeStep = 0;
    public Sprite sprite;
    public GameObject[] backgrounds;
    private float speed = 80.0f;
    private Ground ground;
    private boolean followGround;

    public ParallaxBackground(String file, GameObject[] backgrounds, Ground ground, boolean followGround) {
        this.sprite = AssetPool.getSprite(file);
        this.width = this.sprite.width;
        this.height = this.sprite.height;
        this.backgrounds = backgrounds;
        this.ground = ground;

        if (followGround) {
        	this.speed = Constants.PLAYER_SPEED - 35;
        }
        this.followGround = followGround;
    }

    @Override
    public void update(double up) {
        if (backgrounds == null) {
        	return;
        }
        this.timeStep++;
        this.gameObject.transform.position.x -= up * speed;
        this.gameObject.transform.position.x = (float)Math.floor(this.gameObject.transform.position.x);
        if (this.gameObject.transform.position.x < -width) {
            float maxX = 0;
            int otherTimeStep = 0;
            for (GameObject go : backgrounds) {
                if (go.transform.position.x > maxX) {
                    maxX = go.transform.position.x;
                    otherTimeStep = go.getComponent(ParallaxBackground.class).timeStep;
                }
            }
            if (otherTimeStep == this.timeStep) {
                this.gameObject.transform.position.x = maxX + width;
            } else {
                this.gameObject.transform.position.x = (float) Math.floor((maxX + width) - (up * speed));
            }
        }
        if (this.followGround) {
            this.gameObject.transform.position.y = ground.gameObject.transform.position.y;
        }
    }

    @Override
    public void draw(Graphics2D graph2) {
        if (followGround) {
        	graph2.drawImage(this.sprite.image, (int)this.gameObject.transform.position.x, (int)(this.gameObject.transform.position.y - Window.getWindow().getCurrentScene().camera.position.y), width, height, null);
        } else {
            int height = Math.min((int)(ground.gameObject.transform.position.y - Window.getWindow().getCurrentScene().camera.position.y), Constants.SCREEN_HEIGHT);
            graph2.drawImage(this.sprite.image, (int)this.gameObject.transform.position.x, (int)this.gameObject.transform.position.y, width, Constants.SCREEN_HEIGHT, null);
            graph2.setColor(Constants.GROUND_COLOR);
            graph2.fillRect((int)this.gameObject.transform.position.x, height, width, Constants.SCREEN_HEIGHT);
        }
    }

    @Override
    public Component copy() {
        return null;
    }

    @Override
    public String serialize(int tabSize) {
        return null;
    }
}
