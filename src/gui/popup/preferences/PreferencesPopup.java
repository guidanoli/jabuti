package gui.popup.preferences;
import javax.swing.*;

import gui.maindlg.MainFrame;
import gui.maindlg.MenuPopup;
import io.GlobalProperties;

public class PreferencesPopup implements MenuPopup {
	
	JFrame frame;
	GlobalProperties gp;
	MainFrame parent;

	// containers
	PreferencesPanel panel;
	
	public PreferencesPopup(GlobalProperties gp, MainFrame parent) {
		this.parent = parent;
		this.gp = gp;
		frame = new JFrame("Preferences");
		panel = new PreferencesPanel(gp,frame,parent);
		frame.getContentPane().add(panel);
		gui.DefaultFrame.set(frame);
	}

	public void open(JFrame parent) {
		panel.updateTextBox();
		frame.setVisible(true);
		frame.setLocationRelativeTo(parent);
	}

}
