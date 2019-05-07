package gui;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private JFrame parent; 
	private JMenu edit_menu = new JMenu("Edit");
	private JMenu about_menu = new JMenu("About");
	private JMenuItem pref_item = new JMenuItem("Preferences...",KeyEvent.VK_P);
	private JMenuItem about_item = new JMenuItem("About SetMeUp",KeyEvent.VK_S);
		
	public MenuBar(JFrame parent) {
		this.parent = parent;
		edit_menu.setMnemonic(KeyEvent.VK_E);
		// to do - parent pop up
		pref_item.addActionListener(new MenuItemListener(this.parent,null));
		edit_menu.add(pref_item);
		add(edit_menu);
		about_menu.setMnemonic(KeyEvent.VK_A);
		about_item.addActionListener(new MenuItemListener(this.parent,new AboutPopup()));
		about_menu.add(about_item);
		add(about_menu);
	}
	
}
