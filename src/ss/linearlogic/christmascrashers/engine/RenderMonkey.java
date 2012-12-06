package ss.linearlogic.christmascrashers.engine;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedInputStream;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.util.ResourceLoader;

import ss.linearlogic.christmascrashers.ChristmasCrashers;

/**
 * The RenderMonkey provides various rendering utilities/shortcuts if you feed him enough bananas.
 * 
 * @author LinearLogic
 * @since 0.0.4
 */
public class RenderMonkey {

	/**
	 * Red component of window color
	 */
	public static double default_window_r = 0.4;

	/**
	 * Green component of window color
	 */
	public static double default_window_g = 0.4;

	/**
	 * Blue component of window color
	 */
	public static double default_window_b = 0.4;

	/**
	 * Renders a colored rectangle with the specified color intensities that fills up the game window.
	 * 
	 * @param r Red intensity (must be between 0.0 and 1.0, inclusive)
	 * @param g Green intensity (must be between 0.0 and 1.0, inclusive)
	 * @param b Blue intensity (must be between 0.0 and 1.0, inclusive)
	 */
	public static void renderBackground(double r, double g, double b) {
		renderTransparentBackground(r, g, b, 1.0);
	}

	/**
	 * Renders a colored rectangle with the specified color intensities and transparency level that fills up the game window.
	 * 
	 * @param r Red intensity (must be between 0.0 and 1.0, inclusive)
	 * @param g Green intensity (must be between 0.0 and 1.0, inclusive)
	 * @param b Blue intensity (must be between 0.0 and 1.0, inclusive)
	 * @param transparency The transparency factor of the rectangle (0 = entirely transparent, 1 = entirely opaque)
	 */
	public static void renderTransparentBackground(double r, double g, double b, double transparency) {
		renderTransparentColoredRectangle(0, 0, ChristmasCrashers.getWindowWidth(), ChristmasCrashers.getWindowHeight(), r, g, b, transparency);
	}

	/**
	 * Renders a fully opaque rectangular outline with the supplied attributes at the supplied location.
	 * @param x X-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param y Y-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param w Width of the rectangle, in pixels
	 * @param h Height of the rectangle, in pixels
	 * @param r Red intensity (must be between 0.0 and 1.0, inclusive)
	 * @param g Green intensity (must be between 0.0 and 1.0, inclusive)
	 * @param b Blue intensity (must be between 0.0 and 1.0, inclusive)
	 */
	public static void renderLinedRectangle(double x, double y, double w, double h, double r, double g, double b) {
		renderTransparentLinedRectangle(x, y, w, h, r, g, b, 1.0);
	}

	/**
	 * Renders a (partially) transparent rectangular outline with the supplied attributes at the supplied location.
	 * @param x X-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param y Y-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param w Width of the rectangle, in pixels
	 * @param h Height of the rectangle, in pixels
	 * @param r Red intensity (must be between 0.0 and 1.0, inclusive)
	 * @param g Green intensity (must be between 0.0 and 1.0, inclusive)
	 * @param b Blue intensity (must be between 0.0 and 1.0, inclusive)
	 * @param transparency The transparency factor of the rectangle (0 = entirely transparent, 1 = entirely opaque)
	 */
	public static void renderTransparentLinedRectangle(double x, double y, double w, double h, double r, double g, double b, double transparency) {
		glColor4d(r, g, b, transparency);
		glBegin(GL_LINE_LOOP);
			glVertex2d(x, y); // Top left
			glVertex2d(x + w, y); // Top right
			glVertex2d(x + w, y + h); // Bottom right
			glVertex2d(x, y + h); // Bottom left
		glEnd();
	}

