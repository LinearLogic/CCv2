package fostering.evil.christmascrashers.state;

/**
 * Enum containing all the possible game states, which determine which portion of the input,
 * game logic, and rendering code is executed by the game engine.
 * 
 * @author LinearLogic
 * @since 0.0.2
 */
public enum StateType {
	/**
	 * The flag for the {@link IntroState}
	 */
	INTRO,
	
	/**
	 * The flag for the {@link MainMenuState}
	 */
	MAIN_MENU,
	
	/**
	 * The flag for the {@link GameState}
	 */
	GAME,
	
	/**
	 * The flag for the {@link WindowState}
	 */
	WINDOW;
}
