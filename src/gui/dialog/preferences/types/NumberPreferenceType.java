package gui.dialog.preferences.types;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import gui.dialog.preferences.PreferenceType;
import vars.Language;

public class NumberPreferenceType implements PreferenceType {

	protected JPanel panel = new JPanel(new GridLayout(1,1));
	protected JSpinner spinner;
	public NumberPreferenceType(int min, int max) {
		spinner = new JSpinner(new SpinnerNumberModel(1,min,max,1));
		panel.add(spinner);
	}
	public boolean validateState() { return getState() != null; }
	public JPanel getPanel(Language lang) { return panel; }
	public void setState(String value) { spinner.setValue(Integer.parseInt(value)); }
	public String getState() { return Integer.toString((int) spinner.getValue()); }

}
