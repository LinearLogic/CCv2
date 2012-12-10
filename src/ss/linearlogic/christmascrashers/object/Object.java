package ss.linearlogic.christmascrashers.object;

import ss.linearlogic.christmascrashers.world.Level;

/**
 * Represents an in-game object. Every object has a location (consisting of a {@link Level} and two-dimensional coordinates)
 * and an {@link ObjectType}.
 * 
 * @author LinearLogic
 * @since 0.2.5
 */
public class Object implements IObject {

	/**
	 * The {@link Level} that contains the object
	 */
	private Level level;

	/**
	 * The tile x-coordinate of the object within the level
	 */
	private int x;

	/**
	 * The tile y-coordinate of the object within the level
	 */
	private int y;

	/**
	 * The object's {@link ObjectType type}
	 */
	private ObjectType type;

	/**
	 * Creates an in-game object with the given {@link ObjectType type} at the given location.
	 * 
	 * @param level The {@link #level} containing the object
	 * @param x The {@link #x x-coordinate} of the object
	 * @param y The {@link #y y-coordinate} of the object
	 * @param type The {@link ObjectType} of the object
	 */
	public Object(Level level, int x, int y, ObjectType type) {
		this.type = type;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getPixelX() {
		return x * TILE_SIZE;
	}

	public int getPixelY() {
		return y * TILE_SIZE;
	}

	public ObjectType getType() {
		return type;
	}

	public void setType(ObjectType type) {
		this.type = type;
	}
}
