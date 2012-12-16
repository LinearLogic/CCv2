package ss.linearlogic.christmascrashers.state;

import static org.lwjgl.opengl.GL11.glTranslated;

import java.io.File;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.TrueTypeFont;

import ss.linearlogic.christmascrashers.ChristmasCrashers;
import ss.linearlogic.christmascrashers.engine.GLGuru;
import ss.linearlogic.christmascrashers.engine.RenderMonkey;
import ss.linearlogic.christmascrashers.world.Level;
import ss.linearlogic.christmascrashers.world.World;

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
	 * Constructor - loads the font to be used in the Level Editor window, adds important keys
	 */
	public LevelEditorState() {
		font = RenderMonkey.loadFont("files" + File.separator + "FIXEDSYS500C.TTF", 30);
		importantKeys.add(Keyboard.KEY_RIGHT);
		importantKeys.add(Keyboard.KEY_LEFT);
		importantKeys.add(Keyboard.KEY_UP);
		importantKeys.add(Keyboard.KEY_DOWN);
		importantKeys.add(Keyboard.KEY_ESCAPE);
	}

	@Override
	public void handleInput() {
		checkKeyStates();
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
		System.out.println(displacement.getX() + ", " + displacement.getY());
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
	}

	/**
	 * Prepares the LevelEditor state to be set active. The {@link #currentWorld} is provided using the 
	 */
	public void initialize() {
		System.out.println(GLGuru.getZDisplacement());
		glTranslated(GLGuru.getXDisplacement(), GLGuru.getYDisplacement(), -GLGuru.getZDisplacement()); // Reset the camera displacement
		GLGuru.setXDisplacement(0);
		GLGuru.setYDisplacement(0);
		GLGuru.setZDisplacement(0);
		GLGuru.initGL2D();
		currentLevel = currentWorld.getLevel(0);
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
}
