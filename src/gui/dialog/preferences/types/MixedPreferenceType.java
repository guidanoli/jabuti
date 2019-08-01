package gui.dialog.preferences.types;

import java.awt.Component;
import java.awt.Dimension;
import java.util.function.Supplier;

import javax.swing.Box;
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
		boolean h = listener.getOrientation() == Orientation.HORIZONTAL;
		int orientationId = h ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS;
		Supplier<Component> gapSupplier = h ? () -> Box.createRigidArea(new Dimension(5, 0))
											: () -> Box.createRigidArea(new Dimension(0, 5));
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, orientationId));
		Language lang = Language.getInstance();
		boolean first = true;
		for(PreferenceType subPanel : listener) {
			if(first) first = false;
			else panel.add(gapSupplier.get());
			panel.add(subPanel.getPanel(lang));
		}
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
