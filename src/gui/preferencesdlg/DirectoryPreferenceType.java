package gui.preferencesdlg;
import java.io.File;

public class DirectoryPreferenceType implements PreferenceType {

	protected PreferenceTypePanel panel = new DirectoryPreferenceTypePanel();
	public boolean validateState() { return new File(panel.getValue()).isDirectory(); }
	public PreferenceTypePanel getPanel() { return panel; }

}
