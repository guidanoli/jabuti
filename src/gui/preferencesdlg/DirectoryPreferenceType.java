package gui.preferencesdlg;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class DirectoryPreferenceType implements PreferenceType {

	public DirectoryPreferenceType() { }
	public boolean validateNewValue(String value) { return new File(value).isDirectory(); }
	public Object openDialog(Object prefvalue) {
		if( prefvalue == null ) prefvalue = FileSystemView.getFileSystemView().getHomeDirectory();
		JFileChooser jfc = new JFileChooser(new File((String) prefvalue));
		jfc.setDialogTitle("Choose a directory");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = jfc.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (jfc.getSelectedFile().isDirectory()) {				
				return jfc.getSelectedFile().getAbsolutePath();
			}
		}
		return prefvalue;
	}

}
