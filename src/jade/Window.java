package jade;

import javax.swing.JFrame;

import util.Time;

public class Window extends JFrame implements Runnable{
	
	private static Window window = null;
	private boolean isRunning = true;
	
	public Window() {
		this.setSize(1280, 720);
		this.setTitle("Shape Rush");
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
	
	public void init() {
		
	}
	
	public static Window getWindow() {
		if(Window.window == null) {
			Window.window = new  Window();
		}
		return Window.window;
	}
	
	public void update(double up) {
		System.out.println(up);
	}
	
	@Override
	public void run() {
		double lastFrameTime = 0.0;
		try {
			while(isRunning) {
				double time = Time.getTime();
				double deltaTime = time - lastFrameTime;
				lastFrameTime = time;
				update(time);
			}
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
}