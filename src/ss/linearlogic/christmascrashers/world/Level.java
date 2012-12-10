package ss.linearlogic.christmascrashers.world;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import ss.linearlogic.christmascrashers.ChristmasCrashers;
import ss.linearlogic.christmascrashers.object.Object;
import ss.linearlogic.christmascrashers.object.ObjectType;

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
	 * The ID of the world the level belongs to
	 */
	private int worldID;

	/**
	 * A 2D Array of all the in-game {@link Object objects}
	 */
	private Object[][] objects = new Object[WIDTH][HEIGHT];

	/**
	 * Constructor - creates a Level object with the given world name and ID. The level's contents are not loaded separately, by
	 * calling the {@link #load()} method (if the level is being made from scratch in the Level Editor, this call will not be made).
	 * 
	 * @param worldID The ID of the world the level will belong to
	 * @param ID The level's unique {@link #ID} value
	 */
	public Level(int worldID, int ID) {
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				objects[i][j] = new Object(ObjectType.AIR);
			}
		}
		this.worldID = worldID;
		this.ID = ID;
	}

	/**
	 * Loads the level file and reads from it to load the level's 2D array of blocks and entities.
	 */
	public void load() {
		if (ChristmasCrashers.isDebugModeEnabled() )
			System.out.println("Loading level " + ID + " in world " + worldID + ".");
		// TODO: load data from the file
	}

	/**
	 * Saves the level, in its current state, to its system data file
	 */
	public void save() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Saving level " + ID + " in world " + worldID + ".");
		File dataFile = new File(getDiskLocation());
		if (dataFile.exists())
			dataFile.mkdirs();
		FileWriter fw = null;
		try {
			fw = new FileWriter(getDiskLocation());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fw == null)
			return;
		PrintWriter pw = new PrintWriter(fw);
		// TODO: write data to the file
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

	/**
	 * @return The ID of the world the level belongs to
	 */
	public int getWorldID() {
		return worldID;
	}

//	Currently disabled, as levels should not be moved from world to world
//	/**
//	 * Sets the {@link #worldID} to the supplied value
//	 * @param ID
//	 */
//	public void setWorldID(int ID) {
//		this.worldID = ID;
//	}

	/**
	 * @return The {@link #diskLocation} of the level's data file
	 */
	public String getDiskLocation() {
		return "files" + File.separator + "world" + worldID + File.separator + "level" + ID;
	}
}