	/**
	 * Renders a fully opaque colored rectangle with the supplied attributes at the supplied location.
	 * @param x X-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param y Y-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param w Width of the rectangle, in pixels
	 * @param h Height of the rectangle, in pixels
	 * @param r Red intensity (must be between 0.0 and 1.0, inclusive)
	 * @param g Green intensity (must be between 0.0 and 1.0, inclusive)
	 * @param b Blue intensity (must be between 0.0 and 1.0, inclusive)
	 */
	public static void renderColoredRectangle(double x, double y, double w, double h, double r, double g, double b) {
		renderTransparentColoredRectangle(x, y, w, h, r, g, b, 1.0);
	}

	/**
	 * Renders a (partially) transparent colored rectangle with the supplied attributes at the supplied location.
	 * @param x X-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param y Y-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param w Width of the rectangle, in pixels
	 * @param h Height of the rectangle, in pixels
	 * @param r Red intensity (must be between 0.0 and 1.0, inclusive)
	 * @param g Blue intensity (must be between 0.0 and 1.0, inclusive)
	 * @param b Green intensity (must be between 0.0 and 1.0, inclusive)
	 * @param transparency The transparency factor of the rectangle (0 = entirely transparent, 1 = entirely opaque)
	 */
	public static void renderTransparentColoredRectangle(double x, double y, double w, double h, double r, double g, double b, double transparency) {
		glDisable(GL_TEXTURE_2D);
		glColor4d(r, g, b, transparency);
		glBegin(GL_TRIANGLE_FAN);
			glVertex2d(x, y); // Top left
			glVertex2d(x + w, y); // Top right
			glVertex2d(x + w, y + h); // Bottom right
			glVertex2d(x, y + h); // Bottom left
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}

	/**
	 * Renders a fully opaque rectangle with a texture object (fitted to the rectangle).
	 * 
	 * @param x X-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param y Y-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param w Width of the rectangle, in pixels
	 * @param h Height of the rectangle, in pixels
	 * @param texture The texture object with which to fill the rectangle
	 */
	public static void renderTexturedRectangle(double x, double y, double w, double h, Texture texture) {
		renderTransparentTexturedRectangle(x, y, w, h, texture, 0.0, 0.0, 1.0, 1.0, 1.0);
	}

	/**
	 * Renders a fully opaque rectangle with a texture object and the supplied location and dimensions of the
	 * texture within the rectangle.
	 * 
	 * @param x X-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param y Y-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param w Width of the rectangle, in pixels
	 * @param h Height of the rectangle, in pixels
	 * @param texture The texture object with which to fill the rectangle
	 * @param texX X-coordinate of the top lefthand corner of the texture (pixel location within the rendering window)
	 * @param texY Y-coordinate of the top lefthand corner of the texture (pixel location within the rendering window)
	 * @param texW Width of the texture, in pixels
	 * @param texH Height of the texture, in pixels
	 */
	public static void renderTexturedRectangle(double x, double y, double w, double h, Texture texture, double texX, double texY,  double texW, double texH) {
		renderTransparentTexturedRectangle(x, y, w, h, texture, texX, texY, texW, texH, 1.0);
	}

	/**
	 * Renders a (partially) transparent rectangle with a texture object (fitted to the rectangle).
	 * 
	 * @param x X-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param y Y-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param w Width of the rectangle, in pixels
	 * @param h Height of the rectangle, in pixels
	 * @param texture The texture object with which to fill the rectangle
	 * @param transparency The transparency factor of the rectangle (0 = entirely transparent, 1 = entirely opaque)
	 */
	public static void renderTransparentTexturedRectangle(double x, double y, double w, double h, Texture texture, double transparency) {
		renderTransparentTexturedRectangle(x, y, w, h, texture, 0.0, 0.0, 1.0, 1.0, transparency);
	}

