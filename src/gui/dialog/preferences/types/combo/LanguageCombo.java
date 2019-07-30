package gui.dialog.preferences.types.combo;

import vars.Language;

public class LanguageCombo implements ComboPreferenceTypeListener {

	private static String [] languages;
	
	public LanguageCombo() {
		languages = Language.getLanguages();
	}
	
	public String[] getOptionToolTips(Language lang) { return languages; } // placeholder -- will not be used
	public String[] getOptionLabels() { return languages; }
	public String formatLabel(String option, String tooltip) { return option; }

}
