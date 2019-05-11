package gui.preferencesdlg;
import javax.swing.*;

import gui.maindlg.MainFrame;
import gui.maindlg.MenuPopup;
import io.GlobalProperties;

public class MenuPopupPreferences implements MenuPopup {
	
	JFrame frame;
	GlobalProperties gp;
	MainFrame parent;

	// size parameters
	public final int DEF_H = 170;
	public final int DEF_W = 400;

	// containers
	PreferencesPanel panel;
	
	public MenuPopupPreferences(GlobalProperties gp, MainFrame parent) {
		this.parent = parent;
		this.gp = gp;
		frame = new JFrame("Preferences");
		panel = new PreferencesPanel(gp,frame,parent);
		frame.setSize(DEF_W, DEF_H);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		frame.getContentPane().add(panel);
	}

	public void open(JFrame parent) {
		panel.updateTextBox();
		frame.setVisible(true);
		frame.setLocationRelativeTo(parent);
	}

}
