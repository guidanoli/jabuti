package gui.updatedlg;

import javax.swing.JFrame;

import gui.maindlg.MenuPopup;
import io.GlobalProperties;

public class MenuPopupUpdate implements MenuPopup {

	GlobalProperties gp;
	
	public MenuPopupUpdate(GlobalProperties gp) { this.gp = gp; }

	@Override
	public void open(JFrame parent) {
		// TODO Update dialog
	}

}
