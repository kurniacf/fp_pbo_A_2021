package com.Component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import com.file.Parser;
import com.jade.Component;
import com.jade.Window;
import com.util.Constants;
import com.util.Vector2;

public class TriangleBounds extends Bounds {
    private float base, height, halfWidth, halfHeight, enclosingRadius, x1, x2, x3, y1, y2, y3;
    private final int INSIDE = 0, LEFT = 1, RIGHT = 2, BOTTOM = 4, TOP = 8;  
    // 0000, 0001, 0010, 0100, 1000

    public TriangleBounds(float base, float height) {
        this.type = BoundsType.Triangle;
        this.base = base;
        this.height = height;
        this.halfWidth = this.base / 2.0f;
        this.halfHeight = this.height / 2.0f;
        this.enclosingRadius = Math.max(this.halfHeight, this.halfWidth);
    }

    @Override
    public void start() {
        calculateTransform();
    }

    public static boolean checkCollision(BoxBounds bounds, TriangleBounds triangleBounds) {
        if (triangleBounds.broadPhase(bounds)) {
            return triangleBounds.narrowPhase(bounds);
        }
        return false;
    }

    private boolean broadPhase(BoxBounds bounds) {
        float bRadius = bounds.attachRadius;
        float tRadius = this.enclosingRadius;
        float centerX = this.x2;
        float centerY = this.y2 + halfHeight;
        float plrCenterX = bounds.gameObject.transform.position.x + bounds.halfWidth;
        float plrCenterY = bounds.gameObject.transform.position.y + bounds.halfHeight;
        Vector2 distance = new Vector2(plrCenterX - centerX, plrCenterY - centerY);
        float magSquared = (distance.x * distance.x) + (distance.y * distance.y);
        float radiiSquared = (bRadius + tRadius) * (bRadius + tRadius);
        return magSquared <= radiiSquared;
    }

    private boolean narrowPhase(BoxBounds bounds) {
        Vector2 p1 = new Vector2(x1, y1);
        Vector2 p2 = new Vector2(x2, y2);
        Vector2 p3 = new Vector2(x3, y3);
        Vector2 origin = new Vector2(bounds.gameObject.transform.position.x + (bounds.width / 2.0f), bounds.gameObject.transform.position.y + (bounds.height / 2.0f));
        float rAngle = (float)Math.toRadians(bounds.gameObject.transform.rotation);
        p1 = rotatePoint(rAngle, p1, origin);
        p2 = rotatePoint(rAngle, p2, origin);
        p3 = rotatePoint(rAngle, p3, origin);
        return (boxIntersectingLine(p1, p2, 0, bounds, bounds.gameObject.transform.position) || boxIntersectingLine(p2, p3, 0, bounds, bounds.gameObject.transform.position) || boxIntersectingLine(p3, p1, 0, bounds, bounds.gameObject.transform.position));
    }

    private boolean boxIntersectingLine(Vector2 p1, Vector2 p2, int depth, BoxBounds bounds, Vector2 position) {
        if (depth > 5) {
            System.out.println("Max depth exceeded");
            return true;
        }
        int code1 = computeRegionCode(p1, bounds);
        int code2 = computeRegionCode(p2, bounds);
        if (code1 == 0 && code2 == 0) {
            return true;
        } else if ((code1 & code2) != 0) {
            return false;
        } else if (code1 == 0 || code2 == 0) {
            return true;
        }

        int xmax = (int)(position.x + bounds.width);
        int xmin = (int)(position.x);
        float m = (p2.y - p1.y) / (p2.x - p1.x);
        float b = p2.y - (m * p2.x);

        if ((code1 & LEFT) == LEFT) {
            p1.x = xmin + 1;
        } else if ((code1 & RIGHT) == RIGHT) {
            p1.x = xmax - 1;
        }
        p1.y = (m * p1.x) + b;
        
        if ((code2 & LEFT) == LEFT) {
            p2.x = xmin + 1;
        } else if ((code2 & RIGHT) == RIGHT) {
            p2.x = xmax - 1;
        }
        p2.y = (m * p2.x) + b;
        return boxIntersectingLine(p1, p2, depth + 1, bounds, position);
    }

