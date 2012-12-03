package fostering.evil.christmascrashers;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import fostering.evil.christmascrashers.state.GameState;
import fostering.evil.christmascrashers.state.IntroState;
import fostering.evil.christmascrashers.state.MainMenuState;
import fostering.evil.christmascrashers.state.State;
import fostering.evil.christmascrashers.state.StateType;

/**
 * Main class - contains the {@link #ChristmasCrashers(int, int) game object constructor} and {@link #main(String[]) program entry point}
 * 
 * @author LinearLogic
 * @version 0.0.2
 */
public class ChristmasCrashers {
	
	/**
	 * Current game state flag - determines which state is handled in the input/logic/rendering loop.
	 * It is initialized to the introduction state, and is updated with each pass through the game loop.
	 */
	private static StateType state;
	
	// States could also be handled with a superclass system, but I like variety
	/**
	 * IntroState object, initialized in the {@link #loadStates()} method.
	 */
	private State introState;
	
	/**
	 * MainMenuState object, initialized in the {@link #loadStates()} method.
	 */
	private State mainMenuState;
	
	/**
	 * GameState object, initialized in the {@link #loadStates()} method.
	 */
	private State gameState;
	
	/**
	 * Set to 'true' in the {@link #ChristmasCrashers(int, int) contructor}, this boolean variable will cause
	 * the program to exit the game loop if set to 'false'.
	 */
	private static boolean running = false;
	
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
		// TODO: if debug System.out.println("Constructing the game object. Window dimensions: " + width + "x" + height + " pixels."
		initDisplay(width, height);
		initOpenGL(width, height);
		state = StateType.INTRO;
		loadStates();
		initTimer();
		running = true;
		
		// Logic/rendering loop
		while(running) {
			
			handleState(); // This includes input handling, logic execution, and rendering
			
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
	
	private void loadStates() {
		introState = new IntroState();
		mainMenuState = new MainMenuState();
		gameState = new GameState();
	}
	
	/**
	 * Initializes the {@link #lastFrame} value to the current system time
	 */
	private void initTimer() {
		lastFrame = getTime();
	}
	
	/**
	 * Calls the handleInput(), draw(), and render() methods in the state class determined by the {@link #state} flag.
	 */
	private void handleState() {
		switch(state) {
			case INTRO:
				state = introState.handleInput();
				break;
			case MAIN_MENU:
				state = mainMenuState.handleInput();
				break;
			case GAME:
				state = gameState.handleInput();
				break;
			case WINDOW:
				//state = WindowManager.handleInput();
				break;
		}
	}
	
	/**
	 * @return The adjusted current system time
	 */
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/**
	 * Returns the time between the current frame and the last (cast to an int value) and updates the
	 * lastFrame value with the current system time.
	 * 
	 * @return The integer time between frames
	 */
	public int getDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = getTime();
		return delta;
	}
	
	/**
	 * @return The current {@link #state game state flag}
	 */
	public static StateType getState() {
		return state;
	}
	
	/**
	 * Sets the value of {@link #state} to 'false', causing the program to exit the game loop.
	 */
	public static void exitGameLoop() {
		// TODO: if debug System.out.println("Received quit command, exiting game loop...);
		running = false;
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
