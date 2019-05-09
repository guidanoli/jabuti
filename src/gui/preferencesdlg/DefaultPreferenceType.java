package gui.preferencesdlg;

public class DefaultPreferenceType implements PreferenceType {

	public DefaultPreferenceType() { }
	public boolean validateNewValue(String value) { return true; }
	public String openDialog(String prefvalue) { return prefvalue; }

}
