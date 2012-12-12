package ss.linearlogic.christmascrashers.entity;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

/**
 * Represents an in-game entity, which can move around and interact with objects and other entities in the level.
 * 
 * @author LinearLogic
 * @since 0.3.3
 */
public abstract class Entity {

	/**
	 * The pixel x-coordinate of the bottom left corner of the entity in the level (does not change when the camera shifts)
	 */
	private float pixelX;

	/**
	 * The pixel y-coordinate of the bottom left corner of the entity in the level (does not change when the camera shifts)
	 */
	private float pixelY;

	/**
	 * A 2 dimensional vector representing the entity's current movement speed in the x and y directions
	 */
	private Vector2f movementVector;

	/**
	 * Whether this entity can pass through through objects, regardless of their penatrability
	 */
	private boolean canPenetrateObjects;

	/**
	 * Whether or not this entity's movement is effected by gravity
	 */
	private boolean canFly;

	/**
	 * The entity's sprite image
	 */
	private Texture texture;

	/**
	 * Constructor - sets the {@link #pixelX} and {@link #pixelY} variables and passthrough flag ({@link #canPenetrateObjects()}),
	 * sets whether the entity can fly, and initializes the entity's {@link #movementVector} to stationary. Lastly, the constructor sets the
	 * entity's {@link #texture sprite image} to the provided Texture object.
	 * 
	 * @param pixelX
	 * @param pixelY
	 * @param canPenetrateObjects
	 * @param canFly
	 * @param tex
	 */
	public Entity(float pixelX, float pixelY, boolean canPenetrateObjects, boolean canFly, Texture tex) {
		this.pixelX = pixelX;
		this.pixelY = pixelY;
		this.canPenetrateObjects = canPenetrateObjects;
		this.canFly = canFly;
		this.texture = tex;
		this.movementVector = new Vector2f(0, 0);
	}

	/**
	 * Adjusts the entity's position based on its {@link #movementVector} and handles collisions
	 */
	public abstract void updatePosition();

	/**
	 * Renders the entity at its current location
	 */
	public abstract void draw();

	/**
	 * @return The {@link #pixelX x-coordinate}, in pixels, of the entity
	 */
	public float getPixelX() {
		return pixelX;
	}

	/**
	 * Sets the {@link #pixelX x-coordinate}, in pixels, of the entity to the provided float value.
	 * 
	 * @param x
	 */
	public void setPixelX(int x) {
		this.pixelX = x;
	}

	/**
	 * @return The {@link #pixelY y-coordinate}, in pixels, of the entity
	 */
	public float getPixelY() {
		return pixelY;
	}

	/**
	 * Sets the {@link #pixelY y-coordinate}, in pixels, of the entity to the provided float value.
	 * 
	 * @param y
	 */
	public void setPixelY(int y) {
		this.pixelY = y;
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
	 * @return The entity's {@link #texture}
	 */
	public Texture getTexture() {
		return texture;
	}

	/**
	 * Sets the entity's {@link #texture} to the provided Texture object
	 * 
	 * @param tex
	 */
	public void setTexture(Texture tex) {
		texture = tex;
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
