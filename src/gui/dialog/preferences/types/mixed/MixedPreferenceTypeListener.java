package gui.dialog.preferences.types.mixed;

import gui.dialog.preferences.PreferenceType;
import gui.dialog.preferences.types.MixedPreferenceType;

/**
 * 
 * <p>The mixed preference type listener is the middle man between the
 * {@link MixedPreferenceType} and the {@link PreferenceType}, since it
 * encodes each sub panel state to the mixed preference panel state as
 * a whole, and the other way around - gets each individual state through
 * the mixed preference panel state.
 * 
 * @author guidanoli
 *
 */
public interface MixedPreferenceTypeListener extends Iterable<PreferenceType> {

	/**
	 * 
	 * <p>Mixed preference sub panels orientation
	 * <p>Dictates in which direction will the sub panels
	 * be displayed: horizontally or vertically
	 * 
	 * @author guidanoli
	 *
	 */
	public enum Orientation {
		HORIZONTAL,
		VERTICAL;
	}
	
	/**
	 * Validates each sub panel state in order to generate the mixed preference
	 * panel state string.
	 * @return mixed preference panel state
	 */
	public String getPanelState();
	
	/**
	 * Validates mixed preference panel according to each sub panel state
	 * @return
	 */
	public boolean validatePanelState();
	
	/**
	 * Converts {@code mixed preference panel state -> sub panel state}
	 * @param state - mixed preference panel state string
	 * @param subPanel - sub panel in question
	 * @return sub panel state string
	 */
	public String getSubPanelString(String state, PreferenceType subPanel);
	
	/**
	 * @return sub panels orientation
	 */
	public Orientation getOrientation();
	
}
