package ss.linearlogic.christmascrashers;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.util.HashMap;
import java.util.Scanner;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import ss.linearlogic.christmascrashers.engine.GLGuru;
import ss.linearlogic.christmascrashers.state.GameState;
import ss.linearlogic.christmascrashers.state.IntroState;
import ss.linearlogic.christmascrashers.state.LevelEditorState;
import ss.linearlogic.christmascrashers.state.MainMenuState;
import ss.linearlogic.christmascrashers.state.State;
import ss.linearlogic.christmascrashers.state.StateType;
import ss.linearlogic.christmascrashers.util.TextureMonkey;

/**
 * Main class - contains the {@link #ChristmasCrashers(int, int) game object constructor} and {@link #main(String[]) program entry point}
 * 
 * @author LinearLogic
 * @version 0.4.7
 */
public class ChristmasCrashers {

	/**
	 * The current version of the program
	 */
	public static final String VERSION = "0.4.7";

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
	 * A HashMap containing {@link StateType} and {@link State} subclass pairs, used to set the current state in
	 * the {@link #setCurrentState} method.
	 */
	private static HashMap<StateType, State> states = new HashMap<StateType, State>();

	/**
	 * The game state that is currently active and is handled and rendered in the main game loop
	 */
	private static State currentState;

	/**
	 * Set to 'true' in the {@link #ChristmasCrashers(int, int) contructor}, this boolean variable will cause
	 * the program to exit the game loop if set to 'false'.
	 */
	private static boolean running = false;

	/**
	 * Whether to load a new game instance after destroying the current one
	 */
	private static boolean reload;

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
		TextureMonkey.init(); // The TextureMonkey MUST be initialized before the game states to prevent NPEs during texture retrieval
		loadStates();
		initTimer();
		running = true;
		reload = false;
		currentState = states.get(StateType.INTRO);
		currentState.initialize();

		// Logic/rendering loop
		while(running) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Covers 2D and 3D

			currentState.handleInput();
			currentState.logic();
			currentState.draw();

			Display.update();
			Display.sync(60); // Framerate = 60 fps
			if (Display.isCloseRequested()) {
				running = false;
				reload = false;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_F5)) {
				running = false;
				reload = true;
			}
		}
		if (debugModeEnabled)
			System.out.println("Destroying the openGL context and closing the game window.");
		Display.destroy();
		if (reload)
			new ChristmasCrashers(windowWidth, windowHeight);
		else
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
		states.put(StateType.INTRO, new IntroState());
		states.put(StateType.MAIN_MENU, new MainMenuState());
		states.put(StateType.GAME, new GameState());
		states.put(StateType.LEVEL_EDITOR, new LevelEditorState());
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
	 * @param type
	 * @return The {@link State} subclass paired with the specified {@link StateType} in the {@link #states} HashMap
	 */
	public static State getState(StateType type) {
		return states.get(type);
	}
	/**
	 * Sets the {@link #currentState} to the {@link State} subclass that corresponds with the specified {@link StateType}.
	 * 
	 * @param type
	 */
	public static void setCurrentState(StateType type) {
		currentState = states.get(type);
		currentState.setKeyDown(true);
		currentState.initialize();
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
