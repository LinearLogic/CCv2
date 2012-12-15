package ss.linearlogic.christmascrashers.entity;

import org.lwjgl.util.vector.Vector2f;

import ss.linearlogic.christmascrashers.ChristmasCrashers;
import ss.linearlogic.christmascrashers.engine.Sprite;
import ss.linearlogic.christmascrashers.object.Object;
import ss.linearlogic.christmascrashers.state.GameState;
import ss.linearlogic.christmascrashers.state.StateType;

/**
 * Represents an in-game entity, which can move around and interact with objects and other entities in the level.
 * 
 * @author LinearLogic
 * @since 0.3.3
 */
public abstract class Entity {

	/**
	 * A 2 dimensional vector representing the entity's current movement speed in the x and y directions
	 */
	protected Vector2f movementVector;

	/**
	 * Whether this entity can pass through through objects, regardless of their penatrability (an entity that
	 * cannot pass through any object will still pass through objects that are flagged as penetrable)
	 */
	protected boolean canPenetrateObjects;

	/**
	 * Whether or not this entity's movement is effected by gravity
	 */
	protected boolean canFly;

	protected boolean airborne;

	/**
	 * The entity's {@link Sprite}
	 */
	protected Sprite sprite;

	/**
	 * Constructor - loads the entity's {@link #sprite} and passthrough flag ({@link #canPenetrateObjects}),
	 * sets whether the entity {@link #canFly can fly}, and initializes the entity's {@link #movementVector} to stationary.
	 * 
	 * @param sprite
	 * @param canPenetrateObjects
	 * @param canFly
	 */
	public Entity(Sprite sprite, boolean canPenetrateObjects, boolean canFly) {
		this.sprite = sprite;
		this.canPenetrateObjects = canPenetrateObjects;
		this.canFly = canFly;
		this.movementVector = new Vector2f(0, 0);
	}

	// TODO: add abstract update() method, which can call updatePosition()

	/**
	 * Adjusts the entity's position based on its {@link #movementVector} and handles collisions.
	 */
	public void updatePosition() {
		applyGravity();
		handleCollisionWithObject();
		sprite.incrementX((int) movementVector.getX());
		sprite.incrementY((int) movementVector.getY());
	}

	/**
	 * Applies the force of gravity to the entity's motion, if applicable.
	 */
	public void applyGravity() {
		if (canFly)
			return;
		if (movementVector.getY() > 0) {// Entity is travelling upwards - apply more gravity
			movementVector.setY((float) (movementVector.getY() - ((double) 5 * ChristmasCrashers.getDelta() / 170)));
		}
		else
			movementVector.setY((float) (movementVector.getY() - ((double) 3 * ChristmasCrashers.getDelta() / 100)));
	}

