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
	AIR(true, null, '0'),
	
	/**
	 * The type for a stone tile, which cannot be passed through or broken
	 */
	STONE(false, TextureMonkey.loadTexture("JPG", "files" + File.separator + "stone.png"), '1'),
	
	/**
	 * The type for a brick tile, which cannot be passed through but will be able to be broken in the future
	 */
	BRICK(false, TextureMonkey.loadTexture("JPG", "files" + File.separator + "stone.png"), '2'),
	
	/**
	 * The type for an ice tile, which cannot be passed through and in the future will slow the player's movement
	 */
	ICE(false, TextureMonkey.loadTexture("JPG", "files" + File.separator + "stone.png"), '3'),
	
	/**
	 * The type for a spike tile, which can be passed through but kills the player immediately upon contact
	 */
	SPIKE(true, null, '4'), // TODO: find spike image
	
	/**
	 * The type for a portal tile, which can be passed through and teleports the player immediately upon contact
	 */
	PORTAL(true, TextureMonkey.loadTexture("JPG", "files" + File.separator + "stone.png"), '5'),
	
	/**
	 * The type for a key tile, which can be passed through and is collected by the player (and is thus removed
	 * from the level) immediately upon contact.
	 */
	KEY(true, TextureMonkey.loadTexture("JPG", "files" + File.separator + "stone.png"), '6'),
	
	/**
	 * The type for a potion tile, which can be passed through and is used on the player (and is thus removed
	 * from the level) immediateliy upon contact.
	 */
	POTION(true, TextureMonkey.loadTexture("JPG", "files" + File.separator + "stone.png"), '7'),
	
	/**
	 * The type for a present tile, which can be passed through and is added to the player's inventory (and is thus
	 * removed from the level) immediately upon contact.
	 */
	PRESENT(true, TextureMonkey.loadTexture("JPG", "files" + File.separator + "stone.png"), '8');

	/**
	 * Whether or not the object can be passed through
	 */
	final boolean penetrable;

	/**
	 * The texture of the object's tile
	 */
	final Texture texture;

	/**
	 * The character that represents this type of object in a level data file
	 */
	final char dataChar;

	/**
	 * @param penetrable
	 */
	ObjectType(boolean penetrable, Texture texture, char dataChar) {
		this.penetrable = penetrable;
		this.texture = texture;
		this.dataChar = dataChar;
	}

	/**
	 * @param dataChar
	 * @return The ObjectType that corresponds with the provided {@link #dataChar}
	 */
	public static ObjectType getTypeFromDataChar(char dataChar) {
		switch (dataChar) {
		default:
		case '0':
			return AIR;
		case '1':
			return STONE;
		case '2':
			return BRICK;
		case '3':
			return ICE;
		case '4':
			return SPIKE;
		case '5':
			return PORTAL;
		case '6':
			return KEY;
		case '7':
			return POTION;
		case '8':
			return PRESENT;
		}
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

	/**
	 * @return The character that represents this type of object in a level data file
	 */
	public char getDataChar() {
		return dataChar;
	}
}
