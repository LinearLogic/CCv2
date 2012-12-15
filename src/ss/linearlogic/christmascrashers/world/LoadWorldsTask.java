package ss.linearlogic.christmascrashers.world;

import ss.linearlogic.christmascrashers.ChristmasCrashers;
import ss.linearlogic.christmascrashers.state.IntroState;
import ss.linearlogic.christmascrashers.state.StateType;

/**
 * Starts a new thread and loads World objects from files in the ..files/worlds directory branch.
 * 
 * @author LinearLogic
 * @since 0.2.3
 */
public class LoadWorldsTask implements Runnable {

	/**
	 * An Array of the worlds to load from the disk
	 */
	private World[] worldsToLoad;

	/**
	 * Runs the thread that loads the {@link #worldsToLoad}
	 */
	public void run() {
		if (ChristmasCrashers.isDebugModeEnabled())	
			System.out.println("Starting a new LoadWorldsTask...");
		for (World w : this.worldsToLoad) { // Load the detected worlds
			w.load();
		}
		try {
			Thread.sleep(4000); // Just to make things look realistic ;-)
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		((IntroState) ChristmasCrashers.getState(StateType.INTRO)).setAnimationComplete(true); // Move on from the loading screen splash
	}

	/**
	 * Constructs the task with the worlds to be loaded from the disk
	 * 
	 * @param worlds The {@link World} objects to load
	 */
	public LoadWorldsTask(World[] worlds) {
		this.worldsToLoad = worlds;
	}
}