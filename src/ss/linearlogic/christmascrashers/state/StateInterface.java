package ss.linearlogic.christmascrashers.state;

/**
 * Game state interface, containing the input handling, logic, and rendering methods to be implemented
 * in full by each state class and executed in the appropriate class (determined by the value of the
 * current GameState). 
 * 
 * @author LinearLogic
 * @since 0.0.2
 */
public interface StateInterface {

	/**
	 * Registers and responds to keyboard and mouse input, and returns the new state determined based on input.
	 * 
	 * @return The new state that will be switched on in the next iteration of the main loop.
	 */
	void handleInput();

	/**
	 * Executes the logic for each graphical and non-graphical object.
	 */
	void logic();

	/**
	 * Renders the frame (and all graphical objects in it) for the game state.
	 */
	void draw();

	/**
	 * Sets the {@link ChristmasCrashers#currentState} to this state and runs the specific initialization code (such
	 * as starting an animation or loading a world) for this state.
	 */
	void initialize();

	/**
	 * Adds the specified keyID to the {@link State#importantKeys} list.
	 * 
	 * @param keyID The integer ID of the keyboard key
	 */
	public void addImportantKey(int keyID);

	/**
	 * Remvoes the specified keyID from the {@link State#importantKeys} list.
	 * 
	 * @param keyID The integer ID of the keyboard key
	 */
	public void removeImportantKey(int keyID);
}
