package gui.maindlg;
import java.awt.event.*;
import javax.swing.*;
import gui.aboutdlg.MenuPopupAbout;
import gui.preferencesdlg.MenuPopupPreferences;
import io.GlobalProperties;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private JFrame parent; 
	private JMenu edit_menu = new JMenu("Edit");
	private JMenu about_menu = new JMenu("About");
	private JMenuItem pref_item = new JMenuItem("Preferences...",KeyEvent.VK_P);
	private JMenuItem about_item = new JMenuItem("About SetMeUp",KeyEvent.VK_S);
		
	public MenuBar(JFrame parent, GlobalProperties gp) {
		this.parent = parent;
		edit_menu.setMnemonic(KeyEvent.VK_E);
		// to do - parent pop up
		pref_item.addActionListener(new MenuItemListener(this.parent,new MenuPopupPreferences(gp)));
		edit_menu.add(pref_item);
		add(edit_menu);
		about_menu.setMnemonic(KeyEvent.VK_A);
		about_item.addActionListener(new MenuItemListener(this.parent,new MenuPopupAbout()));
		about_menu.add(about_item);
		add(about_menu);
	}
	
}
