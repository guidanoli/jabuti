package gui;
import java.awt.event.*;
import javax.swing.*;

public class MenuItemListener implements ActionListener {

	private JFrame parent;
	MenuPopup popup;
	
	public MenuItemListener(JFrame parent, MenuPopup popup) {
		this.parent = parent;
		this.popup = popup;
	}

	public void actionPerformed(ActionEvent arg0) {
		popup.open(parent);
	}

}
