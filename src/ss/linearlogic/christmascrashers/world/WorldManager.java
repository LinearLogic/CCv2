package ss.linearlogic.christmascrashers.world;

import java.util.ArrayList;

import ss.linearlogic.christmascrashers.ChristmasCrashers;

/**
 * The World Manager is a static class containing the list of currently loaded worlds as well as methods for a
 * number of operations, such as loading and saving worlds, creating new worlds and deleting old ones, and renaming worlds.
 * <p>There are 5 world slots, and the ID of each world is the slot number (0-4).
 * 
 * @author LinearLogic
 * @since 0.2.2
 */
public class WorldManager {

	/**
	 * The worlds Array contains all the detected {@link World Worlds} (1 world for each of the 5 slots), which are stored in the ../files/worlds directory.
	 * Worlds are detected and loaded in the {@link #loadWorlds()} method.
	 */
	private static World[] worlds = new World[5];

	/**
	 * Loads all of the worlds detected in the ..files/worlds directory by calling the {@link World#load()} method for each.
	 * This method is run in a thread of its own so as to prevent a performance bottleneck.
	 */
	public static void loadWorlds() {
		if (containsLoadedWorlds())
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("[Warning] LoadWorldsTask is overwriting one or more currently loaded worlds.");
		new Thread(new LoadWorldsTask(worlds)).start();
	}

	/**
	 * Checks if the world with the specified ID exists, and if so, loads it from the disk.
	 * @param ID
	 */
	public static void loadWorld(int ID) {
		if (ID < 0 || ID > 4) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.err.println("Failed to load world " + ID + " - invalid world ID (must be between 0 and 4, inclusive).");
			return;
		}
		if (worlds[ID] != null)
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("[Warning] LoadWorldsTask is overwriting world " + ID + ".");
		World[] worldsToLoad = {new World(ID)};
		new Thread(new LoadWorldsTask(worldsToLoad)).start();
	}

	/**
	 * Runs a {@link SaveWorldsTask}, passing all the the currently loaded {@link World} objects to be saved.
	 */
	public static void saveWorlds() {
		if (!containsLoadedWorlds()) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("Cancelling the SaveWorldTask - there are no loaded worlds.");
			return;
		}
		new Thread(new SaveWorldsTask(worlds)).start();
	}

	/**
	 * Runs a {@link SaveWorldsTask}, passing the world with the provided ID
	 * 
	 * @param ID The ID of the world to be saved to the disk
	 */
	public static void saveWorld(int ID) {
		if (ID < 0 || ID > 4) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.err.println("Failed to save world " + ID + " - invalid world ID (must be between 0 and 4, inclusive).");
			return;
		}
		if (worlds[ID] == null) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.err.println("Failed to save world " + ID + " - world does not exist.");
			return;
		}
		World[] worldsToSave = {worlds[ID]};
		new Thread(new SaveWorldsTask(worldsToSave)).start();
	}

	/**
	 * @return The contents of the {@link #worlds} ArrayList, as an Array of {@link World} objects.
	 */
	public static World[] getWorlds() {
		return worlds;
	}

	/** 
	 * @param ID The ID of the world to retrieve from the list of currently loaded worlds
	 * @return The {@link World world} at the specified location in the {@link #worlds} Array (the world's ID
	 * is the same as its index in the list).  If no World object has been loaded with the provided ID, or if the
	 * ID is not within range (between 0 and 4, inclusive), then the method returns null.
	 */
	public static World getWorld(int ID) {
		if (ID < 0 || ID > 5) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.err.println("Could not retrieve world " + ID + " - invalid level ID (must be between 0 and 4, inclusive).");
			return null;
		}
		return worlds[ID];
	}	

	/**
	 * Adds the specified world to the {@link #worlds list of loaded worlds}
	 * @param world
	 */
	public static void addWorld(World world) {
		if (world == null) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.err.println("Failed to add world - null World object provided.");
			return;
		}
		if (world.getID() < 0 || world.getID() > 4) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.err.println("Failed to add world " + world.getID() + " - invalid world ID (must be between 0 and 4, inclusive).");
			return;
		}
		if (worlds[world.getID()] != null) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("[Warning] Overwriting world " + world.getID() + ".");
		}
		worlds[world.getID()] = world;
	}

	/**
	 * Deletes all of the worlds in the {@link #worlds list of loaded worlds}
	 */
	public static void deleteWorlds() {
		if (!containsLoadedWorlds()) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("Cancelling DeleteWorldsTask - there are no loaded worlds.");
			return;
		}
		ArrayList<World> worldsToDelete = new ArrayList<World>();
		
		for (World w : worlds)
			if (w != null)
				worldsToDelete.add(w);
		new Thread(new DeleteWorldsTask((World[]) worldsToDelete.toArray())).start();
		for (World w : worldsToDelete)
			worlds[w.getID()] = null;
	}

	/**
	 * Removes the world with the specified ID from the {@link #worlds list of loaded worlds} and deletes its system files.
	 * 
	 * @param ID The ID of the world to be saved to the disk
	 */
	public static void deleteWorld(int ID) {
		if (ID < 0 || ID > 4) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.err.println("Failed to delete world " + ID + " - invalid world ID (must be between 0 and 4, inclusive).");
			return;
		}
		if (worlds[ID] == null) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.err.println("Failed to delete world " + ID + " - world does not exist.");
			return;
		}
		World[] worldsToDelete = {worlds[ID]};
		new Thread(new DeleteWorldsTask(worldsToDelete)).start();
		worlds[ID] = null;
	}

	/**
	 * @return True iff the {@link #worlds} Array contains one or more World objects (that aren't null)
	 */
	public static boolean containsLoadedWorlds() {
		for (int i = 0; i < 5; i++)
			if (worlds[i] != null)
				return true;
		return false;
	}
}
