package gui.preferences.types;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import gui.preferences.PreferenceType;

public class ComboPreferenceType implements PreferenceType {

	private JPanel panel = new JPanel(new GridLayout(1,1));
	private JComboBox<String> combo;
	private String [] prefValues = null;
	
	public ComboPreferenceType(String [] options) {
		combo = new JComboBox<String>(options);
		prefValues = options;
		panel.add(combo);
	}
	
	public ComboPreferenceType(String [] options, String [] tips, boolean hideValue) {
		String [] labels = tips;
		if( !hideValue )
		{
			labels = new String[options.length];
			for(int i = 0 ; i < labels.length; i++)
			{
				labels[i] = String.format("%s (%s)", tips[i], options[i]);
			}
		}
		combo = new JComboBox<String>(labels);
		prefValues = options;
		panel.add(combo);
	}
	
	public JPanel getPanel() { return panel; }
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
