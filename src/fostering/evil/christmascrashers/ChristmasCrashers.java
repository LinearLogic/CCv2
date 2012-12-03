package fostering.evil.christmascrashers;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * Main class - contains the {@link #ChristmasCrashers(int, int) game object constructor} and {@link #main(String[]) program entry point}
 * 
 * @author LinearLogic
 * @version 0.0.1
 */
public class ChristmasCrashers {
	
	/**
	 * Set to 'true' in the {@link #ChristmasCrashers(int, int) contructor}, this boolean variable will cause the program to exit the
	 * game logic and rendering loop if set to 'false'.
	 */
	private boolean running = false;
	
	/**
	 * The system time (retrieved using {@link #getTime()}) at which the most recent frame was rendered.
	 */
	private long lastFrame;
	
	/**
	 * Game object constructor - initializes openGL, starts the timer, and runs the game logic and rendering loop.
	 * 
	 * @param width The pixel width of the game window
	 * @param height The height of the game window
	 */
	public ChristmasCrashers(int width, int height) {
		initDisplay(width, height);
		initOpenGL(width, height);
		initTimer();
		running = true;
		
		// Logic/rendering loop
		while(running) {
			
			// TODO: input handling and game logic
			// TODO: rendering
			
			Display.update();
			Display.sync(60);
			if (Display.isCloseRequested())
				running = false;
		}
		Display.destroy();
		System.exit(0);
	}
	
	/**
	 * Initializes the game display window with the given pixel dimensions
	 * 
	 * @param width The pixel width of the window
	 * @param height The pixel height of the window
	 */
	private void initDisplay(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("ChristmasCrashers");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes openGL for 2D graphics with the given pixel glOrtho(...) width and height
	 * 
	 * @param width The pixel width to be passed to the glOrtho(...) method
	 * @param height The pixel height to be passed to the glOrtho(...) method
	 */
	private void initOpenGL(int width, int height) {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 0, 480, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
	}
	
	/**
	 * Initializes the {@link #lastFrame} value to the current system time
	 */
	private void initTimer() {
		lastFrame = getTime();
	}
	
	/**
	 * @return The adjusted current system time
	 */
	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/**
	 * Returns the time between the current frame and the last (cast to an int value) and updates the
	 * {@link #lastFrame} value with the current system time.
	 * 
	 * @return The integer time between frames
	 */
	private int getDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = getTime();
		return delta;
	}
	
	/**
	 * Program entry point, creates a game object
	 * 
	 * @param args ...
	 */
	public static void main(String[] args) {
		new ChristmasCrashers(640, 480);
	}
}
