package gui.preferencesdlg;
import java.io.File;

public class DirectoryPreferenceType implements PreferenceType {

	public DirectoryPreferenceType() { }
	public boolean validateNewValue(String value) { return new File(value).isDirectory(); }

	@Override
	public void openDialog() {
		// TODO Auto-generated method stub

	}

}
