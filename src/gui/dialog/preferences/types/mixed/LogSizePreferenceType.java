package gui.dialog.preferences.types.mixed;

import java.util.ArrayList;
import java.util.Iterator;

import gui.dialog.preferences.PreferenceType;
import gui.dialog.preferences.types.DefaultPreferenceType;
import gui.dialog.preferences.types.MixedPreferenceType;

public class LogSizePreferenceType implements MixedPreferenceTypeListener {

	private PreferenceType logSizePreference;
	private PreferenceType percentagePreference;
	private ArrayList<PreferenceType> subPanels;
		
	public LogSizePreferenceType(long maxLogSize) {
		logSizePreference = new MixedPreferenceType(new FileSizePreferenceType(maxLogSize));
		percentagePreference = new DefaultPreferenceType();
		subPanels = new ArrayList<PreferenceType>();
		subPanels.add(logSizePreference);
		subPanels.add(percentagePreference);
	}

	public Iterator<PreferenceType> iterator() { return subPanels.iterator(); }

	public String getPanelState() {
		return String.join("/", logSizePreference.getState(), percentagePreference.getState()); 
	}

	public boolean validatePanelString(String state) {
		String [] parts = state.split("/");
		return parts.length == 2;
	}

	public String getSubPanelString(String state, PreferenceType subPanel) {
		String [] parts = state.split("/");
		if( subPanel == logSizePreference ) return parts[0];
		else return parts[1];
	}

	public Orientation getOrientation() { return Orientation.VERTICAL; }

}
