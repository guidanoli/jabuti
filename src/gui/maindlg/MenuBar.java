package gui.maindlg;
import java.awt.event.*;
import javax.swing.*;
import gui.aboutdlg.MenuPopupAbout;
import gui.preferencesdlg.MenuPopupPreferences;
import gui.updatedlg.MenuPopupUpdate;
import io.GlobalProperties;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private MainFrame parent; 
	private JMenu pgm_menu = new JMenu("Program");
	private JMenu edit_menu = new JMenu("Edit");
	private JMenu about_menu = new JMenu("About");
	private JMenuItem update_item = new JMenuItem("Update",KeyEvent.VK_U);
	private JMenuItem pref_item = new JMenuItem("Preferences...",KeyEvent.VK_P);
	private JMenuItem about_item = new JMenuItem("About SetMeUp",KeyEvent.VK_S);
		
	public MenuBar(MainFrame parent, GlobalProperties gp) {
		this.parent = parent;
		update_item.addActionListener(new MenuItemListener(this.parent,new MenuPopupUpdate(gp)));
		pgm_menu.add(update_item);
		pgm_menu.setMnemonic(KeyEvent.VK_P);
		add(pgm_menu);
		edit_menu.setMnemonic(KeyEvent.VK_E);
		pref_item.addActionListener(new MenuItemListener(this.parent,new MenuPopupPreferences(gp,parent)));
		edit_menu.add(pref_item);
		add(edit_menu);
		about_menu.setMnemonic(KeyEvent.VK_A);
		about_item.addActionListener(new MenuItemListener(this.parent,new MenuPopupAbout()));
		about_menu.add(about_item);
		add(about_menu);
	}
	
}
