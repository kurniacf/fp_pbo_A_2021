package com.Component;

import com.jade.Component;
import com.util.Constants;
import com.util.Vector2;

public class Rigidbody extends Component {
    public Vector2 velocity;

    public Rigidbody(Vector2 velocity) {
        this.velocity = velocity;
    }

    @Override
    public void update(double up) {
        gameObject.transform.position.y += velocity.y * up;
        gameObject.transform.position.x += velocity.x * up;
        velocity.y += Constants.GRAVITY * up;

        if (Math.abs(velocity.y) > Constants.TERMINAL_VELOCITY) {
            velocity.y = Math.signum(velocity.y) * Constants.TERMINAL_VELOCITY;
        }
    }

    @Override
    public Component copy() {
        return null;
    }

    @Override
    public String serialize(int tabSize) {
        return "";
    }
}