	/**
	 * Checks for a collision with an in-game object and if detected adjusts the entity's location accordingly. If
	 * the entity is able to {@link #canPenetrateObjects pass through objects}, this method returns immediately
	 * and does not adjust the entity's location.
	 * <p>To maximize efficiency, this method only checks the objects
	 * in contact with the sides of the entity in which the entity is moving. For instance, if the entity is moving
	 * to the right and down, only its its right and bottom faces will be checked for a collision with an object.
	 */
	protected void handleCollisionWithObject() {
		if (canPenetrateObjects)
			return;
		if (movementVector.getX() != 0) { // Entity is moving horizontally
			boolean movingLeft = true;
			int i = (int) Math.floor((sprite.getX() + movementVector.getX()) / Object.TILE_SIZE); // Default to the left face
			if (movementVector.getX() > 0) { // Object is moving to the right - switch to the right face
				movingLeft = false;
				i = (int) Math.floor((sprite.getX() + sprite.getWidth() + movementVector.getX()) / Object.TILE_SIZE);
			}
			for (int j = (int) Math.floor(sprite.getY() / Object.TILE_SIZE); j <= (int) Math.floor((sprite.getY() + sprite.getHeight()) / Object.TILE_SIZE); j++) {
				Object o = ((GameState) ChristmasCrashers.getState(StateType.GAME)).getCurrentLevel().getObject(i, j);
				if (o == null)
					continue;
				if (!o.getType().isPenetrable()) {
					if (movingLeft)
						sprite.setX((i + 1) * Object.TILE_SIZE);
					else
						sprite.setX((int) (Object.TILE_SIZE * Math.floor((sprite.getX() + movementVector.getX()) / Object.TILE_SIZE)) + (Object.TILE_SIZE - sprite.getWidth() - 1 /* Prevents a window offset glitch*/));
					movementVector.setX(0);
					break; // Prevent overcorrection if the entity is touching more than one non-penatrable object
				}
			}
		}
		if (movementVector.getY() != 0) { // Entity is moving vertically
			boolean movingDownward = true;
			int j = (int) Math.floor((sprite.getY() + movementVector.getY()) / Object.TILE_SIZE); // Default to the bottom face
			if (movementVector.getY() > 0) { // Object is moving upward - switch to the top face
				movingDownward = false;
				j = (int) Math.floor((sprite.getY() + sprite.getHeight() + movementVector.getY()) / Object.TILE_SIZE);
			}
			for (int i = (int) Math.floor(sprite.getX() / Object.TILE_SIZE); i <= (int) Math.floor((sprite.getX() + sprite.getWidth()) / Object.TILE_SIZE); i++) {
				if (j < 0 || j > 63) { // Location is out of range
					if (movingDownward) {
						sprite.setY(Object.TILE_SIZE); // Floor
						airborne = false;
					}
					else
						sprite.setY((int) (Object.TILE_SIZE * 63) + (Object.TILE_SIZE - sprite.getHeight() - 1 /* Prevents a window offset glitch*/));
					movementVector.setY(0);
					return;
				}
				Object o = ((GameState) ChristmasCrashers.getState(StateType.GAME)).getCurrentLevel().getObject(i, j);
				if (o == null)
					continue;
				if (!o.getType().isPenetrable()) {
					if (movingDownward) {
						sprite.setY((j + 1) * Object.TILE_SIZE);
						airborne = false;
					}
					else
						sprite.setY((int) (Object.TILE_SIZE * Math.floor((sprite.getY() + movementVector.getY()) / Object.TILE_SIZE)) + (Object.TILE_SIZE - sprite.getHeight() - 1 /* Prevents a window offset glitch*/));
					movementVector.setY(0);
					return; // Prevent overcorrection if the entity is touching more than one non-penatrable object
				}
			}
		}
	}

	/**
	 * Renders the entity's {@link #sprite}
	 */
	public void draw() {
		sprite.draw();
	}

	/**
	 * @return The entity's {@link #sprite}
	 */
	public Sprite getSprite() {
		return sprite;
	}

	/**
	 * @return the x-coordinate of the entity's {@link #sprite}
	 */
	public float getPixelX() {
		return sprite.getX();
	}

	/**
	 * Sets the x-coordinate of the entity's {@link #sprite} to the supplied integer value
	 * 
	 * @param x
	 */
	public void setPixelX(int x) {
		sprite.setX(x);
	}

	/**
	 * @return the y-coordinate of the entity's {@link #sprite}
	 */
	public float getPixelY() {
		return sprite.getY();
	}

	/**
	 * Sets the y-coordinate of the entity's {@link #sprite} to the supplied integer value
	 * 
	 * @param y
	 */
	public void setPixelY(int y) {
		sprite.setY(y);
	}

	/**
	 * @return The entity's {@link #movementVector}
	 */
	public Vector2f getMovementVector() {
		return movementVector;
	}

	/**
	 * @return Whether this entity can pass through objects, regardless of their penetrability
	 */
	public boolean canPenetrateObjects() {
		return canPenetrateObjects;
	}

	/**
	 * @return Whether the entity's motion is affected by gravity
	 */
	public boolean canFly() {
		return canFly;
	}

	/**
	 * Sets whether the entity can {@link #canFly fly}
	 * 
	 * @param canFly
	 */
	public void setFlyMode(boolean canFly) {
		this.canFly = canFly;
	}

	/**
	 * @return Whether the entity is currently airborne
	 */
	public boolean isAirborne() {
		return airborne;
	}

	/**
	 * Sets whether the entity is airborne
	 * 
	 * @param airborne
	 */
	public void setAirborne(boolean airborne) {
		this.airborne = airborne;
	}
}
