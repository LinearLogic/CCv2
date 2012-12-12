package ss.linearlogic.christmascrashers.entity;

import org.lwjgl.util.vector.Vector2f;

import ss.linearlogic.christmascrashers.engine.Sprite;

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
	 * Whether this entity can pass through through objects, regardless of their penatrability
	 */
	protected boolean canPenetrateObjects;

	/**
	 * Whether or not this entity's movement is effected by gravity
	 */
	protected boolean canFly;

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

	/**
	 * Adjusts the entity's position based on its {@link #movementVector} and handles collisions. <p>Different subclasses
	 * will add to this method in various ways - {@link LivingEntity} subclasses, for instance, will handle adjustments
	 * to health and {@link DamagingEntity} subclasses will handle the expiration of the entity on collision, if applicable.
	 */
	public abstract void updatePosition();

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
}
