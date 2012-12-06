package ss.linearlogic.christmascrashers.world;

import java.util.ArrayList;

import ss.linearlogic.christmascrashers.ChristmasCrashers;

/**
 * A game world contains one or more {@link Level Levels}, connected via teleporters. Each level, in turn, contains
 * blocks and entities that the player interacts with while moving around the level. 
 * <p>Worlds can be created and populated in the Level Editor, and the player chooses the world to play in
 * from a menu presented immediately after clicking the "Start Game" button.
 * 
 * @author LinearLogic
 * @since 0.2.1
 */
public class World {

	/**
	 * The name of the world, which serves as a means of outwardly identifying the world (internall, worlds are
	 * managed by their {@link #ID} values)
	 */
	private String name;

	/**
	 * The world's unique integer ID value, which servers as a means of differentiating the world from others
	 */
	private int ID;

	/**
	 * The file path to the world directory (which bears the same name as the world) containing the world's level and settings files
	 */
	private String diskLocation;

	/**
	 * An ArrayList of all the {@link Level Levels} in the world, with level {@link #ID}s as index values
	 */
	private ArrayList<Level> levels = new ArrayList<Level>();

	/**
	 * Constructor - creates a World object with the given name, ID, and system path. The rest of the attributes
	 * of the world are loaded from the files in the world directory in the loadLeves() and loadSettings() methods.
	 * @param name The {@link #name} of the world
	 * @param ID The {@link #getID()} value of the world
	 * @param location The {@link #diskLocation} (system path) or the world's root directory
	 */
	public World(String name, int ID, String location) {
		this.name = name;
		this.ID = ID;
		this.diskLocation = location;
	}

	/**
	 * @return The name of the world
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The new {@link name} of the world
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The {@link ID} value of the world
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param ID The new {@link ID} value of the world
	 */
	public void setID(int ID) {
		this.ID = ID;
	}

	/**
	 * @return The {@link diskLocation} of the world folder
	 */
	public String getDiskLocation() {
		return diskLocation;
	}

	/**
	 * @param location The new {@link diskLocation} for the world folder
	 */
	public void setDiskLocation(String location) {
		this.diskLocation = location;
	}

	/**
	 * @return The {@link #levels} that the world contains
	 */
	public ArrayList<Level> getLevels() {
		return levels;
	}
	
	/**
	 * @param index
	 * @return The {@link Level} at the supplied index
	 */
	public Level getLevel(int index) {
		return levels.get(index);
	}
	
	/**
	 * Adds a level to the {@link #levels} ArrayList, with the provided level's ID as the index.
	 * 
	 * @param level The {@link Level} to add
	 */
	public void addLevel(Level level) {
		if (levels.get(level.getID()) != null)
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("[Warning] Overwriting level " + level.getID() + " in world " + ID + ".");
		levels.add(level.getID(), level);
	}
	
	/**
	 * Deletes the level at the provided index within the {@link #levels} ArrayList
	 * 
	 * @param index
	 */
	public void removeLevel(int index) {
		levels.remove(index);
	}
}
