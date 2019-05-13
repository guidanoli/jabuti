package gui.maindlg;
import java.awt.event.*;
import javax.swing.*;

import gui.popup.AboutPopup;
import gui.popup.NewBranchPopup;
import gui.popup.UpdateBranchesPopup;
import gui.popup.preferences.PreferencesPopup;
import io.GlobalProperties;
import io.GlobalStrings;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private MainFrame parent; 
	private JMenu branches_menu = new JMenu(GlobalStrings.gui_menubar_branch_menu);
	private JMenu edit_menu = new JMenu(GlobalStrings.gui_menubar_edit_menu);
	private JMenu about_menu = new JMenu(GlobalStrings.gui_menubar_about_menu);
	private JMenuItem new_branch_item = new JMenuItem(GlobalStrings.gui_menubar_branch_new,KeyEvent.VK_N);
	private JMenuItem update_item = new JMenuItem(GlobalStrings.gui_menubar_branch_update,KeyEvent.VK_U);
	private JMenuItem pref_item = new JMenuItem(GlobalStrings.gui_menubar_edit_preferences,KeyEvent.VK_P);
	private JMenuItem about_item = new JMenuItem(GlobalStrings.gui_menubar_about_about,KeyEvent.VK_S);
		
	public MenuBar(MainFrame parent, GlobalProperties gp) {
		this.parent = parent;
		new_branch_item.addActionListener(new MenuItemListener(this.parent, new NewBranchPopup(gp)));
		update_item.addActionListener(new MenuItemListener(this.parent,new UpdateBranchesPopup(gp)));
		branches_menu.add(new_branch_item);
		branches_menu.add(update_item);
		branches_menu.setMnemonic(KeyEvent.VK_B);
		add(branches_menu);
		edit_menu.setMnemonic(KeyEvent.VK_E);
		pref_item.addActionListener(new MenuItemListener(this.parent,new PreferencesPopup(gp,parent)));
		edit_menu.add(pref_item);
		add(edit_menu);
		about_menu.setMnemonic(KeyEvent.VK_A);
		about_item.addActionListener(new MenuItemListener(this.parent,new AboutPopup(parent)));
		about_menu.add(about_item);
		add(about_menu);
	}
	
}
