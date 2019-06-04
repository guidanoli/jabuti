package gui.popup.preferences;
import javax.swing.*;

import gui.maindlg.MainFrame;
import gui.maindlg.MenuPopup;
import vars.Language;

public class PreferencesPopup implements MenuPopup {
	
	JFrame frame;
	MainFrame parent;
	private Language lang = Language.getInstance(); 

	// containers
	PreferencesPanel panel;
	
	public PreferencesPopup(MainFrame parent) {
		this.parent = parent;
		frame = new JFrame(lang.get("gui_popup_preferences_title"));
		panel = new PreferencesPanel(frame,parent);
		frame.getContentPane().add(panel);
		gui.DefaultFrame.set(frame);
	}

	public void open(JFrame parent) {
		panel.updateTextBox();
		frame.setVisible(true);
		frame.setLocationRelativeTo(parent);
	}

}
