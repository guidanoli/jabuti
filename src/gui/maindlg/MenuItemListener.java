package gui.maindlg;
import java.awt.event.*;

public class MenuItemListener implements ActionListener {

	private MenuAction menuAction;
	
	/**
	 * Constructs a menu item listener using a menu MenuPopup
	 * implementation. The action will be will to open the
	 * pop up through the {@link MenuPopup#open(javax.swing.JFrame) open(JFrame)}
	 * method.
	 * @param parent - parent dialog
	 * @param popup - MenuPopup implementation
	 */
	public MenuItemListener(MainFrame parent, MenuPopup popup) {
		this.menuAction = new MenuAction() {
			public void action() {
				popup.open(parent);
			}
		};
	}

	/**
	 * Constructs a menu item listener using purely a MenuAction
	 * implementation.
	 * @param action - MenuAction implementation
	 */
	public MenuItemListener(MenuAction action) {
		menuAction = action;
	}
	
	/**
	 * Triggers the action determined by the constructor
	 */
	public void actionPerformed(ActionEvent arg0) {
		menuAction.action();
	}

}
