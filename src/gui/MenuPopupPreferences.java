package gui;
import javax.swing.*;

public class MenuPopupPreferences implements MenuPopup {

	public MenuPopupPreferences() { }

	public void open(JFrame parent) {
		JOptionPane.showMessageDialog(parent, "(WIP)", "Preferences", JOptionPane.PLAIN_MESSAGE);
	}

}
