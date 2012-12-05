package fostering.evil.christmascrashers.state;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import fostering.evil.christmascrashers.ChristmasCrashers;
import fostering.evil.christmascrashers.engine.GLGuru;
import fostering.evil.christmascrashers.engine.RenderMonkey;

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
	private HashMap<Integer, Texture> textures = new HashMap<Integer, Texture>();
	
	/**
	 * The speed at which the camera zooms in on the starfield.
	 */
	private float zoomSpeed;
	
	/**
	 * How far zoomed in the camera is (incremented by {@link #zoomSpeed})
	 */
	private static float zoomDistance;
	
	/**
	 * The {@link #points} in the starfield, which represent stars. When a point moves behind the camera,
	 * it is replaced with a new point inside the camera's current view, to provide infinite zooming.
	 */
	private static Point[] points = new Point[1000];
	
	/**
	 * How long the animation that fades in the starfield animation and option buttons should take, in milliseconds.
	 */
	private int animationSpeed;

	/**
	 * The current progress through the fade-in animation (this value starts at 0 and increments until it reaches 1000).
	 */
	private static double animationProgress;

	/**
	 * Constructor - initializes the keyDown value to false and populates the importantKeys ArrayList
	 * (inherited from the {@link State} superclass).
	 */
	public MainMenuState() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Loading textures for the Intro state.");
		try {
			textures.put(2, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("files" + File.separator + "CCStartGameButton.PNG")));
			textures.put(1, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("files" + File.separator + "CCLevelEditorButton.PNG")));
			textures.put(0, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("files" + File.separator + "CCExitButton.PNG")));
		} catch (IOException e) {
			System.err.println("Failed to load texture(s) for the MainMenu state!");
			e.printStackTrace();
		}
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Loading navigation button locations and dimensions.");
		
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Initializing MainMenu state variables.");
		keyDown = false;
		zoomSpeed = 1.1f;
		zoomDistance = 0f;
		animationSpeed = 8000;
		animationProgress = 1000;
		addImportantKey(Keyboard.KEY_ESCAPE);
	}

	@Override
	public StateType handleInput() {
		checkKeyStates();
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !keyDown) {
			keyDown = true;
			// Open quitgame prompt window?
			ChristmasCrashers.exitGameLoop();
		}
		if (Mouse.isButtonDown(0)) {
			keyDown = true;
			// TODO: add mouse location checks and handle clicks on in-game options (start game, exit, etc.)
		}
		return StateType.MAIN_MENU;
	}

	@Override
	public void logic() {
		
	}

	@Override
	public void draw() {
		if (points[0] != null) {
			// Switch into 3D mode (gluPerspective)
			GLGuru.initGL3D();
			glColor3d(1, 1, 1);
	        glTranslatef(0, 0, zoomSpeed);
	        zoomDistance += zoomSpeed;
	        glBegin(GL_POINTS);
	        	Random random = new Random();
	        	for (int i = 0; i < points.length; i++) {
	        		Point p = points[i];
	        		if (p.z + zoomDistance >= 0) // Point is no longer in view, replace it.
	        			points[i] = new Point((random.nextFloat() - 0.5f) * 100f, (random.nextFloat() - 0.5f) * 100f, random.nextInt(200) - (300 + zoomDistance));
	        		glVertex3f(p.x, p.y, p.z);
	        	}
	        glEnd();

	        // Switch back to 2D mode using a glOrtho call adjusted based on the current zoomDistance
	        glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, ChristmasCrashers.getWindowWidth(), 0, ChristmasCrashers.getWindowHeight(), -zoomDistance + 1, -zoomDistance - 1);
			glMatrixMode(GL_MODELVIEW);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glDisable(GL_DEPTH_TEST);
			glShadeModel(GL_SMOOTH);
			glClearDepth(1);

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
					Texture tex = textures.get(2);
					RenderMonkey.renderTransparentTexturedRectangle(ChristmasCrashers.getWindowWidth() / 2 - 170, 140 + 2 * (tex.getTextureHeight() + 20), 180, 50, tex, Math.pow(transparency, 2));
					// Level Editor button (fades in second)
					transparency = (animationProgress - 650) / 300;
					if (transparency < 0)
						transparency = 0;
					if (transparency > 1)
						transparency = 1;
					tex = textures.get(1);
					RenderMonkey.renderTransparentTexturedRectangle(ChristmasCrashers.getWindowWidth() / 2 - 90, 140 + tex.getTextureHeight() + 20, 180, 50, tex, Math.pow(transparency, 2));
					// Exit button (fades in last)
					transparency = (animationProgress - 700) / 300;
					if (transparency < 0)
						transparency = 0;
					tex = textures.get(0);
					RenderMonkey.renderTransparentTexturedRectangle(ChristmasCrashers.getWindowWidth() / 2 - 10, 140, 180, 50, tex, Math.pow(transparency, 2));
				}
				animationProgress += (double) ChristmasCrashers.getDelta() * 1000 / animationSpeed;
				if (animationProgress > 1000) // Prevent overshooting the animationProgress limit
					animationProgress = 1000;
			} else { // Load fully opaque option buttons
				RenderMonkey.renderTransparentTexturedRectangle(ChristmasCrashers.getWindowWidth() / 2 - 170, 140 + 2 * (textures.get(2).getTextureHeight() + 20), 180, 50, textures.get(2), 1.0);
				RenderMonkey.renderTransparentTexturedRectangle(ChristmasCrashers.getWindowWidth() / 2 - 90, 140 + textures.get(1).getTextureHeight() + 20, 180, 50, textures.get(1), 1.0);
				RenderMonkey.renderTransparentTexturedRectangle(ChristmasCrashers.getWindowWidth() / 2 - 10, 140, 180, 50, textures.get(0), 1.0);
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
	 * Populates the {@link #points} array to begin the starfield animation, and starts the fade-in animation.
	 */
	public static void initialize() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Initializing MainMenu state.");
		Random random = new Random();
		for (int i = 0; i < points.length; i++)
			points[i] = new Point((random.nextFloat() - 0.5f) * 100f, (random.nextFloat() - 0.5f) * 100f, random.nextInt(200) - (300 + zoomDistance));
		animationProgress = 0;
	}
	
	/**
	 * Represents a point (star) in the starfield animation.
	 * 
	 * @author LinearLogic
	 * @since 0.1.1
	 */
	private static class Point {

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
}
