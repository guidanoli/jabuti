package gui.dialog.preferences;
import javax.swing.*;

import gui.defaults.DefaultPopup;
import gui.dialog.MenuPopup;
import gui.dialog.main.MainFrame;
import vars.Language;

public class PreferencesPopup implements MenuPopup {
	
	JDialog dlg;
	MainFrame parent;
	private Language lang = Language.getInstance(); 

	// containers
	PreferencesPanel panel;
	
	public void open(JFrame parent) {
		dlg = new DefaultPopup(parent,lang.get("gui_popup_preferences_title"));
		panel = new PreferencesPanel(dlg,(MainFrame) parent);
		dlg.getContentPane().add(panel);
		panel.updateTextBox();
		dlg.setResizable(false);
		dlg.setVisible(true);
	}

}
