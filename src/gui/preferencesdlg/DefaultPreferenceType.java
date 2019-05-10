package gui.preferencesdlg;

public class DefaultPreferenceType implements PreferenceType {

	public PreferenceTypePanel panel = getPanel();
	public boolean validateState() { return panel.getValue() != null; }
	public PreferenceTypePanel getPanel() { return panel; }

}
