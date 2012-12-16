package ss.linearlogic.christmascrashers.state;

import static org.lwjgl.opengl.GL11.glTranslated;

import java.io.File;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

import ss.linearlogic.christmascrashers.ChristmasCrashers;
import ss.linearlogic.christmascrashers.engine.GLGuru;
import ss.linearlogic.christmascrashers.engine.RenderMonkey;
import ss.linearlogic.christmascrashers.util.TextureMonkey;
import ss.linearlogic.christmascrashers.world.Level;
import ss.linearlogic.christmascrashers.world.World;
import ss.linearlogic.christmascrashers.world.WorldManager;

/**
 * The Level Editor allows the user to select a world and edit the contents of its levels, both objects and entities.
 * The arrow keys are used to navigate the level, while mouse input is used to select objects and entities from a
 * list at the righthand side of the screen and to place them in the level.
 * <p>Cancelling a level edit will discard all changes made to the level during the editing session, while saving the level
 * will write its contents to its disk location. If the program is forcibly closed (by closing or refreshing the game window),
 * a prompt will appear asking the user if they wish to save the level.
 * 
 * @author LinearLogic
 * @since 0.4.1
 */
public class LevelEditorState extends State {

	/**
	 * The font used to render strings in the Level Editor (
	 */
	private TrueTypeFont font;

	/**
	 * The {@link World} object currently being edited
	 */
	private World currentWorld;

	/**
	 * The {@link Level} object currently being edited
	 */
	private Level currentLevel;

	/**
	 * The {@link NavigationButton} the mouse is currently on
	 */
	private NavigationButton highlightedLevelButton;

	/**
	 * The {@link NavigationButton} corresponding to the level currently open in the editor window
	 */
	private NavigationButton selectedLevelButton;

	/**
	 * Constructor - loads the font to be used in the Level Editor window, adds important keys
	 */
	public LevelEditorState() {
		font = RenderMonkey.loadFont("files" + File.separator + "FIXEDSYS500C.TTF", 24);
		importantKeys.add(Keyboard.KEY_RIGHT);
		importantKeys.add(Keyboard.KEY_LEFT);
		importantKeys.add(Keyboard.KEY_UP);
		importantKeys.add(Keyboard.KEY_DOWN);
		importantKeys.add(Keyboard.KEY_ESCAPE);
	}