	/**
	 * Renders a (partially) transparent rectangle with a texture object and the supplied location and dimensions
	 *  of the texture within the rectangle.
	 *  
	 * @param x X-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param y Y-coordinate of the top lefthand corner of the rectangle (pixel location within the rendering window)
	 * @param w Width of the rectangle, in pixels
	 * @param h Height of the rectangle, in pixels
	 * @param texture The texture object with which to fill the rectangle
	 * @param texX X-coordinate of the top lefthand corner of the texture (pixel location within the rendering window)
	 * @param texY Y-coordinate of the top lefthand corner of the texture (pixel location within the rendering window)
	 * @param texW Width of the texture, in pixels
	 * @param texH Height of the texture, in pixels
	 * @param transparency The transparency factor of the rectangle (0 = entirely transparent, 1 = entirely opaque)
	 */
	public static void renderTransparentTexturedRectangle(double x, double y, double w, double h, Texture texture, double texX, double texY, double texW, double texH, double transparency) {
		glEnable(GL_TEXTURE_2D);
		glColor4d(1.0, 1.0, 1.0, transparency);
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		
		glBegin(GL_TRIANGLE_FAN);
			glTexCoord2d(texX, texY + texH);
			glVertex2d(x, y); // Top left
			
			glTexCoord2d(texX + texW, texY + texH);
			glVertex2d(x + w, y); // Top right
			
			glTexCoord2d(texX + texW, texY);
			glVertex2d(x + w, y + h); // Bottom right

			glTexCoord2d(texX, texY);
			glVertex2d(x, y + h); // Bottom left
		glEnd();
		glDisable(GL_TEXTURE_2D);
	}

	/**
	 * Renders a string in white.
	 * 
	 * @param string The contents of the string
	 * @param x X-coordinate of the top lefthand corner of the string (pixel location within the rendering window)
	 * @param y Y-coordinate of the top lefthand corner of the string (pixel location within the rendering window)
	 * @param font Font type of the string
	 */
	public static void renderString(String string, double x, double y, TrueTypeFont font) {		
		renderString(string, x, y, font, Color.white);
	}

	/**
	 * Render a string in the color supplied.
	 * 
	 * @param string The contents of the string
	 * @param x X-coordinate of the top lefthand corner of the string (pixel location within the rendering window)
	 * @param y Y-coordinate of the top lefthand corner of the string (pixel location within the rendering window)
	 * @param font Font type of the string
	 * @param color The color in which to display the string
	 */
	public static void renderString(String string, double x, double y, TrueTypeFont font, Color color) {
		TextureImpl.bindNone();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(GLGuru.getXDisplacement(), GLGuru.getXDisplacement() + ChristmasCrashers.getWindowWidth(), ChristmasCrashers.getWindowHeight() - GLGuru.getYDisplacement(), -GLGuru.getYDisplacement(), -GLGuru.getZDisplacement() + 1, -GLGuru.getZDisplacement() - 1);
		glMatrixMode(GL_MODELVIEW);
		font.drawString((int) x, (int) (ChristmasCrashers.getWindowHeight() - y - font.getHeight()), string, color);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(GLGuru.getXDisplacement(), GLGuru.getXDisplacement() + ChristmasCrashers.getWindowWidth(), GLGuru.getYDisplacement(), GLGuru.getYDisplacement() + ChristmasCrashers.getWindowHeight(), -GLGuru.getZDisplacement() + 1, -GLGuru.getZDisplacement() - 1);
		glMatrixMode(GL_MODELVIEW);
	}

	/**
	 * Loads a TrueType font from a .ttf file.
	 * 
	 * @param fontFileLocation The path to the TTF file (including its name)
	 * @param fontSize The size of the font
	 * @return The TrueTypeFont loaded
	 */
	public static TrueTypeFont loadFont(String fontFileLocation, int fontSize) { 
		Font startFont = null;
		try {
			startFont = Font.createFont(Font.TRUETYPE_FONT, new BufferedInputStream(ResourceLoader.getResourceAsStream(fontFileLocation)));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Font baseFont = startFont.deriveFont(Font.PLAIN, fontSize);
		return new TrueTypeFont(baseFont, false); // The boolean determines whether to anti-alias
	}
}
