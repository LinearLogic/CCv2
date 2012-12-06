package ss.linearlogic.christmascrashers.state;

import ss.linearlogic.christmascrashers.ChristmasCrashers;

/**
 * The window state is selected when an in-game window pops up, such as a respawn prompt. While
 * a game window is active, no in-game objects or entities will be rendered. Multiple game windows
 * can be open, but only one can be active (receiving and responding to input, and updating).
 * 
 * @author LinearLogic
 * @since 0.0.2
 */
public abstract class WindowState extends State {

	/**
	 * The priority the window takes over other windows - only the window with the highest priority will be
	 * rendered and if two windows with the same priority are open, the most recently opened window will
	 * be rendered (this is determined based on each window's time of {@link #birth time of birth}.
	 */
	protected int priority;

	/**
	 * The adjusted system time at which the window object was created.
	 */
	protected long birth;

	/**
	 * WindowState superclass constructor - sets the {@link #priority} of the window object to the specified integer
	 * value and the {@link #birth} value to the current system time (retrieved with {@link ChristmasCrashers#getTime()}).
	 * @param priority The priority of the window object being constructed
	 */
	public WindowState(int priority) {
		this.priority = priority;
		birth = ChristmasCrashers.getTime();
	}
}
