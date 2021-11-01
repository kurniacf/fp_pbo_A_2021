package com.main;

import jade.Window;

public class Main {
	public static void main(String[] ags) {
		Window window = Window.getWindow();
		window.init();
		
		Thread mainThread = new Thread(window);
		mainThread.start();
	}
}