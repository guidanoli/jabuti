package gui.popup;

import javax.swing.JFrame;

import gui.maindlg.MenuPopup;
import vars.Language;

public class LogPopup implements MenuPopup {

	private Language lang = Language.getInstance();
	private JFrame frame = new JFrame(lang.get("gui_popup_log_title"));
	
	public LogPopup(JFrame parent) {
		
	}

	public void open(JFrame parent) {
		frame.setVisible(true);
	}

}
