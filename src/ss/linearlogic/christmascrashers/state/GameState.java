package ss.linearlogic.christmascrashers.state;

import static org.lwjgl.opengl.GL11.glTranslated;

import org.lwjgl.input.Keyboard;

import ss.linearlogic.christmascrashers.engine.GLGuru;
import ss.linearlogic.christmascrashers.world.Level;
import ss.linearlogic.christmascrashers.world.WorldManager;

/**
 * The game state is where most of the user's time is spent, and includes handling of all relevant forms
 * of input and renders all the in-game objects and entities.
 * 
 * @author LinearLogic
 * @since 0.0.2
 */
public class GameState extends State {

	/**
	 * The {@link Level} to move the player around and render
	 */
	private static Level currentLevel;

	/**
	 * Constructor - adds the {@link #importantKeys}
	 */
	public GameState() {
		importantKeys.add(Keyboard.KEY_UP);
		importantKeys.add(Keyboard.KEY_RIGHT);
		importantKeys.add(Keyboard.KEY_LEFT);
		importantKeys.add(Keyboard.KEY_ESCAPE);
	}

	@Override
	public StateType handleInput() {
		// Adjust player's speed accordingly
		checkKeyStates();
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !keyDown) {
			MainMenuState.initialize(false); // Skip the fade-in animation
			return StateType.MAIN_MENU;
		}
		return StateType.GAME;
	}

	@Override
	public void logic() {
		
	}

	@Override
	public void draw() {
		if (currentLevel != null)
			currentLevel.draw();
	}

	/**
	 * Sets the gamestate to active
	 */
	public static void initialize() {
		glTranslated(-GLGuru.getXDisplacement(), -GLGuru.getYDisplacement(), -GLGuru.getZDisplacement()); // Reset the camera displacement
		GLGuru.setXDisplacement(0);
		GLGuru.setYDisplacement(0);
		GLGuru.setZDisplacement(0);
		currentLevel = WorldManager.getWorld(0).getLevel(0);
	}

	/**
	 * @return The {@link #currentLevel}
	 */
	public Level getCurrentLevel() {
		return currentLevel;
	}
}
