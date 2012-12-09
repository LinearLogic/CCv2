package ss.linearlogic.christmascrashers.world;

import ss.linearlogic.christmascrashers.ChristmasCrashers;

/**
 * Runs a thread that saves all the {@link World} objects (provided in the {@link #SaveWorldsTask(World[])} constructor})
 * to the disk location of the world.
 * 
 * @author LinearLogic
 * @since 0.2.3
 */
public class SaveWorldsTask implements Runnable {

	/**
	 * An Array of the worlds to be saved to the disk
	 */
	private World[] worldsToSave;

	/**
	 * Saves the worlds in {@link #worldsToSave}
	 */
	public void run() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Starting a new SaveWorldsTask...");
		for (World w : this.worldsToSave) {
			w.save();
		}
	}

	/**
	 * Constructs the task with the worlds to be saved to the disk.
	 * 
	 * @param worlds The {@link World} objects to save
	 */
	public SaveWorldsTask(World[] worlds) {
		this.worldsToSave = worlds;
	}
}