package ss.linearlogic.christmascrashers.state;

import static org.lwjgl.opengl.GL11.glTranslated;

import org.lwjgl.input.Keyboard;

import ss.linearlogic.christmascrashers.ChristmasCrashers;
import ss.linearlogic.christmascrashers.engine.GLGuru;
import ss.linearlogic.christmascrashers.entity.Player;
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
	 * The {@link Player} object controlled by input from this client
	 */
	private static Player mainPlayer;

	/**
	 * Constructor - adds the {@link #importantKeys}
	 */
	public GameState() {
		importantKeys.add(Keyboard.KEY_RIGHT);
		importantKeys.add(Keyboard.KEY_LEFT);
		importantKeys.add(Keyboard.KEY_UP);
		importantKeys.add(Keyboard.KEY_DOWN);
		importantKeys.add(Keyboard.KEY_ESCAPE);
	}

	@Override
	public StateType handleInput() {
		// Adjust player's speed accordingly
		checkKeyStates();
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !keyDown) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("Switching to MainMenu state.");
			MainMenuState.setKeyDown(true);
			MainMenuState.initialize(false); // Skip the fade-in animation
			return StateType.MAIN_MENU;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			keyDown = true;
			mainPlayer.getMovementVector().setX(mainPlayer.getMovementVector().getX() + 5);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			keyDown = true;
			mainPlayer.getMovementVector().setX(mainPlayer.getMovementVector().getX() - 5);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			keyDown = true;
			mainPlayer.getMovementVector().setY(mainPlayer.getMovementVector().getY() + 5);

		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			keyDown = true;
			mainPlayer.getMovementVector().setY(mainPlayer.getMovementVector().getY() - 5);
		}
		mainPlayer.updatePosition();
		glTranslated(-mainPlayer.getMovementVector().getX(), -mainPlayer.getMovementVector().getY(), 0);
		mainPlayer.clearMovementVector();
		
		GLGuru.setXDisplacement(mainPlayer.getPixelX() + (mainPlayer.getSprite().getWidth() - ChristmasCrashers.getWindowWidth()) / 2);
		GLGuru.setYDisplacement(mainPlayer.getPixelY() + (mainPlayer.getSprite().getHeight() - ChristmasCrashers.getWindowHeight()) / 2);
		return StateType.GAME;
	}

	@Override
	public void logic() {
		
	}

	@Override
	public void draw() {
		if (currentLevel != null)
			currentLevel.draw();
		mainPlayer.draw();
	}

	/**
	 * Sets the gamestate to active
	 */
	public static void initialize() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Initializing Game state");
		currentLevel = WorldManager.getWorld(0).getLevel(0);
		mainPlayer = new Player(5, 1); // Initialize the user's player
		int xOffset = (int) (mainPlayer.getPixelX() + (mainPlayer.getSprite().getWidth() - ChristmasCrashers.getWindowWidth()) / 2);
		int yOffset = (int) (mainPlayer.getPixelY() + (mainPlayer.getSprite().getHeight() - ChristmasCrashers.getWindowHeight()) / 2);
		System.out.println(GLGuru.getXDisplacement());
		glTranslated(GLGuru.getXDisplacement() - xOffset, GLGuru.getYDisplacement() - yOffset, -GLGuru.getZDisplacement()); // Reset the camera displacement

		GLGuru.setXDisplacement(0);
		GLGuru.setYDisplacement(0);
		GLGuru.setZDisplacement(0);
	}

	/**
	 * @return The {@link #currentLevel}
	 */
	public static Level getCurrentLevel() {
		return currentLevel;
	}
}
