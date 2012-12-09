package ss.linearlogic.christmascrashers.world;

import java.io.File;

import ss.linearlogic.christmascrashers.ChristmasCrashers;
import ss.linearlogic.christmascrashers.util.FileMonkey;

/**
 * Starts a new thread that deletes the directories and files of the {@link World} objects provided in the {@link #DeleteWorldsTask(World[]) constructor}.
 * 
 * @author LinearLogic
 * @since 0.2.4
 */
public class DeleteWorldsTask implements Runnable {

	/**
	 * An Array of the worlds to erased from the disk
	 */
	private World[] worldsToDelete;

	/**
	 * Deletes the folders and files of the worlds in {@link #worldsToDelete}
	 */
	public void run() {
		if (ChristmasCrashers.isDebugModeEnabled())	
			System.out.println("Starting a new DeleteWorldsTask...");
		for (World world : this.worldsToDelete) {
			FileMonkey.delete(new File(world.getDiskLocation()));
		}
	}

	/**
	 * Constructs the task with the worlds to be erased from the disk
	 * 
	 * @param worlds The {@link World} objects whose directories to delete
	 */
	public DeleteWorldsTask(World[] worlds) {
		this.worldsToDelete = worlds;
	}
}
