package gui.maindlg;
import java.awt.event.*;

public class MenuItemListener implements ActionListener {

	private MainFrame parent;
	MenuPopup popup;
	
	public MenuItemListener(MainFrame parent, MenuPopup popup) {
		this.parent = parent;
		this.popup = popup;
	}

	public void actionPerformed(ActionEvent arg0) {
		popup.open(parent);
	}

}
