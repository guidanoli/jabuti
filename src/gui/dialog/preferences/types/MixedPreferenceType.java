package gui.dialog.preferences.types;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import gui.dialog.preferences.PreferenceType;
import gui.dialog.preferences.types.mixed.MixedPreferenceTypeListener;
import gui.dialog.preferences.types.mixed.MixedPreferenceTypeListener.Orientation;
import vars.Language;

/**
 * 
 * A mixed preference type mixes difference Preference Type into one.
 * The so called sub panels are arranged one after the other either
 * horizontally or vertically.
 * 
 * The preference type state is delegated to the listener that is
 * passed by the constructor.
 * 
 * @author guidanoli
 *
 */
public class MixedPreferenceType implements PreferenceType {
	
	private MixedPreferenceTypeListener listener;
	private JPanel panel;
	
	public MixedPreferenceType(MixedPreferenceTypeListener listener) {
		this.listener = listener;
		int orientationId = listener.getOrientation() == Orientation.HORIZONTAL ?
				BoxLayout.X_AXIS : BoxLayout.Y_AXIS;
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, orientationId));
		Language lang = Language.getInstance();
		for(PreferenceType subPanel : listener) panel.add(subPanel.getPanel(lang));
	}

	public JPanel getPanel(Language lang) { return panel; }

	public void setState(String value) {
		for(PreferenceType subPanel : listener) {
			String subState = listener.getSubPanelString(value, subPanel);
			subPanel.setState(subState);
		}
	}

	public String getState() {
		return listener.getPanelState();
	}

	public boolean validateValue(String value) {
		/* Validate global scope */
		boolean globalScope = listener.validatePanelString(value);
		if(!globalScope) return false;
		/* Validate individual sub panels */
		for(PreferenceType subPanel : listener) {
			String subState = listener.getSubPanelString(value, subPanel);
			if(subState == null || !subPanel.validateValue(subState)) return false;
		}
		/* Validate */
		return true;
	}
	
}
