package ss.linearlogic.christmascrashers.state;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import ss.linearlogic.christmascrashers.ChristmasCrashers;
import ss.linearlogic.christmascrashers.engine.RenderMonkey;
import ss.linearlogic.christmascrashers.world.WorldManager;

/**
 * The introduction state is entered first. While active, it runs the loading animation, and then prompts
 * user to press 'Enter' to begin. The player can skip the intro at any point by pressing the 'escape' key.
 * 
 * @author LinearLogic
 * @since 0.0.2
 */
public class IntroState extends State {

	/**
	 * Contains the textures used in the loading screen animation.
	 */
	private HashMap<Integer, Texture> textures = new HashMap<Integer, Texture>();

	/**
	 * Holds the current state of the loading animation.
	 */
	private static boolean animationComplete;

	/**
	 * Rate at which the "loading..." text in the splash fades in and out, in milliseconds per cycle.
	 */
	private int fadeFrequency;

	/**
	 * The fade value in the current frame (determines the {@link #transparencyLevel} of the "loading..." text
	 * using a cosine function with {@link #fadeFrequency} as a parameter).
	 */
	private double fadeValue;

	/**
	 * The transparency level for the rectangle that fades the "loading..." text in and out.
	 */
	private double transparencyLevel;

	/**
	 * Constructor - loads intro textures, sets the {@link #fadeFrequency}, initializes the {@link #finishedLoading} and {@link State#keyDown} values to 'false',
	 * and populates the {@link State#importantKeys} ArrayList. Note that the splash animation and
	 * game-loader thread are not started here, but in the {@link #initialize()} method.
	 */
	public IntroState() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Loading textures for the Intro state.");
		try {
			textures.put(0, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("files" + File.separator + "CCIntroBanner.PNG")));
			textures.put(1, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("files" + File.separator + "CCIntroLoading.PNG")));
			textures.put(2, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("files" + File.separator + "CCIntroComplete.PNG")));
		} catch (IOException e) {
			System.err.println("Failed to load texture(s) for the Intro state!");
			e.printStackTrace();
		}
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Initializing Intro state variables.");
		fadeFrequency = 1200; // One second per fade cycle
		fadeValue = 0;
		transparencyLevel = 1.0;
		keyDown = false;
		addImportantKey(Keyboard.KEY_RETURN);
		animationComplete = false;
	}

	@Override
	public void handleInput() {
		checkKeyStates();
		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) && animationComplete && !keyDown) { // Animation is complete
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("Switching to MainMenu state.");
			ChristmasCrashers.setCurrentState(StateType.MAIN_MENU);
		}
	}

	@Override
	public void logic() {
		transparencyLevel = (0.5 * Math.cos(fadeValue)) + 0.5;
		fadeValue = (fadeValue + 2.0 * Math.PI * ((double) ChristmasCrashers.getDelta() / fadeFrequency)) % (2 * Math.PI);
	}

	@Override
	public void draw() {
		RenderMonkey.renderBackground(1, 1, 1);
		RenderMonkey.renderTexturedRectangle(0.0, ChristmasCrashers.getWindowHeight() / 2 - 40, textures.get(0).getTextureWidth(), textures.get(0).getTextureHeight(), textures.get(0)); // Render the ChristmasCrashers banner
		if (!animationComplete) {
			RenderMonkey.renderTransparentTexturedRectangle(0.0, ChristmasCrashers.getWindowHeight() / 2 - textures.get(1).getTextureHeight(), textures.get(1).getTextureWidth(), textures.get(1).getTextureHeight(), textures.get(1), transparencyLevel); // Render the appropriately-faded loading banner
		} else {
			RenderMonkey.renderTexturedRectangle(0.0, ChristmasCrashers.getWindowHeight() / 2 - textures.get(2).getTextureHeight(), textures.get(2).getTextureWidth(), textures.get(2).getTextureHeight(), textures.get(2)); // Render the appropriately-faded loading banner
		}
	}

	/**
	 * Begins the loading animation and executes the game-loader thread.
	 */
	public void initialize() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Initializing Intro state.");
		animationComplete = false;
		WorldManager.loadWorlds();
	}

	/**
	 * @return The state of the {@link #animationComplete} flag
	 */
	public boolean isAnimationComplete() {
		return animationComplete;
	}

	/**
	 * Sets the {@link #animationComplete} flag to the provided value.
	 * @param complete
	 */
	public void setAnimationComplete(boolean complete) {
		animationComplete = complete;
	}
}
