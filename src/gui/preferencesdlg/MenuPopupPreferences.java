package gui.preferencesdlg;
import javax.swing.*;
import gui.maindlg.MenuPopup;
import io.GlobalProperties;

public class MenuPopupPreferences implements MenuPopup {
	
	JFrame frame;
	GlobalProperties gp;

	// size parameters
	public final int DEF_H = 170;
	public final int DEF_W = 300;

	// containers
	PreferencesPanel panel;
	
	public MenuPopupPreferences(GlobalProperties gp) {
		this.gp = gp;
		frame = new JFrame("Preferences");
		panel = new PreferencesPanel(gp,frame);
		frame.setSize(DEF_W, DEF_H);
		frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		frame.getContentPane().add(panel);
	}

	public void open(JFrame parent) {
		panel.updateTextBox();
		frame.setVisible(true);
		frame.setLocationRelativeTo(parent);
	}

}
