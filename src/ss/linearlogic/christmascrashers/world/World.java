package ss.linearlogic.christmascrashers.world;

import java.io.File;

import ss.linearlogic.christmascrashers.ChristmasCrashers;

/**
 * A game world contains one or more {@link Level} objects, connected via teleporters. Each level, in turn, contains
 * blocks and entities that the player interacts with while moving around the level. 
 * <p>Note that for a world to be detected, its directory must follow the format "world#" where # is the slot
 * number between 0 and 4, inclusive.
 * <p>Worlds can be created and populated in the Level Editor, and the player chooses the world to play in
 * from a menu presented immediately after clicking the "Start Game" button.
 * 
 * @author LinearLogic
 * @since 0.2.1
 */
public class World {

	/**
	 * The world's unique integer ID value, which servers as a means of differentiating the world from others
	 * and determining the system path to the world's data directory.
	 */
	private int ID;

	/**
	 * An Array of all the {@link Level Levels} in the world, with level {@link #ID}s as index values. There can be
	 * up to 5 levels per world.
	 */
	private Level[] levels = new Level[5];

	/**
	 * Constructor - creates a World object with the giveen name, ID, and system path. The rest of the attributes
	 * of the world are loaded from the files in the world directory in the loadLeves() and loadSettings() methods.
	 * @param name The {@link #name} of the world
	 * @param ID The {@link #getID()} value of the world
	 * @param location The {@link #diskLocation} (system path) or the world's root directory
	 */
	public World(int ID) {
		this.ID = ID;
	}

	/**
	 * Loads the {@Level levels} in the world from their respective data files in the world directory.
	 */
	public void load() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Loading world " + ID + ".");
		File worldDir = new File(getDiskLocation());
		if (!worldDir.exists()) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("World " + ID + " does not exist, creating it. System path to world directory: .." + getDiskLocation());
			worldDir.mkdirs();
			return;
		}
		for (File levelFile : worldDir.listFiles()) { // Iterate through files in the world folder, only loading the ones that match the level file format
			if (levelFile.getName().length() == 9 && levelFile.getName().substring(0, 5).equalsIgnoreCase("level"))
				if (Character.isDigit(levelFile.getName().charAt(5))) {
					int levelID = Character.getNumericValue(levelFile.getName().charAt(5));
					if (levelID >= 0 && levelID <= 4) // in range
						levels[levelID] = new Level(this.ID, levelID);
				}
		}
		if (!containsLoadedLevels()) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("No levels were found for world " + this.ID + " - check to make sure the level files are properly named. Aborting loading of world " + this.ID + ".");
			return;
		}
		for (Level l : levels)
			if (l != null)
				l.load();
	}
	

	/**
	 * {@link Level#save() Saves} each of the levels in the world, writing their data to the disk.
	 */
	public void save() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Saving world " + ID + ".");
		if (!containsLoadedLevels()) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("Aborting save operation for world "  + ID + " - world does not contain any loaded levels.");
			return;
		}
		for (Level l : levels)
			if (l != null)
				l.save();
	}

	/**
	 * @return The {@link #levels} that the world contains
	 */
	public Level[] getLevels() {
		return levels;
	}

	/**
	 * @param ID The ID of the level to retrieve from the list of currently loaded level
	 * @return The {@link Level level} at the specified location in the {@link #levels} Array (the level's ID is
	 * the same as its index in the list).  If no Level object has been loaded with the provided ID, or if the
	 * ID is not within range (between 0 and 4, inclusive), then the method returns null.
	 */
	public Level getLevel(int ID) {
		if (ID < 0 || ID > 5) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.err.println("Could not retrieve level " + ID + " in world " + this.ID + " - invalid level ID (must be between 0 and 4, inclusive).");
			return null;
		}
		return levels[ID];
	}

	/**
	 * Adds a level to the {@link #levels} ArrayList, with the provided level's ID as the index.
	 * 
	 * @param level The {@link Level} to add
	 */
	public void addLevel(Level level) {
		if (level == null) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.err.println("Failed to add level to world " + ID + " - null Level object provided.");
			return;
		}
		if (level.getID() < 0 || level.getID() > 4) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.err.println("Failed to add level " + level.getID() + " to world " + ID + " - invalid level ID (must be between 0 and 4, inclusive).");
			return;
		}
		if (levels[level.getID()] != null)
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("[Warning] Overwriting level " + level.getID() + " in world " + ID + ".");
		levels[level.getID()] = level;
		level.load();
	}

	/**
	 * Deletes the level with the specified ID
	 * 
	 * @param ID The ID of the level to remove
	 */
	public void deleteLevel(int ID) {
		if (ID < 0 || ID > 4) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.err.println("Failed to delete level " + ID + " - invalid level ID (must be between 0 and 4, inclusive).");
			return;
		}
		if (levels[ID] == null) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.err.println("Failed to delete level " + ID + " - level does not exist.");
			return;
		}
		levels[ID] = null;
	}

	/**
	 * @return True iff the {@link #levels} Array contains one or more Level objects (that aren't null)
	 */
	public boolean containsLoadedLevels() {
		for (int i = 0; i < 5; i++)
			if (levels[i] != null)
				return true;
		return false;
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
	 * @return The system path to the world folder
	 */
	public String getDiskLocation() {
		return "files" + File.separator + "worlds" + File.separator + "world" + ID;
	}
}
