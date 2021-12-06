package com.Component;

import com.jade.Component;

public class BoxBounds extends Component {
	public float width, height;
	
	public BoxBounds(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void update(double up) {
		
	}
	
	@Override
	public Component copy() {
		return new BoxBounds(width, height);
	}
	
	@Override
	public String serialize (int tabSize) {
		return "";
	}
}