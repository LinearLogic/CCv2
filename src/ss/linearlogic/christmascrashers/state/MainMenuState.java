package ss.linearlogic.christmascrashers.state;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import ss.linearlogic.christmascrashers.ChristmasCrashers;
import ss.linearlogic.christmascrashers.engine.GLGuru;
import ss.linearlogic.christmascrashers.engine.RenderMonkey;
import ss.linearlogic.christmascrashers.world.WorldManager;


/**
 * The main menu state presents the user with clickable options, such as "start game" and "exit",
 * and runs a starfield animation in the background while active.
 * 
 * @author LinearLogic
 *@since 0.0.2
 */
public class MainMenuState extends State {

	/**
	 * Contains the textures used for the game option/navigation buttons.
	 */
	private static HashMap<Integer, Texture> textures = new HashMap<Integer, Texture>();

	/**
	 * The speed at which the camera zooms in on the starfield.
	 */
	private float zoomSpeed;

	/**
	 * The {@link #points} in the starfield, which represent stars. When a point moves behind the camera,
	 * it is replaced with a new point inside the camera's current view, to provide infinite zooming.
	 */
	private Point[] points = new Point[1000];

	/**
	 * How long the animation that fades in the starfield animation and option buttons should take, in milliseconds.
	 */
	private int animationSpeed;

	/**
	 * The current progress through the fade-in animation (this value starts at 0 and increments until it reaches 1000).
	 */
	private double animationProgress;

	/**
	 * The {@link NavigationButton} currently selected by the mouse (NONE if the mouse isn't hovering over one of
	 * the navigation buttons)
	 */
	private NavigationButton selectedButton;

	/**
	 * The font used in the MainMenu screen (for the version, etc.)
	 */
	private TrueTypeFont font;

