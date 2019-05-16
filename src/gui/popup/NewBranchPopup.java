package gui.popup;

import javax.swing.JFrame;

import gui.maindlg.MenuPopup;
import vars.GlobalProperties;

public class NewBranchPopup implements MenuPopup {

	//TODO: New Branch dialog
	
	GlobalProperties gp;
	public NewBranchPopup(GlobalProperties gp) {
		this.gp = gp;
	}

	@Override
	public void open(JFrame parent) {
	}

}
