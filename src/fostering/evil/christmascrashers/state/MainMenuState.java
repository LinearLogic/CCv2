package fostering.evil.christmascrashers.state;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

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
	 * Constructor - initializes the keyDown value to false and populates the importantKeys ArrayList
	 * (inherited from the {@link State} superclass).
	 */
	public MainMenuState() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Initializing MainMenu state variables.");
		keyDown = false;
		zoomSpeed = 1f;
		zoomDistance = 0f;
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
	        // Switch back to 2D mode (glOrtho)
	        GLGuru.initGL2D();
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
	 * Populates the {@link #points} array to begin the starfield animation.
	 */
	public static void initialize() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Initializing MainMenu state.");
		Random random = new Random();
		for (int i = 0; i < points.length; i++)
			points[i] = new Point((random.nextFloat() - 0.5f) * 100f, (random.nextFloat() - 0.5f) * 100f, random.nextInt(200) - (300 + zoomDistance));
	}
	
	/**
	 * Represents a point (star) in the starfield animation.
	 * 
	 * @author LinearLogic
	 * @since 0.0.7
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
