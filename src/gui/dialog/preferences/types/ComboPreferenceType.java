package gui.dialog.preferences.types;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import gui.dialog.preferences.PreferenceType;
import gui.dialog.preferences.types.combo.ComboPreferenceTypeListener;
import vars.Language;

public class ComboPreferenceType implements PreferenceType {

	private JPanel panel = new JPanel(new GridLayout(1,1));
	private JComboBox<String> combo;
	private String [] prefValues = null;
	
	ComboPreferenceTypeListener listener;
	
	public ComboPreferenceType(ComboPreferenceTypeListener listener) {
		assert listener != null;
		this.listener = listener;
	}
	
	public JPanel getPanel(Language lang) {
		String [] labels = listener.getOptionLabels(lang);
		String [] tooltips = listener.getOptionToolTips(lang);
		String [] optionStrings = new String[labels.length];
		
		for(int i = 0; i < labels.length; i++)
			optionStrings[i] = listener.formatLabel(labels[i], tooltips[i]);
		
		combo = new JComboBox<String>(optionStrings);
		prefValues = labels;
		panel.add(combo);
		return panel;
	}
	public void setState(String value) {
		for(int i = 0 ; i < prefValues.length; i++)
			if( prefValues[i].equals(value) ) combo.setSelectedIndex(i);
	}
	public String getState() {
		int index = combo.getSelectedIndex();
		return prefValues[index];
	}
	public boolean validateState() { return true; }

}
