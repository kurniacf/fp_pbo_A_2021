package com.Component;

import com.jade.Component;
import com.jade.GameObject;
import com.util.Vector2;

enum BoundsType {
    Box,
    Triangle
}

public abstract class Bounds extends Component {
    public BoundsType type;
    public boolean isSelected;
    abstract public float getWidth();
    abstract public float getHeight();
    abstract public boolean raycast(Vector2 position);

    public static boolean checkCollision(Bounds bo1, Bounds bo2) {
        if (bo1.type == bo2.type && bo1.type == BoundsType.Box) {
            return BoxBounds.checkCollision((BoxBounds)bo1, (BoxBounds)bo2);
        } else if (bo1.type == BoundsType.Box && bo2.type == BoundsType.Triangle) {
            return TriangleBounds.checkCollision((BoxBounds)bo1, (TriangleBounds)bo2);
        } else if (bo1.type == BoundsType.Triangle && bo2.type == BoundsType.Box) {
            return TriangleBounds.checkCollision((BoxBounds)bo2, (TriangleBounds)bo1);
        }
        return false;
    }

    public static void resolveCollision(Bounds bound, GameObject gameObject) {
        if (bound.type == BoundsType.Box) {
            BoxBounds box = (BoxBounds)bound;
            box.resolveCollision(gameObject);
        } else if (bound.type == BoundsType.Triangle) {
        	gameObject.getComponent(Player.class).die();
        }
    }
}