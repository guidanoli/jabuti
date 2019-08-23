package vars.properties;

import gui.dialog.preferences.PreferenceType;

/**
 * 
 * <p>Lets the user edit the preference under certain constraints through the {@link PreferenceType} interface. Additional
 * to the {@link Property} fields, this class also has:
 * 
 * <ul>
 * <li><b>type</b> - {@link PreferenceType}</li>
 * <li><b>reset</b> - <code>true</code> if user is advised to reset application after modifying this property value</li>
 * </ul>
 * 
 * @author guidanoli
 *
 */
public class EditableProperty extends Property {

	private PreferenceType type;
	private boolean reset;
	
	public EditableProperty(String key, String defaultValue, PreferenceType type, boolean reset) {
		super(key, defaultValue);
		setType(type);
		setReset(reset);
	}

	public PreferenceType getType() {
		return type;
	}

	public boolean isReset() {
		return reset;
	}

	private void setType(PreferenceType type) {
		this.type = type;
	}

	private void setReset(boolean reset) {
		this.reset = reset;
	}
	
	
}
