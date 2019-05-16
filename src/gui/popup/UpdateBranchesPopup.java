package gui.popup;
import javax.swing.JFrame;
import gui.maindlg.MenuPopup;
import vars.GlobalProperties;

public class UpdateBranchesPopup implements MenuPopup {

	GlobalProperties gp;
	
	// TODO Update dialog
	
	public UpdateBranchesPopup(GlobalProperties gp) { this.gp = gp; }

	@Override
	public void open(JFrame parent) {
	}

}
