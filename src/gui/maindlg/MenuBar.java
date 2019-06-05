package gui.maindlg;
import java.awt.event.KeyEvent;

import javax.swing.*;

import gui.popup.AboutPopup;
import gui.popup.NewBranchPopup;
import gui.popup.preferences.PreferencesPopup;
import vars.Language;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private Language lang = Language.getInstance();
	private MainFrame parent; 
	private JMenu branches_menu = new JMenu(lang.get("gui_menubar_branch_menu"));
	private JMenu edit_menu = new JMenu(lang.get("gui_menubar_edit_menu"));
	private JMenu about_menu = new JMenu(lang.get("gui_menubar_about_menu"));
	private JMenuItem new_branch_item = new JMenuItem(lang.get("gui_menubar_branch_new"));
	private JMenuItem update_item = new JMenuItem(lang.get("gui_menubar_branch_update"));
	private JMenuItem pref_item = new JMenuItem(lang.get("gui_menubar_edit_preferences"));
	private JMenuItem about_item = new JMenuItem(lang.get("name"));
	private JMenuItem [] items = {branches_menu, edit_menu, about_menu, new_branch_item, update_item, pref_item, about_item};	
	
	public MenuBar(MainFrame parent) {
		this.parent = parent;
		new_branch_item.addActionListener(new MenuItemListener(this.parent, new NewBranchPopup()));
		update_item.addActionListener(
			new MenuItemListener(
				new MenuAction() {
					public void action() {
						parent.panel.updateTable();
					}
				}
			)
		);
		branches_menu.add(new_branch_item);
		branches_menu.add(update_item);
//		branches_menu.setEnabled(false); //TODO: Branches menu items functionalities
		add(branches_menu);
		pref_item.addActionListener(new MenuItemListener(this.parent,new PreferencesPopup(parent)));
		edit_menu.add(pref_item);
		add(edit_menu);
		about_item.addActionListener(new MenuItemListener(this.parent,new AboutPopup(parent)));
		about_menu.add(about_item);
		add(about_menu);
		for( JMenuItem item : items ) setCustomMneumonic(item);
	}
	
	private void setCustomMneumonic(JMenuItem menu) {
		for( char c : menu.getText().toLowerCase().toCharArray() )
		{
			menu.setMnemonic(getKeyId(c));
			if(menu.getMnemonic() != 0) return;
		}
	}
	
	private int getKeyId(char c) {
		if( c < 'a' || c > 'z' ) return 0;
		return KeyEvent.VK_A + (c - 'a');
	}
	
}
