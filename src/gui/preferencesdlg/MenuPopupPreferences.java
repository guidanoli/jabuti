package gui.preferencesdlg;
import java.util.Properties;

import javax.swing.*;

import gui.maindlg.MenuPopup;

public class MenuPopupPreferences implements MenuPopup {
	
	JFrame frame;
	Properties prop;

	// size parameters
	public final int DEF_H = 170;
	public final int DEF_W = 300;

	// containers
	JPanel panel;
	
	public MenuPopupPreferences(Properties prop) {
		this.prop = prop;
		frame = new JFrame("Preferences");
		panel = new PreferencesPanel(prop,frame);
		frame.setSize(DEF_W, DEF_H);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().add(panel);
	}

	public void open(JFrame parent) {
		frame.setVisible(true);
		frame.setLocationRelativeTo(parent);
	}

}