	/**
	 * Constructor - initializes the keyDown value to false and populates the importantKeys ArrayList
	 * (inherited from the {@link State} superclass).
	 */
	public MainMenuState() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Loading textures for the MainMenu state.");
		try {
			textures.put(2, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("files" + File.separator + "CCStartGameButton.PNG")));
			textures.put(1, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("files" + File.separator + "CCLevelEditorButton.PNG")));
			textures.put(0, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("files" + File.separator + "CCExitButton.PNG")));
		} catch (IOException e) {
			System.err.println("Failed to load texture(s) for the MainMenu state!");
			e.printStackTrace();
		}
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Loading fonts for the MainMenu state.");
		font = RenderMonkey.loadFont("files" + File.separator + "FIXEDSYS500C.TTF", 30);
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Initializing MainMenu state variables.");
		keyDown = false;
		zoomSpeed = 1.1f;
		animationSpeed = 7000;
		animationProgress = 1000;
		selectedButton = NavigationButton.NONE;
	}

	@Override
	public void handleInput() {
		checkKeyStates();
		// Determine the currently selected navigation button
		int x = Mouse.getX();
		int y = Mouse.getY();
		if (NavigationButton.START_GAME.isSelected(x, y))
			selectedButton = NavigationButton.START_GAME;
		else if (NavigationButton.LEVEL_EDITOR.isSelected(x, y))
			selectedButton = NavigationButton.LEVEL_EDITOR;
		else if (NavigationButton.EXIT.isSelected(x, y))
			selectedButton = NavigationButton.EXIT;
		else
			selectedButton = NavigationButton.NONE;

		if (Mouse.isButtonDown(0) && !keyDown && (animationProgress >= 1000)) { // Left click
			keyDown = true;
			switch(selectedButton) {
				case START_GAME:
					if (ChristmasCrashers.isDebugModeEnabled())
						System.out.println("Switching to Game state.");
					((GameState) ChristmasCrashers.getState(StateType.GAME)).setCurrentWorld(WorldManager.getWorld(0)); // TODO replace this with the world selected in the world choice menu (not yet implemented)
					ChristmasCrashers.setCurrentState(StateType.GAME);
					break;
				case LEVEL_EDITOR:
					if (ChristmasCrashers.isDebugModeEnabled())
						System.out.println("Switching to LevelEditor state.");
					ChristmasCrashers.setCurrentState(StateType.LEVEL_EDITOR);
					break;
				case EXIT:
					ChristmasCrashers.exitGameLoop();
					break;
				case NONE:
				default:
					break;
			}
		}
	}

	@Override
	public void logic() {

	}

	@Override
	public void draw() {
		if (points[0] != null) { // Only render things if the starfield animation has been initialized
			// Switch into 3D mode (gluPerspective)
			GLGuru.initGL3D();
			glDisable(GL_TEXTURE_2D);
			glColor3d(1, 1, 1);
	        glTranslatef(0, 0, zoomSpeed);
	        GLGuru.setZDisplacement(GLGuru.getZDisplacement() + zoomSpeed);
	        glBegin(GL_POINTS);
	        	Random random = new Random();
	        	for (int i = 0; i < points.length; i++) {
	        		Point p = points[i];
	        		if (p.z + GLGuru.getZDisplacement() >= 0) // Point is no longer in view, replace it.
	        			points[i] = new Point((random.nextFloat() - 0.5f) * 100f, (random.nextFloat() - 0.5f) * 100f, random.nextInt(200) - (300 + (float) GLGuru.getZDisplacement()));
	        		glVertex3f(p.x, p.y, p.z);
	        	}
	        glEnd();
			glEnable(GL_TEXTURE_2D);

	        // Switch back to 2D mode using a glOrtho call adjusted based on the current zoomDistance
	        GLGuru.initGL2D();

			// Load fade-in animation components
			if (animationProgress < 1000) {
				if (animationProgress < 400) { // Fade in the starfield
					RenderMonkey.renderTransparentBackground(0, 0, 0, 	1 - animationProgress / 400);
				}
				if (animationProgress > 600) { // Fade in the buttons
					// Start Game button (fades in first)
					double transparency = (animationProgress - 600) / 300;
					if (transparency > 1)
						transparency = 1;
					RenderMonkey.renderTransparentTexturedRectangle(NavigationButton.START_GAME.x, NavigationButton.START_GAME.y, NavigationButton.START_GAME.width, NavigationButton.START_GAME.height, NavigationButton.START_GAME.texture, Math.pow(transparency, 2));
					// Level Editor button (fades in second)
					transparency = (animationProgress - 650) / 300;
					if (transparency < 0)
						transparency = 0;
					if (transparency > 1)
						transparency = 1;
					RenderMonkey.renderTransparentTexturedRectangle(NavigationButton.LEVEL_EDITOR.x, NavigationButton.LEVEL_EDITOR.y, NavigationButton.LEVEL_EDITOR.width, NavigationButton.LEVEL_EDITOR.height, NavigationButton.LEVEL_EDITOR.texture, Math.pow(transparency, 2));
					// Exit button (fades in last)
					transparency = (animationProgress - 700) / 300;
					if (transparency < 0)
						transparency = 0;
					RenderMonkey.renderTransparentTexturedRectangle(NavigationButton.EXIT.x, NavigationButton.EXIT.y, NavigationButton.EXIT.width, NavigationButton.EXIT.height, NavigationButton.EXIT.texture, Math.pow(transparency, 2));
				}
				animationProgress += (double) ChristmasCrashers.getDelta() * 1000 / animationSpeed;
				if (animationProgress > 1000) // Prevent overshooting the animationProgress limit
					animationProgress = 1000;
			} else { // Load fully opaque option buttons
				RenderMonkey.renderString("v" + ChristmasCrashers.VERSION, 30, 30, font, Color.lightGray); // Render the version string
				// Render the button highlighting (if any)
				RenderMonkey.renderTransparentTexturedRectangle(NavigationButton.START_GAME.x, NavigationButton.START_GAME.y, NavigationButton.START_GAME.width, NavigationButton.START_GAME.height, NavigationButton.START_GAME.texture, 1.0);
				RenderMonkey.renderTransparentTexturedRectangle(NavigationButton.LEVEL_EDITOR.x, NavigationButton.LEVEL_EDITOR.y, NavigationButton.LEVEL_EDITOR.width, NavigationButton.LEVEL_EDITOR.height, NavigationButton.LEVEL_EDITOR.texture, 1.0);
				RenderMonkey.renderTransparentTexturedRectangle(NavigationButton.EXIT.x, NavigationButton.EXIT.y, NavigationButton.EXIT.width, NavigationButton.EXIT.height, NavigationButton.EXIT.texture, 1.0);
				// Highlight the selected NavigationButton (if any)
				if (selectedButton != NavigationButton.NONE) {
					RenderMonkey.renderLinedRectangle(selectedButton.x, selectedButton.y, selectedButton.width, selectedButton.height, 0.3, 0.3, 1.0);
					RenderMonkey.renderLinedRectangle(selectedButton.x - 1, selectedButton.y - 1, selectedButton.width + 2, selectedButton.height + 2, 0.3, 0.3, 1.0);
					RenderMonkey.renderLinedRectangle(selectedButton.x - 2, selectedButton.y - 2, selectedButton.width + 4, selectedButton.height + 4, 0.3, 0.3, 1.0);
					RenderMonkey.renderLinedRectangle(selectedButton.x - 3, selectedButton.y - 3, selectedButton.width + 6, selectedButton.height + 6, 0.3, 0.3, 1.0);
				}
			}
		}
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
	 * Setst the current {@link #animationProgress} to the specified integer value (must be between 0 and 1000, inclusive).
	 * 
	 * @param progress
	 */
	public void setAnimationProgress(int progress) {
		if (progress < 0)
			progress = 0;
		if (progress > 1000)
			progress = 1000;
		animationProgress = progress;
	}

	/**
	 * Populates the {@link #points} array to begin the starfield animation, and sets the {@link #animationProgress}
	 * to 0 to begin the fade-in animation.
	 */
	public void initialize() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Initializing MainMenu state.");
		selectedButton = NavigationButton.NONE;
		glTranslated(GLGuru.getXDisplacement(), GLGuru.getYDisplacement(), -GLGuru.getZDisplacement()); // Reset the camera displacement
		GLGuru.setXDisplacement(0);
		GLGuru.setYDisplacement(0);
		GLGuru.setZDisplacement(0);
		Random random = new Random();
		for (int i = 0; i < points.length; i++)
			points[i] = new Point((random.nextFloat() - 0.5f) * 100f, (random.nextFloat() - 0.5f) * 100f, random.nextInt(200) - (300 + (float) GLGuru.getZDisplacement()));
		animationProgress = 0;
	}

	/**
	 * Represents a point (star) in the starfield animation.
	 * 
	 * @author LinearLogic
	 * @since 0.1.1
	 */
	private class Point {

		/**
		 * The x-coordinate of the point in the 3D animation frame
		 */
		float x;

		/**
		 * The y-coordinate of the point in the 3D animation frame
		 */
		float y;

		/**
		 * The z-coordinate of the point in the 3D animation frame
		 */
		float z;

		/**
		 * Constructs the point object to display at the given location.
		 * 
		 * @param x The {@link #x}-coordinate of the point
		 * @param y The {@link #y}-coordinate of the point
		 * @param z The {@link #z}-coordinate of the point
		 */
        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

	/**
	 * Enum containing all the navigation buttons that can be selected via mouse and, for each button, the dimensional
	 * attributes and Texture object used to render the button.
	 * 
	 * @author LinearLogic
	 * @since 0.1.3
	 */
	private enum NavigationButton {

		START_GAME(ChristmasCrashers.getWindowWidth() / 2 - 170, ChristmasCrashers.getWindowHeight() / 2 + 45, 180, 50, textures.get(2)),
		LEVEL_EDITOR(ChristmasCrashers.getWindowWidth() / 2 - 90, ChristmasCrashers.getWindowHeight() / 2 - 25, 180, 50, textures.get(1)),
		EXIT(ChristmasCrashers.getWindowWidth() / 2 - 10, ChristmasCrashers.getWindowHeight() / 2 - 95, 180, 50, textures.get(0)),
		NONE(0, 0, 0, 0, null);

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
		 * @param texture The {@link #texture} of the button
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
		 */
		private boolean isSelected(int x, int y) {
			if (y > this.y && y < this.y + height)
				if (x > this.x && x < this.x + width)
					return true;
			return false;
		}
	}
}
