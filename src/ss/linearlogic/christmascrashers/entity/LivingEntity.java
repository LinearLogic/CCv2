package ss.linearlogic.christmascrashers.entity;

import ss.linearlogic.christmascrashers.engine.Sprite;

/**
 * {@link Entity} subclass that represents and in-game entity with health that can be damaged and killed. The Player
 * is an example of a LivingEntity. <p>Note that the LivingEntity class extends the {@link DamagingEntity} class - entities
 * that are peaceful will simply have damage values of zero.
 * 
 * @author LinearLogic
 * @since 0.3.3
 */
public abstract class LivingEntity extends DamagingEntity {

	/**
	 * The entity's current health, which is less than or equal to its {@link #maxHealth}. If the entity's health
	 * reaches zero, then the {@link #handleDeath()} method is called.
	 */
	protected int health;

	/**
	 * The entity's health capacity
	 */
	protected int maxHealth;

	/**
	 * Constructor - calls the {@link DamagingEntity} superclass {@link DamagingEntity#DamagingEntity(Sprite, int, boolean, boolean, boolean) constructor}
	 * and sets the entity's {@link #health} and {@link #maxHealth} to the provided maxHealth value.
	 * 
	 * @param sprite
	 * @param maxHealth 
	 * @param damage 
	 * @param expiresOnCollision 
	 * @param canPenetrateObjects 
	 * @param canFly 
	 */
	public LivingEntity(Sprite sprite, int maxHealth, int damage, boolean expiresOnCollision, boolean canPenetrateObjects, boolean canFly) {
		super(sprite, damage, expiresOnCollision, canPenetrateObjects, canFly);
		this.health = maxHealth;
		this.maxHealth = maxHealth;
	}

	/**
	 * This method is called when the entity's {@link #health} reaches zero, and handles either the despawning or
	 * respawning of the entity.
	 */
	public abstract void handleDeath();

	/**
	 * @return The entity's current {@link #health}
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Sets the entity's {@link #health} to the specified integer value.
	 * 
	 * @param health
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * Increments the entity's health by the specified amount (can be positive or negative)
	 * 
	 * @param amount
	 */
	public void incrementHealth(int amount) {
		health += amount;
	}

	/**
	 * @return The entity's {@link #maxHealth} value
	 */
	public int getMaxHealth() {
		return maxHealth;
	}

	/**
	 * Sets the entity's {@link #maxHealth} to the specified integer value.
	 * 
	 * @param maxHealth
	 */
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
}
