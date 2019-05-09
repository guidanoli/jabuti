package gui.preferencesdlg;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class DirectoryPreferenceType implements PreferenceType {

	public DirectoryPreferenceType() { }
	public boolean validateNewValue(String value) { return new File(value).isDirectory(); }
	public String openDialog(String prefvalue) {
		File defdir = FileSystemView.getFileSystemView().getHomeDirectory();
		if( prefvalue != null ) defdir = new File(prefvalue);
		JFileChooser jfc = new JFileChooser(defdir);
		jfc.setDialogTitle("Open a directory");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = jfc.showDialog(null,"Open");
		if (returnValue == JFileChooser.APPROVE_OPTION && jfc.getSelectedFile().isDirectory())
			return jfc.getSelectedFile().getAbsolutePath();
		return prefvalue;
	}

}
