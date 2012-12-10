package ss.linearlogic.christmascrashers.object;

import ss.linearlogic.christmascrashers.world.Level;

/**
 * In-game object interface, implemented by the {@link Object} class.
 * 
 * @author LinearLogic
 * @since 0.2.5
 */
public interface IObject {

	/**
	 * The uniform length and width, in pixels, of each object's texture tile.
	 */
	public static final int TILE_SIZE = 32;

	/**
	 * @return The {@link Level} that contains the object
	 */
	public Level getLevel();

	/**
	 * Sets the {@link Level} that contains the object to the supplied level
	 * 
	 * @param level
	 */
	public void setLevel(Level level);

	/**
	 * @return The tile x-coordinate of the object
	 */
	public int getX();

	/**
	 * Sets the tile x-coordinate of the object to the specified value
	 * @param x
	 */
	public void setX(int x);

	/**
	 * @return The tile y-coordinate of the object
	 */
	public int getY();

	/**
	 * Sets the tile y-coordinate of the object to the specified value
	 * 
	 * @param y
	 */
	public void setY(int y);

	/**
	 * @return The pixel x-coordinate of the object. Note that this coordinate is universal, meaning that it doesn't
	 * change when the camera changes and is not confined to a coordinate within the game window.
	 */
	public int getPixelX();

	/**
	 * @return The pixel y-coordinate of the object. Note that this coordinate is universal, meaning that it doesn't
	 * change when the camera changes and is not confined to a coordinate within the game window.
	 */
	public int getPixelY();

	/**
	 * @return The {@link ObjectType type} of the object
	 */
	public ObjectType getType();

	/**
	 * Sets the type of the object to the supplied {@link ObjectType}.
	 * 
	 * @param type
	 */
	public void setType(ObjectType type);
}
