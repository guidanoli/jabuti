package gui.popup.preferences.types;
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

import gui.popup.preferences.PreferenceType;
import vars.GlobalProperties;
import vars.Language;



public class DirectoryPreferenceType implements PreferenceType {

	private Language lang = Language.getInstance();
	protected JPanel panel;
	protected JTextField txt = new JTextField();
	protected JButton btn = new JButton(lang.get("gui_popup_preferences_type_dir_panel_btnlabel"));
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
		c.weightx = 2;
		panel.add(txt,c);
		c.insets = new Insets(0,5,0,0);
		c.weightx = 0;
		panel.add(btn,c);
	}
	protected String openDialog(String defaultDir) {
		if( defaultDir == null )
			defaultDir = GlobalProperties.getInstance().getProperty("path");
		File defdir = new File(defaultDir);
		JFileChooser jfc = new JFileChooser(defdir);
		jfc.setDialogTitle(lang.get("gui_popup_preferences_type_dir_dlg_title"));
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = jfc.showDialog(null,lang.get("gui_popup_preferences_type_dir_dlg_btnlabel"));
		if (returnValue == JFileChooser.APPROVE_OPTION && jfc.getSelectedFile().isDirectory())
			return jfc.getSelectedFile().getAbsolutePath();
		return defaultDir;
	}
	public void setState(String value) { txt.setText(value); }
	public String getState() { return txt.getText(); }
	public boolean validateState() { return new File(getState()).isDirectory(); }
	public JPanel getPanel() { return panel; }
}
