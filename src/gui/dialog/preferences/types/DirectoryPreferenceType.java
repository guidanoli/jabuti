package gui.dialog.preferences.types;
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

import gui.dialog.preferences.PreferenceType;
import vars.Language;
import vars.properties.GlobalProperties;



public class DirectoryPreferenceType implements PreferenceType {

	protected JPanel panel;
	protected JTextField txt = new JTextField();
	protected JButton btn = new JButton();
	protected String dlg_title;
	protected String dlg_button_label;
	
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
			defaultDir = GlobalProperties.getInstance().get("path");
		File defdir = new File(defaultDir);
		JFileChooser jfc = new JFileChooser(defdir);
		jfc.setDialogTitle(dlg_title);
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = jfc.showDialog(null,dlg_button_label);
		if (returnValue == JFileChooser.APPROVE_OPTION && jfc.getSelectedFile().isDirectory())
			return jfc.getSelectedFile().getAbsolutePath();
		return defaultDir;
	}
	public void setState(String value) { txt.setText(value); }
	public String getState() { return txt.getText(); }
	public boolean validateValue(String value) { return value != null && new File(value).isDirectory(); }
	public JPanel getPanel(Language lang) {
		assert lang != null;
		btn.setText(lang.get("gui_popup_preferences_type_dir_panel_btnlabel"));
		dlg_title = lang.get("gui_popup_preferences_type_dir_dlg_title");
		dlg_button_label = lang.get("gui_popup_preferences_type_dir_dlg_btnlabel");
		return panel;
	}
}
