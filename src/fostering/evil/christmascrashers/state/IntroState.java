package fostering.evil.christmascrashers.state;

import org.lwjgl.input.Keyboard;

/**
 * The introduction state is entered first. While active, it runs the loading animation, and then prompts
 * user to press 'Enter' to begin. The player can skip the intro at any point by pressing the 'escape' key.
 * 
 * @author LinearLogic
 * @since 0.0.2
 */
public class IntroState extends State {

	/**
	 * Represents the status of the intro animation sequence: 0 if not started, 1 if in progress, 2 if complete.
	 */
	private int animationStatus;
	
	/**
	 * Represents the state of the game worlds: 'true' if they have all been loaded, otherwise 'false'.
	 * If this value is true, the world-loader thread will not be called in the {@link #initialize()} method.
	 */
	boolean worldsLoaded;
	
	/**
	 * Constructor - initializes the {@link #finishedLoading} and {@link State#keyDown} values to 'false',
	 * and populates the {@link State#importantKeys} ArrayList. Note that the splash animation and
	 * game-loader thread are not started here, but in the {@link #initialize()} method.
	 */
	public IntroState() {
		keyDown = false;
		addImportantKey(Keyboard.KEY_ESCAPE);
		addImportantKey(Keyboard.KEY_RETURN);
		animationStatus = 0;
		worldsLoaded = false; // TODO: change this value to the worldsLoaded value in the world manager class
	}
	
	@Override
	public StateType handleInput() {
		checkKeyStates();
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !keyDown) {
			MainMenuState.setKeyDown(true); // Prevents repeat key events after switching the active state
			reset();
			// TODO: if debug System.out.println("Switching to MainMenu state");
			// Cancel animation
			return StateType.MAIN_MENU;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) && (animationStatus == 2) && !keyDown) { // Animation is complete
			MainMenuState.setKeyDown(true);
			reset();
			// TODO: if debug System.out.println("Switching to MainMenu state");
			return StateType.MAIN_MENU;
		}
		return StateType.INTRO;
	}

	@Override
	public void logic() {
		
	}

	@Override
	public void draw() {
		
	}
	
	/**
	 * Begins the loading animation and executes the game-loader thread.
	 */
	public void initialize() {
		if (animationStatus != 0) // Not sure how this would happen, but just to make sure...
			animationStatus = 0;
		if (!worldsLoaded) {
			// load worlds here
		}
	}
	
	/**
	 * Resets the Intro state so that it can be played through again.
	 */
	private void reset() {
		//worldsLoaded = getWorldsLoadedFromWorldManager
		//TODO: reset animation
		animationStatus = 0;
	}
}
