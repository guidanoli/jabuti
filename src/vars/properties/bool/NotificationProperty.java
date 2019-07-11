package vars.properties.bool;

public class NotificationProperty extends LongBooleanProperty {

	public static enum Type {
		GENERAL(0),
		CLEANUP(1),
		SETUP(2),
		MAKE(3);
		
		private final int value;
	    private Type(int value) { this.value = value; }
	    public int getValue() { return value; }
	}
	
	public static int TYPES_COUNT = Type.values().length;
	
	public NotificationProperty() {
		super(TYPES_COUNT, true, "notify");
	}
		
}
