package vars.properties;

import gui.dialog.preferences.PreferenceType;

/**
 * <p>Abstraction to property. This was created so not to depend on the Language class
 * on creation. For the building of the Preferences dialog, labels that do depend on
 * the Language class will be generated through the {@link PreferenceType} listener.
 * 
 * <p>This class holds the following fields:
 * <ul>
 * <li><b>key</b> - string to access its value through {@link GlobalProperties}</li>
 * <li><b>default value</b> - value initialized and overwritten to invalid values</li>
 * <li><b>type</b> - {@link PreferenceType}</li>
 * <li><b>reset</b> - {@code true} if user is advised to reset application after modifying this property value</li>
 * </ul>
 * @author guidanoli
 *
 */
public class Property {

	private String key;
	private String defaultValue;
	private PreferenceType type;
	private boolean reset;
	
	public Property(String key, String defaultValue, PreferenceType type, boolean reset) {
		setKey(key);
		setDefaultValue(defaultValue);
		setType(type);
		setReset(reset);
	}

	/* Getters */
	
	public String getKey() {
		return key;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
	
	public PreferenceType getType() {
		return type;
	}

	public boolean isReset() {
		return reset;
	}

	/* Setters */
	
	private void setKey(String key) {
		this.key = key;
	}

	private void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	private void setType(PreferenceType type) {
		this.type = type;
	}

	private void setReset(boolean reset) {
		this.reset = reset;
	}
	
}
