package ss.linearlogic.christmascrashers.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * The TextureMonkey runs on bananas and provides methods for various Texture operations, such as loading Texture objects.
 * 
 * @author LinearLogic
 * @since 0.2.5
 */
public class TextureMonkey {

	/**
	 * A HashMap of all the loaded textures for tile/sprite images, populated via the {@link #init()} method.
	 * Note that texture ID characters match up with the dataChar values for {@link ObjectType object types} and EntityTypes
	 */
	private static HashMap<Character, Texture> gameTextures = new HashMap<Character, Texture>();

	/**
	 * A HashMap of all the loaded window textures (eg. navigation buttons), populated via the {@link #init()} method.
	 * Note that not all window textures are stored here - this list contains textures that are used in multiple states
	 * and are therefore most efficiently accessed from a central location.
	 */
	private static HashMap<Integer, Texture> windowTextures = new HashMap<Integer, Texture>();

	/**
	 * Loads the textures included in the body of the method.
	 */
	public static void init() {
		// '0' == air (no texture)
		// 1 - 10 are object tile textures
		gameTextures.put('1', loadTexture("JPG", "files" + File.separator + "stone.jpg"));
		gameTextures.put('2', loadTexture("JPG", "files" + File.separator + "brick.jpg"));
		gameTextures.put('3', loadTexture("JPG", "files" + File.separator + "ice.jpg"));
		gameTextures.put('4', loadTexture("JPG", "files" + File.separator + "stone.jpg")); // To be replaced with spikes image once one is found
		gameTextures.put('5', loadTexture("PNG", "files" + File.separator + "portal.png"));
		gameTextures.put('6', loadTexture("PNG", "files" + File.separator + "key.png"));
		gameTextures.put('7', loadTexture("PNG", "files" + File.separator + "potion.png"));
		gameTextures.put('8', loadTexture("PNG", "files" + File.separator + "present.png"));
		// characters from 'a' and on are entitiy textures
		// a = Gremlin 1
		// b = Gremlin 2
		// c = icycle
		// d = snowball
		
		// Window texture loading:
		windowTextures.put(0, loadTexture("PNG", "files" + File.separator + "BlueButton.PNG"));
		windowTextures.put(1, loadTexture("PNG", "files" + File.separator + "AquaButton.PNG"));
		windowTextures.put(2, loadTexture("PNG", "files" + File.separator + "GreenButton.PNG"));
		windowTextures.put(3, loadTexture("PNG", "files" + File.separator + "RedButton.PNG"));

	}

	/**
	 * @param format
	 * @param pathToTexture
	 * @return The Texture (image) of the specified format (JPG, PNG, etc.) at the specified disk location. If the Texture
	 * object is unable to be loaded (due to an invalid path, an incompatible format, or otherwise), the method returns null.
	 */
	public static Texture loadTexture(String format, String pathToTexture) {
		Texture tex = null;
		try {
			tex = TextureLoader.getTexture(format, ResourceLoader.getResourceAsStream(pathToTexture));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tex;
	}

	/**
	 * @param idChar
	 * @return The in-game (object/entity) texture that corresponds with the provided ID char
	 */
	public static Texture getGameTexture(char idChar) {
		return gameTextures.get(idChar);
	}

	/**
	 * @param textureID
	 * @return The window (eg. button) texture that corresponds with the provided integer ID
	 */
	public static Texture getWindowTexture(int textureID) {
		return windowTextures.get(textureID);
	}
}
