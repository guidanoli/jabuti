package gui.dialog.preferences.types.combo;

import vars.Language;

/**
 * <p>Whenever the combo preference type panel is going to be displayed,
 * it has to be populated with the appropriate data (options + tips).
 * <p>This listener was created in order not to depend on the Language
 * class for constructing a ComboPreferenceType object.
 *  
 * @author guidanoli
 *
 */
public interface ComboPreferenceTypeListener {
	
	/**
	 * Get option tool tips (description of each option)
	 * @param lang - Language object
	 * @return array of tool tips or {@code null} if no
	 * tool tip will be presented. If one of the tool
	 * tips is {@code null} (but the array isn't), then there
	 * will not be a tool tip for that option in specific.
	 */
	public String [] getOptionToolTips(Language lang);
	
	/**
	 * Get option labels (strings that will be set
	 * as properties)
	 * @param lang - Language object
	 * @return array of options. The return and any of
	 * its values cannot be {@code null}!
	 */
	public String [] getOptionLabels(Language lang);
		
	/**
	 * Format label as combo box items
	 * @param option - preference option
	 * @param tooltip - option tool tip
	 * @return resulting label
	 */
	public String formatLabel(String option, String tooltip);
	
}
