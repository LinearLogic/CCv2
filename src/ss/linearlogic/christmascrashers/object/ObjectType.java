package ss.linearlogic.christmascrashers.object;

import java.io.File;

import org.newdawn.slick.opengl.Texture;

import ss.linearlogic.christmascrashers.util.TextureMonkey;

/**
 * An {@link Object}'s type is its material (air, brick, spike, potion, etc.) and contains two of the object's attributes:
 * whether or not the object can be passed through and the object's function (such as restoring a player's health), if any.
 * @author LinearLogic
 * @since 0.2.5
 */
public enum ObjectType {

	/**
	 * The type for an air tile, which can be passed through and has no texture to render over the background
	 */
	AIR(true, null),
	
	/**
	 * The type for a stone tile, which cannot be passed through or broken
	 */
	STONE(false, TextureMonkey.loadTexture("JPG", "files" + File.separator + "stone.png")),
	
	/**
	 * The type for a brick tile, which cannot be passed through but will be able to be broken in the future
	 */
	BRICK(false, TextureMonkey.loadTexture("JPG", "files" + File.separator + "stone.png")),
	
	/**
	 * The type for an ice tile, which cannot be passed through and in the future will slow the player's movement
	 */
	ICE(false, TextureMonkey.loadTexture("JPG", "files" + File.separator + "stone.png")),
	
	/**
	 * The type for a spike tile, which can be passed through but kills the player immediately upon contact
	 */
	SPIKE(true, null), // TODO: find spike image
	
	/**
	 * The type for a portal tile, which can be passed through and teleports the player immediately upon contact
	 */
	PORTAL(true, TextureMonkey.loadTexture("JPG", "files" + File.separator + "stone.png")),
	
	/**
	 * The type for a key tile, which can be passed through and is collected by the player (and is thus removed
	 * from the level) immediately upon contact.
	 */
	KEY(true, TextureMonkey.loadTexture("JPG", "files" + File.separator + "stone.png")),
	
	/**
	 * The type for a potion tile, which can be passed through and is used on the player (and is thus removed
	 * from the level) immediateliy upon contact.
	 */
	POTION(true, TextureMonkey.loadTexture("JPG", "files" + File.separator + "stone.png")),
	
	/**
	 * The type for a present tile, which can be passed through and is added to the player's inventory (and is thus
	 * removed from the level) immediately upon contact.
	 */
	PRESETN(true, TextureMonkey.loadTexture("JPG", "files" + File.separator + "stone.png"));

	/**
	 * Whether or not the object can be passed through
	 */
	final boolean penetrable;

	/**
	 * The texture of the object's tile
	 */
	final Texture texture;
	/**
	 * @param penetrable
	 */
	ObjectType(boolean penetrable, Texture texture) {
		this.penetrable = penetrable;
		this.texture = texture;
	}

	/**
	 * @return Whether or not the object can be passed through
	 */
	public boolean isPenetrable() {
		return penetrable;
	}

	/**
	 * @return The texture of the object's tile
	 */
	public Texture getTexture() {
		return texture;
	}
}
