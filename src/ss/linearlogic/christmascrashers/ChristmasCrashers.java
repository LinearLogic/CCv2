package ss.linearlogic.christmascrashers;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.util.Scanner;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import ss.linearlogic.christmascrashers.engine.GLGuru;
import ss.linearlogic.christmascrashers.engine.RenderMonkey;
import ss.linearlogic.christmascrashers.state.GameState;
import ss.linearlogic.christmascrashers.state.IntroState;
import ss.linearlogic.christmascrashers.state.MainMenuState;
import ss.linearlogic.christmascrashers.state.State;
import ss.linearlogic.christmascrashers.state.StateType;
import ss.linearlogic.christmascrashers.util.TextureMonkey;

/**
 * Main class - contains the {@link #ChristmasCrashers(int, int) game object constructor} and {@link #main(String[]) program entry point}
 * 
 * @author LinearLogic
 * @version 0.3.6
 */
public class ChristmasCrashers {

	/**
	 * The current version of the program
	 */
	public static final String VERSION = "0.3.6";

	/**
	 * Indicates whether the program is running in debug mode
	 */
	private static boolean debugModeEnabled;

	/**
	 * Width of the game window in pixels
	 */
	private static int windowWidth;

	/**
	 * Height of the game window, in pixels
	 */
	private static int windowHeight;

	/**
	 * Current game state flag - determines which state is handled in the input/logic/rendering loop.
	 * It is initialized to the introduction state, and is updated with each pass through the game loop.
	 */
	private static StateType state;

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
	private static long lastFrame;

	/**
	 * Game object constructor - initializes openGL, starts the timer, and runs the game logic and rendering loop.
	 * 
	 * @param width The pixel width of the game window
	 * @param height The height of the game window
	 */
	public ChristmasCrashers(int width, int height) {
		windowWidth = width;
		windowHeight = height;
		if (debugModeEnabled)
			System.out.println("Constructing the game object. Window dimensions: " + width + "x" + height + " pixels.");
		initDisplay(windowWidth, windowHeight);
		GLGuru.setXDisplacement(0);
		GLGuru.setYDisplacement(0);
		GLGuru.setZDisplacement(0);
		GLGuru.initGL2D(windowWidth, windowHeight);
		state = StateType.INTRO;
		loadStates();
		initTimer();
		running = true;
		IntroState.initialize();
		TextureMonkey.init();

		// Logic/rendering loop
		while(running) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Covers 2D and 3D

			handleState(); // This calls the input handling, logic execution, and rendering for the current state

			Display.update();
			Display.sync(60); // Framerate = 60 fps
			if (Display.isCloseRequested())
				running = false;
		}
		if (debugModeEnabled)
			System.out.println("Destroying the openGL context and closing the game window.");
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
			Display.setVSyncEnabled(true); // Framerate cannot exceed the computer monitor's refresh rate (prevents image tearing)
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructs a State subclass object for each state type.
	 */
	private void loadStates() {
		if (debugModeEnabled)
			System.out.println("Loading game states.");
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
				introState.logic();
				introState.draw();
				break;
			case MAIN_MENU:
				state = mainMenuState.handleInput();
				mainMenuState.logic();
				mainMenuState.draw();
				break;
			case GAME:
				RenderMonkey.renderBackground(0.3, 0.4, 1.0);
				state = gameState.handleInput();
				gameState.logic();
				gameState.draw();
				break;
			case WINDOW:
				// This guy's a bit more complex...
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
	public static int getDelta() {
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
	 * @return The {@link #windowWidth width} of the game window
	 */
	public static int getWindowWidth() {
		return windowWidth;
	}

	/**
	 * @return The {@link #windowHeight height} of the game window
	 */
	public static int getWindowHeight() {
		return windowHeight;
	}

	/**
	 * @return The value of the {@link #debugModeEnabled} flag, which is 'true' iff debug mode is enabled
	 */
	public static boolean isDebugModeEnabled() {
		return debugModeEnabled;
	}

	/**
	 * Sets the value of {@link #state} to 'false', causing the program to exit the game loop.
	 */
	public static void exitGameLoop() {
		if (debugModeEnabled)
			System.out.println("Received quit command, exiting game loop.");
		running = false;
	}

	/**
	 * Program entry point, creates a game object after determining whether to run the program in debug mode.
	 * 
	 * @param args ...
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Christmas Crashers! Run in DEBUG mode? (Y/N)");
		Scanner sc = new Scanner(System.in);
		while(true) {
			String response = sc.nextLine();
			if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")) {
				debugModeEnabled = true;
				break;
			}
			else if (response.equalsIgnoreCase("n") || response.equalsIgnoreCase("no")) {
				debugModeEnabled = false;
				break;
			}
			else
				System.out.println("Invalid answer, please type 'Y' or 'N'");
		}
		sc.close();
		new ChristmasCrashers(800, 480);
	}
}
