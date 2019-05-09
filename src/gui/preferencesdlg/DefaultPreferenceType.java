package gui.preferencesdlg;

public class DefaultPreferenceType implements PreferenceType {

	public DefaultPreferenceType() { }
	public boolean validateNewValue(String value) { return true; }
	public Object openDialog(Object prefvalue) { return prefvalue; }

}
