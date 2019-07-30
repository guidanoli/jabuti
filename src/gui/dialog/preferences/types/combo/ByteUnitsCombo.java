package gui.dialog.preferences.types.combo;

import vars.Language;

public class ByteUnitsCombo implements ComboPreferenceTypeListener {

	String [] toolTips = {"B", "KB", "MB", "GB"};
	String [] sizeMultiplier = {"1", "1024", "1048576", "1073741824"};
	
	public String[] getOptionToolTips(Language lang) { return toolTips; }
	public String[] getOptionLabels() { return sizeMultiplier; }
	public String formatLabel(String option, String tooltip) { return tooltip; }

}
