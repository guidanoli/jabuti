package gui;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private JMenu edit_menu = new JMenu("Edit");
	private JMenu about_menu = new JMenu("About");
		
	public MenuBar() {
		edit_menu.setMnemonic(KeyEvent.VK_E);
		edit_menu.add(new JMenuItem("Preferences...",KeyEvent.VK_P));
		add(edit_menu);
		about_menu.setMnemonic(KeyEvent.VK_A);
		about_menu.add(new JMenuItem("About SetMeUp",KeyEvent.VK_S));
		add(about_menu);
		// constructor
	}
	
}
