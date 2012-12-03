package fostering.evil.christmascrashers.state;

import java.util.ArrayList;

/**
 * The game state is where most of the user's time is spent, and includes handling of all relevant forms
 * of input and renders all the in-game objects and entities.
 * 
 * @author LinearLogic
 * @since 0.0.2
 */
public class GameState extends State {

	ArrayList<Integer> importantKeys = new ArrayList<Integer>();
	@Override
	public StateType handleInput() {
		// TODO: keyboard handling
		return null;
	}

	@Override
	public void logic() {
		
	}

	@Override
	public void draw() {
		
	}
}
