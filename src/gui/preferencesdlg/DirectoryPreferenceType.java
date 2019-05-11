package gui.preferencesdlg;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

public class DirectoryPreferenceType implements PreferenceType {

	protected JPanel panel;
	protected JTextField txt = new JTextField();
	protected JButton btn = new JButton("Open...");
	public boolean validateState() { return new File(getState()).isDirectory(); }
	public JPanel getPanel() { return panel; }
	public DirectoryPreferenceType() {
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints ();
		panel = new JPanel(layout);
		txt.setEditable(false);
		btn.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dir = getState();
				String new_dir = openDialog(dir);
				setState(new_dir);
			}
		});
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 2;
		c.weighty = 1;
		panel.add(txt,c);
		c.insets = new Insets(0,5,0,0);
		c.gridx = 1;
		c.weightx = 1;
		panel.add(btn,c);
		System.out.println("Panel constructed!");
	}
	public void setState(String value) { txt.setText(value); }
	public String getState() { return txt.getText(); }
	protected String openDialog(String defaultDir) {
		// TODO: get Default Directory from VIS
		File defdir = FileSystemView.getFileSystemView().getHomeDirectory();
		if( defaultDir != null ) defdir = new File(defaultDir);
		JFileChooser jfc = new JFileChooser(defdir);
		jfc.setDialogTitle("Open a directory");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = jfc.showDialog(null,"Open");
		if (returnValue == JFileChooser.APPROVE_OPTION && jfc.getSelectedFile().isDirectory())
			return jfc.getSelectedFile().getAbsolutePath();
		return defaultDir;
	}
}
