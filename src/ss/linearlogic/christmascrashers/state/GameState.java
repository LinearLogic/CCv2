package ss.linearlogic.christmascrashers.state;

import static org.lwjgl.opengl.GL11.glTranslated;

import org.lwjgl.input.Keyboard;

import ss.linearlogic.christmascrashers.ChristmasCrashers;
import ss.linearlogic.christmascrashers.engine.GLGuru;
import ss.linearlogic.christmascrashers.engine.RenderMonkey;
import ss.linearlogic.christmascrashers.entity.Player;
import ss.linearlogic.christmascrashers.world.Level;
import ss.linearlogic.christmascrashers.world.World;

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
	private Level currentLevel;

	/**
	 * The game world the player is currently in
	 */
	private World currentWorld;

	/**
	 * The {@link Player} object controlled by input from this client
	 */
	private Player mainPlayer;

	/**
	 * Constructor - adds the {@link #importantKeys}
	 */
	public GameState() {
		importantKeys.add(Keyboard.KEY_RIGHT);
		importantKeys.add(Keyboard.KEY_D);
		importantKeys.add(Keyboard.KEY_LEFT);
		importantKeys.add(Keyboard.KEY_A);
		importantKeys.add(Keyboard.KEY_UP);
		importantKeys.add(Keyboard.KEY_W);
		importantKeys.add(Keyboard.KEY_ESCAPE);
		importantKeys.add(Keyboard.KEY_S);
	}

	@Override
	public void handleInput() {
		// Adjust player's speed accordingly
		checkKeyStates();
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !keyDown) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("Switching to MainMenu state.");
			ChristmasCrashers.setCurrentState(StateType.MAIN_MENU);
			((MainMenuState) ChristmasCrashers.getState(StateType.MAIN_MENU)).setAnimationProgress(1000); // Skip the fade-in animation
			return;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)) {
			keyDown = true;
			mainPlayer.getMovementVector().setX(mainPlayer.getMovementVector().getX() + 5);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) {
			keyDown = true;
			mainPlayer.getMovementVector().setX(mainPlayer.getMovementVector().getX() - 5);
		}
		if ((Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) && !mainPlayer.canFly() && !mainPlayer.isAirborne()) {
			keyDown = true;
			mainPlayer.setAirborne(true);
			mainPlayer.getMovementVector().setY(mainPlayer.getMovementVector().getY() + 12.5f);
		}
		int posX = mainPlayer.getSprite().getX();
		int posY = mainPlayer.getSprite().getY();
		mainPlayer.updatePosition();
		glTranslated(-(mainPlayer.getSprite().getX() - posX), -(mainPlayer.getSprite().getY() - posY), 0);
		mainPlayer.getMovementVector().setX(0);
		
		GLGuru.setXDisplacement(mainPlayer.getPixelX() + (mainPlayer.getSprite().getWidth() - ChristmasCrashers.getWindowWidth()) / 2);
		GLGuru.setYDisplacement(mainPlayer.getPixelY() + (mainPlayer.getSprite().getHeight() - ChristmasCrashers.getWindowHeight()) / 2);
	}

	@Override
	public void logic() {
		
	}

	@Override
	public void draw() {
		RenderMonkey.renderBackground(0.3, 0.4, 1.0);
		if (currentLevel != null) {
			currentLevel.draw();
		}
		mainPlayer.draw();
	}

	/**
	 * Sets the gamestate to active
	 */
	public void initialize() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Initializing Game state");
		currentWorld.load();
		for (int i = 0; i < 5; i++)
			if (currentWorld.getLevel(i) != null)
				currentLevel = currentWorld.getLevel(i);
		mainPlayer = new Player(5, 1); // Initialize the user's player
		int xOffset = (int) (mainPlayer.getPixelX() + (mainPlayer.getSprite().getWidth() - ChristmasCrashers.getWindowWidth()) / 2);
		int yOffset = (int) (mainPlayer.getPixelY() + (mainPlayer.getSprite().getHeight() - ChristmasCrashers.getWindowHeight()) / 2);
		glTranslated(GLGuru.getXDisplacement() - xOffset, GLGuru.getYDisplacement() - yOffset, -GLGuru.getZDisplacement()); // Reset the camera displacement
		GLGuru.setXDisplacement(0);
		GLGuru.setYDisplacement(0);
		GLGuru.setZDisplacement(0);
		GLGuru.initGL2D();
	}

	/**
	 * @return The {@link #currentLevel} being played
	 */
	public Level getCurrentLevel() {
		return currentLevel;
	}

	/**
	 * Sets the {@link #currentLevel} to the specified {@link Level} object
	 * 
	 * @param level
	 */
	public void setCurrentLevel(Level level) {
		currentLevel = level;
	}

	/**
	 * @return The game world the player is currently in
	 */
	public World getCurrentWorld() {
		return currentWorld;
	}

	/**
	 * Sets the current game world to the specified {@link World} object
	 * @param world
	 */
	public void setCurrentWorld(World world) {
		this.currentWorld = world;
	}
}
