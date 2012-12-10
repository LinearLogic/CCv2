package ss.linearlogic.christmascrashers.util;

import java.io.File;
import java.io.IOException;

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
}
