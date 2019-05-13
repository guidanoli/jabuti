package gui.popup.preferences;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class ComboPreferenceType implements PreferenceType {

	JPanel panel = new JPanel(new GridLayout(1,1));
	JComboBox<String> combo;
	public ComboPreferenceType(String [] options) {
		combo = new JComboBox<String>(options);
		panel.add(combo);
	}
	public JPanel getPanel() { return panel; }
	public void setState(String value) { combo.setSelectedItem(value); }
	public String getState() { return (String) combo.getSelectedItem(); }
	public boolean validateState() { return true; }

}
