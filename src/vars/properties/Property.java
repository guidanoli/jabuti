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
 * </ul>
 * @author guidanoli
 *
 */
public class Property {

	private String key;
	private String defaultValue;
	

	public Property(String key, String defaultValue) {
		setKey(key);
		setDefaultValue(defaultValue);
	}
	
	/* Getters */
	
	public String getKey() {
		return key;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
	
	/* Setters */
	
	private void setKey(String key) {
		this.key = key;
	}

	private void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
