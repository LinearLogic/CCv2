package ss.linearlogic.christmascrashers.state;

import static org.lwjgl.opengl.GL11.glTranslated;

import java.io.File;

import org.newdawn.slick.TrueTypeFont;

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
	 * Constructor - loads the font to be used in the Level Editor window
	 */
	public LevelEditorState() {
		font = RenderMonkey.loadFont("files" + File.separator + "FIXEDSYS500C.TTF", 30);
	}

	@Override
	public void handleInput() {
		
	}

	@Override
	public void logic() {
		
	}

	@Override
	public void draw() {
		
	}

	/**
	 * Prepares the LevelEditor state to be set active. The {@link #currentWorld} is provided using the 
	 */
	public void initialize() {
		glTranslated(GLGuru.getXDisplacement(), GLGuru.getYDisplacement(), -GLGuru.getZDisplacement()); // Reset the camera displacement
		GLGuru.setXDisplacement(0);
		GLGuru.setYDisplacement(0);
		GLGuru.setZDisplacement(0);
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
