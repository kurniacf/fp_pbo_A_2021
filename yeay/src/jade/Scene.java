package jade;

import java.awt.Graphics2D;

public abstract class Scene {
	String name;
	
	public void Scene(String name) {
		this.name = name;
	}
	
	public abstract void init();
	public abstract void update(double up);
	public abstract void draw(Graphics2D g2);
	
}
