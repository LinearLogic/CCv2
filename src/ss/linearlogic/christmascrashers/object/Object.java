package ss.linearlogic.christmascrashers.object;

/**
 * Represents an in-game object tile.
 * 
 * @author LinearLogic
 * @since 0.2.5
 */
public class Object implements IObject {

	/**
	 * The object's {@link ObjectType type}
	 */
	private ObjectType type;

	/**
	 * Creates an in-game object at the given location
	 * 
	 * @param type The {@link ObjectType} of the object
	 */
	public Object(/* Location location, */ ObjectType type) {
		this.type = type;
	}

	public ObjectType getType() {
		return type;
	}

	public void setType(ObjectType type) {
		this.type = type;
	}
}
