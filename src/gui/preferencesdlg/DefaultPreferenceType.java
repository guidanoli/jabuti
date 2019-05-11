package gui.preferencesdlg;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class DefaultPreferenceType implements PreferenceType {

	protected JPanel panel = new JPanel(new GridLayout(1,1));
	protected JTextField txt = new JTextField();
	public DefaultPreferenceType() { panel.add(txt); }
	public boolean validateState() { return getState() != null; }
	public JPanel getPanel() { return panel; }
	public void setState(String value) { txt.setText(value); }
	public String getState() { return txt.getText(); }
	
}
