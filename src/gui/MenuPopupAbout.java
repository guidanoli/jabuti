package gui;
import javax.swing.*;

public class MenuPopupAbout implements MenuPopup {
	
	public MenuPopupAbout() { }

	public void open(JFrame parent) {
		JOptionPane.showMessageDialog(parent, "SetMeUp v.1.0\nGuilherme Dantas", "About SetMeUp", JOptionPane.PLAIN_MESSAGE);
	}

}