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
import ss.linearlogic.christmascrashers.object.Object;
import ss.linearlogic.christmascrashers.object.ObjectType;
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
	 * The {@link ObjectType type} of {@link Object} that will be placed whenever a map tile is left-clicked
	 */
	private ObjectType currentObjectType;

	/**
	 * The in-game object currently highlighted by the mouse
	 */
	private Object highlightedObject;

	/**
	 * The {@link Button buttons} belonging to the level selection menu
	 */
	private Button[] levelSelectionButtons = {Button.LEVEL_ONE, Button.LEVEL_TWO, Button.LEVEL_THREE, Button.LEVEL_FOUR, Button.LEVEL_FIVE};

	/**
	 * The {@link Button buttons} belonging to the tile selection menu
	 */
	private Button[] objectSelectionButtons = {Button.OBJECT_STONE, Button.OBJECT_BRICK, Button.OBJECT_ICE, Button.OBJECT_SPIKES, Button.OBJECT_PORTAL, Button.OBJECT_KEY, Button.OBJECT_POTION, Button.OBJECT_PRESENT};

//	private Button[] entityButtons; TODO

// These variables will only be necessary if I end up deciding to have object and entity tiles in separate menus
//	/**
//	 * Whether the group of object buttons is active. If so, {@link Button buttons} in the {@link #objectSelectionButtons}
//	 * array will be handled; otherwise, objects in the {@link #entityButtons} array will be handled.
//	 */
//	private boolean areobjectSelectionButtonsActive;

	/**
	 * The {@link Button} the mouse is currently on
	 */
	private Button highlightedButton;

	/**
	 * The {@link Button} corresponding to the level currently open in the editor window
	 */
	private Button selectedLevelButton;

	/**
	 * The object or entity {@link Button} currently selected in the tile menu on the righthand side of the editor
	 */
	private Button selectedTileButton;

	/**
	 * Constructor - loads the font to be used in the Level Editor window, adds important keys
	 */
	public LevelEditorState() {
		font = RenderMonkey.loadFont("files" + File.separator + "FIXEDSYS500C.TTF", 24);
		importantKeys.add(Keyboard.KEY_RIGHT);
		importantKeys.add(Keyboard.KEY_D);
		importantKeys.add(Keyboard.KEY_LEFT);
		importantKeys.add(Keyboard.KEY_A);
		importantKeys.add(Keyboard.KEY_UP);
		importantKeys.add(Keyboard.KEY_W);
		importantKeys.add(Keyboard.KEY_DOWN);
		importantKeys.add(Keyboard.KEY_S);
	}

	@Override
	public void handleInput() {
		// Mouse handling
		highlightedButton = Button.NONE;
		int x = Mouse.getX();
		int y = Mouse.getY();
		if (x >= 0 && x < ChristmasCrashers.getWindowWidth() - 140 && y >= 0 && y < ChristmasCrashers.getWindowHeight() - 60) { // Mouse is within the map view
			if (currentLevel != null) {
				int tileX = (int) Math.floor((GLGuru.getXDisplacement() + x) / 30);
				int tileY = (int) Math.floor((GLGuru.getYDisplacement() + y) / 30);
			// ArrayIndexOutOfBoundsException prevention
				if (tileX >= 0 && tileX < Level.WIDTH && tileY >= 0 && tileY < Level.HEIGHT) {
					highlightedObject = currentLevel.getObject(tileX, tileY);
					if (Mouse.isButtonDown(0) && currentObjectType != null && currentObjectType != ObjectType.AIR) {
						currentLevel.getObject(tileX, tileY).setType(currentObjectType);
					}
					if (Mouse.isButtonDown(1)) {
						currentLevel.getObject(tileX, tileY).setType(ObjectType.AIR);
						keyDown = true;
					}
				} else {
					highlightedObject = null;
				}
			}
		} else { // Mouse is in one of the sidebars
			checkKeyStates();
			for (Button button : Button.values()) {
				if (button == Button.NONE)
					continue;
				if (button.isHighlighted(x, y)) {
					highlightedButton = button;
					break;
				}
			}
			if (Mouse.isButtonDown(0) && !keyDown && highlightedButton != Button.NONE) {
				keyDown = true;
				if (highlightedButton == Button.SAVE_BUTTON) {
					WorldManager.saveWorld(currentWorld.getID());
					if (ChristmasCrashers.isDebugModeEnabled())
						System.out.println("Saving changes and switching to MainMenu state.");
					ChristmasCrashers.setCurrentState(StateType.MAIN_MENU);
					((MainMenuState) ChristmasCrashers.getState(StateType.MAIN_MENU)).setAnimationProgress(1000); // Skip the fade-in animation
					return;
				}
				if (highlightedButton == Button.CANCEL_BUTTON) {
					// TODO: open confirmation prompt
					if (ChristmasCrashers.isDebugModeEnabled())
						System.out.println("Discarding changes and switching to MainMenu state.");
					ChristmasCrashers.setCurrentState(StateType.MAIN_MENU);
					((MainMenuState) ChristmasCrashers.getState(StateType.MAIN_MENU)).setAnimationProgress(1000); // Skip the fade-in animation
					return;
				}
				if (highlightedButton.texture == null) { // A level selection button is highlighted
					selectedLevelButton = highlightedButton;
					currentLevel = currentWorld.getLevel(Integer.parseInt(Character.toString(selectedLevelButton.metadata)));
					if (ChristmasCrashers.isDebugModeEnabled() && currentLevel != null)
						System.out.println("Now editing level " + currentLevel.getID() + " in world " + currentWorld.getID() + ".");
					return;
				} else {
					System.out.println("!");
					selectedTileButton = highlightedButton;
					if (Integer.parseInt(Character.toString(selectedTileButton.metadata)) < 10 /* Increment this as necessary*/) // Object is selected
						currentObjectType = ObjectType.getTypeFromDataChar(selectedTileButton.metadata);
					// else // Entity is selected
				}
			}
		}

		// Keyboard handling
		if (!keyDown)
			ChristmasCrashers.getDelta(); // Clear the buildup in the delta count
		Vector2f displacement = new Vector2f(0, 0);
		int delta = ChristmasCrashers.getDelta();
		// Panning speeds are in pixels per second
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)) {
			keyDown = true;
			displacement.setX(300 * (float) delta / 1000);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) {
			keyDown = true;
			displacement.setX(displacement.getX() - (300 * (float) delta / 1000));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) {
			keyDown = true;
			displacement.setY(300 * (float) delta / 1000);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) {
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
					if (currentLevel.getObject(i, j).getType().getTexture() != null)
						RenderMonkey.renderTexturedRectangle(i * 30, j * 30, 30, 30, currentLevel.getObject(i, j).getType().getTexture());
			if (highlightedObject != null)
				RenderMonkey.renderTransparentColoredRectangle(highlightedObject.getX() * 30, highlightedObject.getY() * 30, 30, 30, 0.9, 0.9, 0.9, 0.4);
		}
		
		// Draw the menu backgrounds
		RenderMonkey.renderColoredRectangle(GLGuru.getXDisplacement(), GLGuru.getYDisplacement() + ChristmasCrashers.getWindowHeight() - 60, ChristmasCrashers.getWindowWidth() - 140, 60, 0.3, 0.3, 0.3);
		RenderMonkey.renderColoredRectangle(GLGuru.getXDisplacement() + ChristmasCrashers.getWindowWidth() - 140, GLGuru.getYDisplacement(), 140, ChristmasCrashers.getWindowHeight(), 0.6, 0.6, 0.6);

		// Draw the selected level selection and tile selection buttons, if any
		if (selectedLevelButton != Button.NONE)
			RenderMonkey.renderTexturedRectangle(GLGuru.getXDisplacement() + selectedLevelButton.x, GLGuru.getYDisplacement() + selectedLevelButton.y, selectedLevelButton.width, selectedLevelButton.height, TextureMonkey.getWindowTexture(2));
		if (selectedTileButton != Button.NONE) {
			RenderMonkey.renderLinedRectangle(GLGuru.getXDisplacement() + selectedTileButton.x - 1, GLGuru.getYDisplacement() + selectedTileButton.y - 1, selectedTileButton.width + 2, selectedTileButton.height + 2, 0.2, 0.8, 0.3);
			RenderMonkey.renderLinedRectangle(GLGuru.getXDisplacement() + selectedTileButton.x - 2, GLGuru.getYDisplacement() + selectedTileButton.y - 2, selectedTileButton.width + 4, selectedTileButton.height + 4, 0.2, 0.8, 0.3);
			RenderMonkey.renderLinedRectangle(GLGuru.getXDisplacement() + selectedTileButton.x - 3, GLGuru.getYDisplacement() + selectedTileButton.y - 3, selectedTileButton.width + 6, selectedTileButton.height + 6, 0.2, 0.8, 0.3);
		}

		// Draw the highlighted button, if any
		if (highlightedButton != Button.NONE && highlightedButton != selectedLevelButton && highlightedButton != selectedTileButton) {
			if (highlightedButton.texture == null) // Level selection button is highlighted
				RenderMonkey.renderTexturedRectangle(GLGuru.getXDisplacement() + highlightedButton.x, GLGuru.getYDisplacement() + highlightedButton.y, highlightedButton.width, highlightedButton.height, TextureMonkey.getWindowTexture(1));
			else {
				RenderMonkey.renderLinedRectangle(GLGuru.getXDisplacement() + highlightedButton.x - 1, GLGuru.getYDisplacement() + highlightedButton.y - 1, highlightedButton.width + 2, highlightedButton.height + 2, 0.3, 0.3, 1.0);
				RenderMonkey.renderLinedRectangle(GLGuru.getXDisplacement() + highlightedButton.x - 2, GLGuru.getYDisplacement() + highlightedButton.y - 2, highlightedButton.width + 4, highlightedButton.height + 4, 0.3, 0.3, 1.0);
				RenderMonkey.renderLinedRectangle(GLGuru.getXDisplacement() + highlightedButton.x - 3, GLGuru.getYDisplacement() + highlightedButton.y - 3, highlightedButton.width + 6, highlightedButton.height + 6, 0.3, 0.3, 1.0);
			}
		}

		// Draw the non-selected level selection buttons
		for (Button button : levelSelectionButtons) {
			if (button != highlightedButton && button != selectedLevelButton)
				RenderMonkey.renderTexturedRectangle(GLGuru.getXDisplacement() + button.x, GLGuru.getYDisplacement() + button.y, button.width, button.height, TextureMonkey.getWindowTexture(0));
		}

		// Draw the object selection buttons
		for (Button button : objectSelectionButtons)
			RenderMonkey.renderTexturedRectangle(GLGuru.getXDisplacement() + button.x, GLGuru.getYDisplacement() + button.y, button.width, button.height, button.texture);

		// Draw the SAVE and CANCEL buttons
		RenderMonkey.renderTexturedRectangle(GLGuru.getXDisplacement() + Button.SAVE_BUTTON.x, GLGuru.getYDisplacement() + Button.SAVE_BUTTON.y, Button.SAVE_BUTTON.width, Button.SAVE_BUTTON.height, Button.SAVE_BUTTON.texture);
		RenderMonkey.renderTexturedRectangle(GLGuru.getXDisplacement() + Button.CANCEL_BUTTON.x, GLGuru.getYDisplacement() + Button.CANCEL_BUTTON.y, Button.CANCEL_BUTTON.width, Button.CANCEL_BUTTON.height, Button.CANCEL_BUTTON.texture);

		// Draw the level names (must be drawn last!)
		RenderMonkey.renderString("Level 1  Level 2  Level 3  Level 4  Level 5", Button.LEVEL_ONE.x + 10, Button.LEVEL_ONE.y + 8, font, Color.black);
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
	 * Prepares the LevelEditor state to be set active, initializing various attributes to their default states.
	 */
	public void initialize() {
		glTranslated(GLGuru.getXDisplacement(), GLGuru.getYDisplacement(), -GLGuru.getZDisplacement()); // Reset the camera displacement
		GLGuru.setXDisplacement(0);
		GLGuru.setYDisplacement(0);
		GLGuru.setZDisplacement(0);
		GLGuru.initGL2D();
		currentLevel = null;
		currentWorld = WorldManager.getWorld(0); // TODO: Set the currentWorld to the world selected in the main menu world window
		currentWorld.load();
		currentObjectType = ObjectType.AIR; 
		selectedLevelButton = Button.NONE;
		selectedTileButton = Button.NONE;
		highlightedButton = Button.NONE;
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
	private enum Button {

		// Level selection buttons
		LEVEL_ONE(5, ChristmasCrashers.getWindowHeight() - 52, 120, 44, '0', null, null),
		LEVEL_TWO(137, ChristmasCrashers.getWindowHeight() - 52, 120, 44, '1', null, null),
		LEVEL_THREE(269, ChristmasCrashers.getWindowHeight() - 52, 120, 44, '2', null, null),
		LEVEL_FOUR(401, ChristmasCrashers.getWindowHeight() - 52, 120, 44, '3', null, null),
		LEVEL_FIVE(533, ChristmasCrashers.getWindowHeight() - 52, 120, 44, '4', null, null),

		// Object buttons
		OBJECT_STONE(ChristmasCrashers.getWindowWidth() - 120, ChristmasCrashers.getWindowHeight() - 120, 40, 40, '1', TextureMonkey.getGameTexture('1'), "Stone"),
		OBJECT_BRICK(ChristmasCrashers.getWindowWidth() - 60, ChristmasCrashers.getWindowHeight() - 120, 40, 40, '2', TextureMonkey.getGameTexture('2'), "Brick"),
		OBJECT_ICE(ChristmasCrashers.getWindowWidth() - 120, ChristmasCrashers.getWindowHeight() - 180, 40, 40, '3', TextureMonkey.getGameTexture('3'), "Ice"),
		OBJECT_SPIKES(ChristmasCrashers.getWindowWidth() - 60, ChristmasCrashers.getWindowHeight() - 180, 40, 40, '4', TextureMonkey.getGameTexture('4'), "Spikes"),
		OBJECT_PORTAL(ChristmasCrashers.getWindowWidth() - 120, ChristmasCrashers.getWindowHeight() - 240, 40, 40, '5', TextureMonkey.getGameTexture('5'), "Portal"),
		OBJECT_KEY(ChristmasCrashers.getWindowWidth() - 60, ChristmasCrashers.getWindowHeight() - 240, 40, 40, '6', TextureMonkey.getGameTexture('6'), "Key"),
		OBJECT_POTION(ChristmasCrashers.getWindowWidth() - 120, ChristmasCrashers.getWindowHeight() - 300, 40, 40, '7', TextureMonkey.getGameTexture('7'), "Potion"),
		OBJECT_PRESENT(ChristmasCrashers.getWindowWidth() - 60, ChristmasCrashers.getWindowHeight() - 300, 40, 40, '8', TextureMonkey.getGameTexture('8'), "Present"),

		// TODO: entity buttons
		
		// Window navigation buttons
		SAVE_BUTTON(ChristmasCrashers.getWindowWidth() - 130, 10, 50, 50, '0',TextureMonkey.loadTexture("PNG", "files" + File.separator + "SaveIcon.PNG"), "Save"),
		CANCEL_BUTTON(ChristmasCrashers.getWindowWidth() - 60, 10, 50, 50, '0', TextureMonkey.loadTexture("PNG", "files" + File.separator + "DeleteIcon.PNG"), "Delete"),

		NONE(0, 0, 0, 0, '0', null, null);


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
		 * A button's metadata contains information unique to each type of button. For instance, a level
		 * selection button's metadata will contain the corresponding level ID (to be cast to an int), while a
		 * tile button's metadata will contain the ID of the corresponding object/entity.
		 */
		private final char metadata;
	
		/**
		 * The Texture object that contains the image displayed on the button
		 */
		private final Texture texture;
	
		/**
		 * The string to be displayed in the info window when the button is highlighted (moused over)
		 */
		private final String info; // TODO: add info window

		/**
		 * Constructor - sets the button's dimensional attributes, texture, and info string to the supplied values.
		 * 
		 * @param x The pixel {@link #x}-coordinate of the button
		 * @param y The pixel {@link #y}-coordinate of the button
		 * @param width The {@link #width}, in pixels, of the button
		 * @param height The {@link #height}, in pixels, of the button
		 * @param metadata The button's {@link #metadata} character
		 * @param texture The Texture object used as the button's image
		 * @param info The button's {@link #info} string
		 */
		private Button(double x, double y, double width, double height, char metadata, Texture texture, String info) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.metadata = metadata;
			this.texture = texture;
			this.info = info;
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
