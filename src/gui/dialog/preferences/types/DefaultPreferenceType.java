package gui.dialog.preferences.types;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.dialog.preferences.PreferenceType;
import vars.Language;

public class DefaultPreferenceType implements PreferenceType {

	protected JPanel panel = new JPanel(new GridLayout(1,1));
	protected JTextField txt = new JTextField();
	public DefaultPreferenceType() { panel.add(txt); }
	public boolean validateValue(String value) { return value != null; }
	public JPanel getPanel(Language lang) { return panel; }
	public void setState(String value) { txt.setText(value); }
	public String getState() { return txt.getText(); }
	
}
