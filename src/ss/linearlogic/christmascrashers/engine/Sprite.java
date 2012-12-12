package ss.linearlogic.christmascrashers.engine;

import org.newdawn.slick.opengl.Texture;

/**
 * A Sprite is an image tile consisting of location and dimension variables and a Texture object. Sprites are used
 * for things that are meant to change location, such as {@link Entity} subclasses like the Player.
 * 
 * @author LinearLogic
 * @since 0.3.4
 */
public class Sprite {

	/**
	 * The pixel x-coordinate of the bottom-lefthand corner of the sprite within the level (not confined to being
	 * within the game window/camera view)
	 */
	private int x;

	/**
	 * The pixel y-coordinate of the bottom-lefthand corner of the sprite within the level (not confined to being
	 * within the game window/camera view)
	 */
	private int y;

	/**
	 * The width, in pixels, of the sprite
	 */
	private int width;

	/**
	 * The height, in pixels, of the sprite
	 */
	private int height;

	/**
	 * The Texture object that provides the image for the sprite
	 */
	private Texture texture;

	/**
	 * Constructs the Sprite object with the specified {@link #x}, {@link #y}, {@link #width} and {@link #height} values,
	 * and sets the sprite's {@link #texture} to the specified Texture object. Note that the texture's dimensions are
	 * adjusted to match the width and height of the sprite.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param tex
	 */
	public Sprite(int x, int y, int width, int height, Texture tex) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.texture = tex;
	}

	/**
	 * Renders the sprite's {@link #texture} at the sprite's {@link #x} and {@link #y} coordinates and with
	 * the sprite's {@link #width} and {@link #height}.
	 */
	public void draw() {
		RenderMonkey.renderTexturedRectangle(x, y, width, height, texture);
	}

	/**
	 * @return The {@link #x x-coordinate} of the sprite
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the {@link #x x-coordinate} of the sprite to the specified integer value
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return The {@link #y y-coordinate} of the sprite
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the {@link #y y-coordinate} of the sprite to the specified integer value
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return The {@link #width} of the sprite
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the {@link #width} of the sprite to the specified integer value.
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return The {@link #height} of the sprite
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the {@link #height} of the sprite to the specified integer value.
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return The {@link #texture} of the sprite
	 */
	public Texture getTexture() {
		return texture;
	}

	/**
	 * Sets the sprite's {@link #texture} to the specified Texture object
	 * 
	 * @param tex
	 */
	public void setTexture(Texture tex) {
		this.texture = tex;
	}
}