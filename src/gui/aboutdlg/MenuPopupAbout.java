package gui.aboutdlg;
import javax.swing.*;

import gui.maindlg.MenuPopup;

public class MenuPopupAbout implements MenuPopup {
	
	public MenuPopupAbout() { }

	public void open(JFrame parent) {
		// TODO: Better dialog with better format
		JOptionPane.showMessageDialog(parent, "SetMeUp v.1.0\nGuilherme Dantas", "About SetMeUp", JOptionPane.PLAIN_MESSAGE);
	}

}
