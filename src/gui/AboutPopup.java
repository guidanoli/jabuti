package gui;
import javax.swing.*;

public class AboutPopup implements MenuPopup {
	
	public AboutPopup() { }

	public void open(JFrame parent) {
		JOptionPane.showMessageDialog(parent, "SetMeUp v.1.0\nGuilherme Dantas", "About SetMeUp", JOptionPane.PLAIN_MESSAGE);
	}

}
