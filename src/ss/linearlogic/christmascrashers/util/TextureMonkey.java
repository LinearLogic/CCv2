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
	 * An HashMap of all the loaded textures for tile/sprite images (populated via the {@link #init()} method).
	 * Note that texture ID characters match up with the dataChar values for {@link ObjectType object types} and EntityTypes
	 */
	private static HashMap<Character, Texture> textures = new HashMap<Character, Texture>();

	/**
	 * Loads the textures included in the body of the method.
	 */
	public static void init() {
		textures.put('1', loadTexture("JPG", "files" + File.separator + "stone.png"));
		textures.put('2', loadTexture("JPG", "files" + File.separator + "stone.png"));
		textures.put('3', loadTexture("JPG", "files" + File.separator + "stone.png"));
		textures.put('4', loadTexture("JPG", "files" + File.separator + "stone.png"));
		textures.put('5', loadTexture("JPG", "files" + File.separator + "stone.png"));
		textures.put('6', loadTexture("JPG", "files" + File.separator + "stone.png"));
		textures.put('7', loadTexture("JPG", "files" + File.separator + "stone.png"));
		textures.put('8', loadTexture("JPG", "files" + File.separator + "stone.png"));
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
			tex = TextureLoader.getTexture(format, ResourceLoader.getResourceAsStream("files" + File.separator + "stone.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tex;
	}

	/**
	 * @param idChar
	 * @return The texture that corresponds with the provided char
	 */
	public static Texture getTexture(char idChar) {
		return textures.get(idChar);
	}
}
