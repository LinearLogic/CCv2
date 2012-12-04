package fostering.evil.christmascrashers.state;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

/**
 * The State superclass, which implements the {@link StateInterface} and defines methods for handling of
 * {@link #importantKeys}. The methods declared in the {@link StateInterface are implemented in full in
 * the subclasses of this class.
 * 
 * @author LinearLogic
 *@since 0.0.2
 */
public abstract class State implements StateInterface{

	/**
	 * Contains the integer IDs of all the keyboard keys that should be monitored. If any of them are
	 * depressed, the {@link #keyDown} value will be 'true'
	 */
	protected ArrayList<Integer> importantKeys = new ArrayList<Integer>();
	
	/**
	 * Represents the collective status of all {@link #importantKeys}. Only if its value is 'true' will new
	 * keyboard input be handled. In the case of the {@link MainMenuState}, the left mouse button also
	 * affects the status of the keyDown flag.
	 */
	protected static boolean keyDown;
	
	/**
	 * Iterates through the keyIDs in {@link #importantKeys}, checking the state of each key. If one of
	 * the relevant keys is depressed, function returns 'true'. Otherwise, it returns 'false'.
	 */
	protected void checkKeyStates() {
		if (keyDown) {
			for (int keyID : importantKeys)
				if (Keyboard.isKeyDown(keyID))
					return;
		}
		keyDown = false;
	}
	
	/**
	 * @return The value of the {@link #keyDown} variable
	 */
	public static boolean getKeyDown() {
		return keyDown;
	}
	/**
	 * Sets the value of {@link #keyDown} to the supplied boolean
	 * @param value The new key state (true if down, false if up)
	 */
	public static void setKeyDown(boolean value) {
		keyDown = value;
	}
	
	@Override
	public void addImportantKey(int keyID) {
		if (importantKeys.contains(keyID))
			return;
		importantKeys.add(keyID);
	}
	
	@Override
	public void removeImportantKey(int keyID) {
		if (!importantKeys.contains(keyID))
			return;
		importantKeys.remove(keyID);
	}
}
