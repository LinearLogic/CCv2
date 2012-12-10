package ss.linearlogic.christmascrashers.object;

/**
 * In-game object interface, implemented by the {@link Object} class.
 * 
 * @author LinearLogic
 * @since 0.2.5
 */
public interface IObject {

	/**
	 * The uniform length and width, in pixels, of each object's texture tile.
	 */
	public static final int TILE_SIZE = 32;

	// TODO: add location thing-a-ma-jigs (prereq: creation of the Location class)

	/**
	 * @return The {@link ObjectType type} of the object
	 */
	public ObjectType getType();

	/**
	 * Sets the type of the object to the supplied {@link ObjectType}.
	 * 
	 * @param type
	 */
	public void setType(ObjectType type);
}
