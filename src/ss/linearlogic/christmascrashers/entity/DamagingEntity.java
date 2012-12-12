package ss.linearlogic.christmascrashers.entity;

import ss.linearlogic.christmascrashers.engine.Sprite;

/**
 * {@link Entity} subclass that represents and in-game entity capable of dealing damage to other entities.
 * 
 * @author LinearLogic
 * @since 0.3.3
 */
public abstract class DamagingEntity extends Entity {

	/**
	 * The amount of damage this entity deals to other entities
	 */
	private int damage;

	/**
	 * True if the entity is destroyed after dealing damage to an entity or colliding with an object, false if the
	 * entity can repeatedly deal damage. Projectiles expire on collision while most living entities do not.
	 */
	private boolean expiresOnCollision;

	/**
	 * Constructor - calls the {@link Entity} superclass {@link Entity#Entity(Sprite, boolean, boolean) constructor} and sets
	 * the entity's {@link #damage} and {@link #expiresOnCollision()} attributes to the specified values.
	 * 
	 * @param sprite
	 * @param damage
	 * @param expiresOnCollision
	 * @param canPenetrateObjects
	 * @param canFly
	 */
	public DamagingEntity(Sprite sprite, int damage, boolean expiresOnCollision, boolean canPenetrateObjects, boolean canFly) {
		super(sprite, canPenetrateObjects, canFly);
		this.damage = damage;
		this.expiresOnCollision = expiresOnCollision;
	}

	/**
	 * @return The entity's {@link #damage}
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * This method is caled when the entity expires, and handles its despawning.
	 */
	public abstract void handleDeath();

	/**
	 * Sets the entity's {@link #damage} to the specified integer value
	 * 
	 * @param damage
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * @return Whether the entity {@link #expiresOnCollision}
	 */
	public boolean expiresOnCollision() {
		return expiresOnCollision;
	}
}
