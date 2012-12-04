package fostering.evil.christmascrashers.state;

import org.lwjgl.input.Keyboard;

import fostering.evil.christmascrashers.ChristmasCrashers;

/**
 * The main menu state presents the user with clickable options, such as "start game" and "exit",
 * and runs a starfield animation in the background while active.
 * 
 * @author LinearLogic
 *@since 0.0.2
 */
public class MainMenuState extends State {

	/**
	 * Constructor - initializes the keyDown value to false and populates the importantKeys ArrayList
	 * (inherited from the {@link State} superclass).
	 */
	public MainMenuState() {
		keyDown = false;
		addImportantKey(Keyboard.KEY_ESCAPE);
	}
	@Override
	public StateType handleInput() {
		// TODO: mouse handling
		checkKeyStates();
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !keyDown) {
			keyDown = true;
			// Open quitgame prompt window?
			ChristmasCrashers.exitGameLoop();
		}
		return StateType.MAIN_MENU;
	}

	@Override
	public void logic() {

	}

	@Override
	public void draw() {

	}
	
	/**
	 * Begins the main menu starfield animation.
	 */
	public static void initialize() {
		// Animation
	}
}
