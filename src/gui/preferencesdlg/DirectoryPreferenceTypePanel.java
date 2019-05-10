package gui.preferencesdlg;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

public class DirectoryPreferenceTypePanel implements PreferenceTypePanel {

	protected JPanel panel;
	protected JTextField txt = new JTextField();
	protected JButton btn = new JButton("Open...");
	
	public DirectoryPreferenceTypePanel() {
		GridLayout layout = new GridLayout(1,2);
		panel = new JPanel(layout);
		btn.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(e.getActionCommand());
			}
		});
		panel.add(txt);
		panel.add(btn);
		System.out.println("Panel constructed!");
	}
	public JPanel getPanel() { return panel; }
	public String getPanelName() { return "dir"; }
	public void setState(String value) { txt.setText(value); }
	public String getValue() { return txt.getText(); }
	protected String openDialog(String prefvalue) {
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