    private int computeRegionCode(Vector2 point, BoxBounds bounds) {
        int code = INSIDE;
        Vector2 topLeft = bounds.gameObject.transform.position;
        if (point.x < topLeft.x) {
            code |= LEFT;
        } else if (point.x > topLeft.x + bounds.width) {
            code |= RIGHT;
        }
        if (point.y < topLeft.y) {
            code |= TOP;
        } else if (point.y > topLeft.y + bounds.height) {
            code |= BOTTOM;
        }
        return code;
    }

    private Vector2 rotatePoint(double angle, Vector2 p, Vector2 o) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        Vector2 newVector = new Vector2(p.x, p.y);
        newVector.x -= o.x;
        newVector.y -= o.y;

        float newX = (float)((newVector.x * cos) - (newVector.y * sin));
        float newY = (float)((newVector.x * sin) + (newVector.y * cos));
        return new Vector2(newX + o.x, newY + o.y);
    }

    public void calculateTransform() {
        double rAngle = Math.toRadians(gameObject.transform.rotation);
        Vector2 p1 = new Vector2(gameObject.transform.position.x, gameObject.transform.position.y + height);
        Vector2 p2 = new Vector2(gameObject.transform.position.x + halfWidth, gameObject.transform.position.y);
        Vector2 p3 = new Vector2(gameObject.transform.position.x + base, gameObject.transform.position.y + height);
        Vector2 origin = new Vector2(gameObject.transform.position.x + (Constants.TILE_WIDTH / 2.0f), gameObject.transform.position.y + (Constants.TILE_HEIGHT / 2.0f));

        p1 = rotatePoint(rAngle, p1, origin);
        p2 = rotatePoint(rAngle, p2, origin);
        p3 = rotatePoint(rAngle, p3, origin);

        this.x1 = p1.x;
        this.y1 = p1.y;
        this.x2 = p2.x;
        this.y2 = p2.y;
        this.x3 = p3.x;
        this.y3 = p3.y;
    }

    @Override
    public float getWidth() {
        return this.base;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    public float dot(Vector2 v1, Vector2 v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    @Override
    public boolean raycast(Vector2 position) {
        Vector2 v0 = new Vector2(x3 - x1, y3 - y1);
        Vector2 v1 = new Vector2(x2 - x1, y2 - y1);
        Vector2 v2 = new Vector2(position.x - x1, position.y - y1);

        float dot00 = dot(v0, v0);
        float dot01 = dot(v0, v1);
        float dot02 = dot(v0, v2);
        float dot11 = dot(v1, v1);
        float dot12 = dot(v1, v2);

        float invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
        float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

        return (u >= 0.0f) && (v >= 0.0f) && (u + v < 1.0f);
    }

    @Override
    public void draw(Graphics2D graph2) {
        if (isSelected) {
        	graph2.setStroke(Constants.THICK_LINE);
        	graph2.setColor(Color.GREEN);
        	graph2.draw(new Line2D.Float(this.x1 - Window.getScene().camera.position.x, this.y1 - Window.getScene().camera.position.y, this.x2 - Window.getScene().camera.position.x, this.y2 - Window.getScene().camera.position.y));
        	graph2.draw(new Line2D.Float(this.x1 - Window.getScene().camera.position.x, this.y1 - Window.getScene().camera.position.y, this.x3 - Window.getScene().camera.position.x, this.y3 - Window.getScene().camera.position.y));
        	graph2.draw(new Line2D.Float(this.x3 - Window.getScene().camera.position.x, this.y3 - Window.getScene().camera.position.y, this.x2 - Window.getScene().camera.position.x, this.y2 - Window.getScene().camera.position.y));
        	graph2.setStroke(Constants.LINE);
        }
    }

    @Override
    public Component copy() {
        return new TriangleBounds(this.base, this.height);
    }

    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();
        builder.append(beginObjectProperty("TriangleBounds", tabSize));
        builder.append(addFloatProperty("Base", this.base, tabSize + 1, true, true));
        builder.append(addFloatProperty("Height", this.height, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));
        return builder.toString();
    }

    public static TriangleBounds deserialize() {
        float base = Parser.consumeFloatProperty("Base");
        Parser.consume(',');
        float height = Parser.consumeFloatProperty("Height");
        Parser.consumeEndObjectProperty();
        return new TriangleBounds(base, height);
    }
}
