package ss.linearlogic.christmascrashers.world;

/**
 * Levels are what the player moves through and contain a 2D array of blocks and entities, with which the player
 * interacts while travelling around the level. All levels are fixed size, namely 640 by 320 tiles.
 * <p>Levels contain waypoints, including teleport checkpoint/respawn locations,
 * and are bundled together in groups called {@link World Worlds}.
 * 
 * @author LinearLogic
 * @since 0.2.1
 */
public class Level {

	/**
	 * The width, in tiles, of the level
	 */
	public static final int WIDTH = 64;

	/**
	 * The height, in tiles, of the level
	 */
	public static final int HEIGHT = 32;

	/**
	 * The level's unique integer ID value, which servers as a means of differentiating it from other levels in the
	 * same world. The level's ID is also featured in the name of the level's data file.
	 */
	private int ID;

	/**
	 * Constructor - creates a Level object with the given ID. The level's contents are not loaded separately, by
	 * calling the load() method (if the level is being made from scratch in the Level Editor, this call will not be made).
	 * 
	 * @param ID
	 */
	public Level(int ID) {
		this.ID = ID;
	}
	/**
	 * @return The {@link ID} value of the level
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param ID The new {@link ID} value of the level
	 */
	public void setID(int ID) {
		this.ID = ID;
	}
}
