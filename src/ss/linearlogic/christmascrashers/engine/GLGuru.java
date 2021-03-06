package ss.linearlogic.christmascrashers.engine;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import ss.linearlogic.christmascrashers.ChristmasCrashers;

/**
 * The GLGuru is the gateway to openGL, providing methods for setting up 2D and 3D openGL rendering.
 * 
 * @author LinearLogic
 * @since 0.0.6
 */
public class GLGuru {

	/**
	 * How far to the right (positive) or left (negative) the camera is panned
	 */
	private static double xDisplacement;

	/**
	 * How far up (positive) or down (negative) the camera is panned
	 */
	private static double yDisplacement;

	/**
	 * How far in (positive) or out (negative) the camera is zoomed
	 */
	private static double zDisplacement;

	/**
	 * Sets up openGL for 2D graphics using glOrtho with the current game window width and height as dimensions.
	 */
	public static void initGL2D() {
		initGL2D(ChristmasCrashers.getWindowWidth(), ChristmasCrashers.getWindowHeight());
	}

	/**
	 * Sets up openGL for 2D graphics using glOrtho with custom dimensions.
	 * 
	 * @param width The pixel width to be passed to the glOtho(...) method
	 * @param height The pixel height to be passed to the glOrtho(...) method
	 */
	public static void initGL2D(int width, int height) {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(xDisplacement, xDisplacement + width, yDisplacement, yDisplacement + height, -zDisplacement + 1, -zDisplacement - 1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_DEPTH_TEST);
		glShadeModel(GL_SMOOTH);
		glClearDepth(1);
	}

	/**
	 * Sets up openGL for 3D graphics using gluPerspective with the current game window width and height for the aspect ratio.
	 */
	public static void initGL3D() {
		initGL3D(ChristmasCrashers.getWindowWidth(), ChristmasCrashers.getWindowHeight());
	}
	/**
	 * Sets up openGL for 3D graphics using gluPerspective with a custom aspect ratio.
	 * 
	 * @param width The pixel width of the game window, to be used in the aspect ratio in gluPerspective
	 * @param height The pixel height of the game window, to be used in the aspect ratio in gluPerspective
	 */
	public static void initGL3D(int width, int height) {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective((float) 30, width / height, 0.001f, 300);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_DEPTH_TEST);
	}

	/**
	 * @return The current {@link #xDisplacement}
	 */
	public static double getXDisplacement() {
		return xDisplacement;
	}

	/**
	 * Sets the current {@link #xDisplacement} supplied value
	 * @param displacement
	 */
	public static void setXDisplacement(double displacement) {
		xDisplacement = displacement;
	}

	/**
	 * @return The current {@link #yDisplacement}
	 */
	public static double getYDisplacement() {
		return yDisplacement;
	}

	/**
	 * Sets the current {@link #yDisplacement} supplied value
	 * @param displacement
	 */
	public static void setYDisplacement(double displacement) {
		yDisplacement = displacement;
	}

	/**
	 * @return The current {@link #zDisplacement}
	 */
	public static double getZDisplacement() {
		return zDisplacement;
	}

	/**
	 * Sets the current {@link #zDisplacement} supplied value
	 * @param displacement
	 */
	public static void setZDisplacement(double displacement) {
		zDisplacement = displacement;
	}
}
