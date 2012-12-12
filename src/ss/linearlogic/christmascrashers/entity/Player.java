package ss.linearlogic.christmascrashers.entity;

import java.io.File;

import ss.linearlogic.christmascrashers.engine.Sprite;
import ss.linearlogic.christmascrashers.object.Object;
import ss.linearlogic.christmascrashers.util.TextureMonkey;

/**
 * Represents an in-game player, a {@link LivingEntity} subclass with special attributes, such as lives and an inventory.
 *  
 * @author LinearLogic
 * @since 0.3.5
 */
public class Player extends LivingEntity {

	/**
	 * The current number of lives remaining, which is less than or equal to the player's {@link #maxLives}.
	 * This value is decremented each time the player dies. If the player dies with no lives left, the game is over.
	 */
	private int lives;

	/**
	 * The maximum number of lives the player can have
	 */
	private int maxLives;

	/**
	 * Contructor - calls the superclass {@link LivingEntity#LivingEntity(Sprite, int, int, boolean, boolean, boolean) constructor}
	 * with the default values for a Player and sets the coordinates of the player's location (in tiles, NOT pixels)
	 * to the provided integer values.
	 * 
	 * @param x The tile x-coordinate of the Player within the current level
	 * @param y The tile y-coordinate of the Player within the current level
	 */
	public Player(int x, int y) {
		super(new Sprite(x * Object.TILE_SIZE, y * Object.TILE_SIZE, 28, 28, TextureMonkey.loadTexture("PNG", "files" + File.separator + "player.png")), 100, 0, false, false, false);
	}

	@Override
	public void handleDeath() {
		
	}

	@Override
	public void updatePosition() {
		sprite.incrementX((int) movementVector.getX());
		sprite.incrementY((int) movementVector.getY());
		movementVector.setX(0);
		movementVector.setY(0);
	}

	/**
	 * @return The number of {@link #lives} the player has remaining
	 */
	public int getLives() {
		return lives;
	}

	/**
	 * Sets the number of remaining {@link #lives} the player has to the specified integer value.
	 * 
	 * @param lives
	 */
	public void setLives(int lives) {
		this.lives = lives;
	}

	/**
	 * @return The maximum number of lives the player can have
	 */
	public int getMaxLives() {
		return maxLives;
	}

	/**
	 * Sets the maximum number of lives the player can have to the specified integer value.
	 * 
	 * @param maxLives
	 */
	public void setMaxLives(int maxLives) {
		this.maxLives = maxLives;
	}
}