	@Override
	public void handleInput() {
		checkKeyStates();
		// Mouse handling
		int x = Mouse.getX();
		int y = Mouse.getY();
		highlightedLevelButton = NavigationButton.NONE;
		for (NavigationButton button : NavigationButton.values())
			if (button.isHighlighted(x, y)) {
				if (button.texture == null)
					highlightedLevelButton = button;
			}
		if (Mouse.isButtonDown(0) && highlightedLevelButton != NavigationButton.NONE) {
			selectedLevelButton = highlightedLevelButton;
			switch(selectedLevelButton) {
				case LEVEL_ONE:
					currentLevel = currentWorld.getLevel(0);
					return;
				case LEVEL_TWO:
					currentLevel = currentWorld.getLevel(1);
					return;
				case LEVEL_THREE:
					currentLevel = currentWorld.getLevel(2);
					return;
				case LEVEL_FOUR:
					currentLevel = currentWorld.getLevel(3);
					return;
				case LEVEL_FIVE:
					currentLevel = currentWorld.getLevel(4);
					return;
				default:
					break;
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !keyDown) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("Switching to MainMenu state.");
			ChristmasCrashers.setCurrentState(StateType.MAIN_MENU);
			((MainMenuState) ChristmasCrashers.getState(StateType.MAIN_MENU)).setAnimationProgress(1000); // Skip the fade-in animation
		}
		if (!keyDown)
			ChristmasCrashers.getDelta(); // Clear the buildup in the delta count
		Vector2f displacement = new Vector2f(0, 0);
		int delta = ChristmasCrashers.getDelta();
		// Panning speeds are in pixels per second
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			keyDown = true;
			displacement.setX(300 * (float) delta / 1000);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			keyDown = true;
			displacement.setX(displacement.getX() - (300 * (float) delta / 1000));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			keyDown = true;
			displacement.setY(300 * (float) delta / 1000);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			keyDown = true;
			displacement.setY(displacement.getY() - (300 * (float) delta / 1000));
		}
		glTranslated(-displacement.getX(), -displacement.getY(), 0);
		GLGuru.setXDisplacement(GLGuru.getXDisplacement() + displacement.getX());
		GLGuru.setYDisplacement(GLGuru.getYDisplacement() + displacement.getY());
	}

	@Override
	public void logic() {
		
	}

	@Override
	public void draw() {
		RenderMonkey.renderBackground(0.3, 0.4, 1.0);
		// Note - the map tiles are 30x30 pixels in the Level Editor, rather than their normal size, 40x40
		int leftBound = (int) Math.floor(GLGuru.getXDisplacement() / 30) ;
		int rightBound = (int) Math.ceil((GLGuru.getXDisplacement() + ChristmasCrashers.getWindowWidth() - 120) / 30);
		int bottomBound = (int) Math.floor(GLGuru.getYDisplacement() / 30);
		int topBound = (int) Math.ceil((GLGuru.getYDisplacement() + ChristmasCrashers.getWindowHeight() - 60) / 30);
		
		// ArrayIndexOutOfBoundsException prevention
		if (currentLevel != null) {
			if (leftBound < 0)
				leftBound = 0;
			if (rightBound >= Level.WIDTH)
				rightBound = Level.WIDTH;
			if (bottomBound < 0)
				bottomBound = 0;
			if (topBound >= Level.HEIGHT)
				topBound = Level.HEIGHT;
			for (int i = leftBound; i < rightBound; i++)
				for (int j = bottomBound; j < topBound; j++)
					if(currentLevel.getObject(i, j).getType().getTexture() != null)
						RenderMonkey.renderTexturedRectangle(i * 30, j * 30, 30, 30, currentLevel.getObject(i, j).getType().getTexture());
		}
		// Render the menus
		RenderMonkey.renderColoredRectangle(GLGuru.getXDisplacement(), GLGuru.getYDisplacement() + ChristmasCrashers.getWindowHeight() - 60, ChristmasCrashers.getWindowWidth() - 120, 60, 0.3, 0.3, 0.3);
		RenderMonkey.renderColoredRectangle(GLGuru.getXDisplacement() + ChristmasCrashers.getWindowWidth() - 120, GLGuru.getYDisplacement(), 120, ChristmasCrashers.getWindowHeight(), 0.6, 0.6, 0.6);
		if (selectedLevelButton != NavigationButton.NONE)
			RenderMonkey.renderTexturedRectangle(GLGuru.getXDisplacement() + selectedLevelButton.x, GLGuru.getYDisplacement() + selectedLevelButton.y, selectedLevelButton.width, selectedLevelButton.height, TextureMonkey.getWindowTexture(3));
		if (highlightedLevelButton != NavigationButton.NONE && highlightedLevelButton != selectedLevelButton)
			RenderMonkey.renderTexturedRectangle(GLGuru.getXDisplacement() + highlightedLevelButton.x, GLGuru.getYDisplacement() + highlightedLevelButton.y, highlightedLevelButton.width, highlightedLevelButton.height, TextureMonkey.getWindowTexture(1));
		for (NavigationButton button : NavigationButton.values()) {
			if (button.texture == null) { // Level button
				
				if (button != selectedLevelButton && button != highlightedLevelButton)
					RenderMonkey.renderTexturedRectangle(GLGuru.getXDisplacement() + button.x, GLGuru.getYDisplacement() + button.y, button.width, button.height, TextureMonkey.getWindowTexture(0));
			}
		}
		RenderMonkey.renderString("Level 1  Level 2  Level 3  Level 4  Level 5", NavigationButton.LEVEL_ONE.x + 10, NavigationButton.LEVEL_ONE.y + 8, font, Color.black);
	}

	@Override
	protected void checkKeyStates() {
		if (keyDown) {
			for (int keyID : importantKeys)
				if (Keyboard.isKeyDown(keyID))
					return;
			if (Mouse.isButtonDown(0))
				return;
		}
		keyDown = false;
	}

	/**
	 * Prepares the LevelEditor state to be set active. The {@link #currentWorld} is provided using the 
	 */
	public void initialize() {
		glTranslated(GLGuru.getXDisplacement(), GLGuru.getYDisplacement(), -GLGuru.getZDisplacement()); // Reset the camera displacement
		GLGuru.setXDisplacement(0);
		GLGuru.setYDisplacement(0);
		GLGuru.setZDisplacement(0);
		GLGuru.initGL2D();
		currentWorld = WorldManager.getWorld(0); // TODO: Set the currentWorld to the world selected in the main menu world window
		selectedLevelButton = NavigationButton.NONE;
		highlightedLevelButton = NavigationButton.NONE;
	}

	/**
	 * @return The {@link World} object currently being edited
	 */
	public World getCurrentWorld() {
		return currentWorld;
	}

	/**
	 * Sets the {@link #currentWorld} to the specified world object
	 * 
	 * @param worldToEdit The world to be modified in the editor
	 */
	public void setCurrentWorld(World worldToEdit) {
		currentWorld = worldToEdit;
	}

	/**
	 * @return The {@link Level} object currently being edited
	 */
	public Level getCurrentLevel() {
		return currentLevel;
	}

	/**
	 * Enum containing all the navigation buttons that can be selected via mouse and, for each button, the dimensional
	 * attributes and Texture object used to render the button.
	 * 
	 * @author LinearLogic
	 * @since 0.4.4
	 */
	private enum NavigationButton {

		// Level selection buttons
		LEVEL_ONE(15, ChristmasCrashers.getWindowHeight() - 52, 120, 44, null),
		LEVEL_TWO(147, ChristmasCrashers.getWindowHeight() - 52, 120, 44, null),
		LEVEL_THREE(279, ChristmasCrashers.getWindowHeight() - 52, 120, 44, null),
		LEVEL_FOUR(411, ChristmasCrashers.getWindowHeight() - 52, 120, 44, null),
		LEVEL_FIVE(543, ChristmasCrashers.getWindowHeight() - 52, 120, 44, null),
		NONE(0, 0, 0, 0, null);

		// TODO: add save/discard and object/entity buttons

		/**
		 * The pixel x-coordinate of the bottom left corner of the button within the game window
		 */
		private final double x;

		/**
		 * The pixel y-coordinate of the bottom left corner of the button within the game window
		 */
		private final double y;

		/**
		 * The width, in pixels, of the button (does not have to be the width of the Texture used for the button)
		 */
		private final double width;

		/**
		 * The height, in pixels, of the button (does not have to be the height of the Texture used for the button)
		 */
		private final double height;
	
		/**
		 * The Texture object that contains the image displayed on the button
		 */
		private final Texture texture;

		/**
		 * Constructor - sets the button's dimensional attributes and Texture to the supplied values and Texture object.
		 * 
		 * @param x The pixel {@link #x}-coordinate of the button
		 * @param y The pixel {@link #y}-coordinate of the button
		 * @param width The {@link #width}, in pixels, of the button
		 * @param height The {@link #height}, in pixels, of the button
		 */
		private NavigationButton(double x, double y, double width, double height, Texture texture) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.texture = texture;
		}

		/**
		 * Returns true iff the point defined by the provided coordinates falls within the bounds of the button rectangle.
		 * 
		 * @param x The pixel x-coordinate (within the game window) of the point to be checked
		 * @param y The pixel y-coordinate (within the game window) of the point to be checked
		 * @return Whether the provided point is within the button rectangle
		 * @param texture The {@link #texture} of the button (if null, then the texture is one that changes depending on whether
		 * the button is highlighted or selected)
		 */
		private boolean isHighlighted(int x, int y) {
			if (y > this.y && y < this.y + height)
				if (x > this.x && x < this.x + width)
					return true;
			return false;
		}
	}
}
