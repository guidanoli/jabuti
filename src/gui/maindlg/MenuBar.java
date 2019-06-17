package gui.maindlg;
import java.awt.event.KeyEvent;

import javax.swing.*;

import gui.popup.AboutPopup;
import gui.popup.NewBranchPopup;
import gui.popup.log.LogPopup;
import gui.popup.preferences.PreferencesPopup;
import vars.Language;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private Language lang = Language.getInstance();
	
	private JMenu branches_menu = new JMenu(lang.get("gui_menubar_branch_menu"));
	private JMenu edit_menu = new JMenu(lang.get("gui_menubar_edit_menu"));
	private JMenu about_menu = new JMenu(lang.get("gui_menubar_about_menu"));
	
	private JMenuItem new_branch_item = new JMenuItem(lang.get("gui_menubar_branch_new"));
	private JMenuItem show_log_item = new JMenuItem(lang.get("gui_menubar_branch_log"));
	private JMenuItem update_item = new JMenuItem(lang.get("gui_menubar_branch_update"));
	private JMenuItem setup_all_item = new JMenuItem(lang.get("gui_menubar_edit_setup_all"));
	private JMenuItem make_all_item = new JMenuItem(lang.get("gui_menubar_edit_make_all"));
	private JMenuItem setup_none_item = new JMenuItem(lang.get("gui_menubar_edit_setup_none"));
	private JMenuItem make_none_item = new JMenuItem(lang.get("gui_menubar_edit_make_none"));
	private JMenuItem pref_item = new JMenuItem(lang.get("gui_menubar_edit_preferences"));
	private JMenuItem about_item = new JMenuItem(lang.get("name"));
	
	private JMenuItem [] items = {
		branches_menu,
		edit_menu,
		about_menu,
		new_branch_item,
		update_item,
		pref_item,
		setup_all_item,
		make_all_item,
		setup_none_item,
		make_none_item,
		about_item,
		show_log_item,
	};	
	
	public MenuBar(MainFrame parent) {
				
		/* ****************
		 * ACTION LISTENERS
		 * **************** */
		new_branch_item.addActionListener(
			new MenuItemListener(
				parent,
				new NewBranchPopup()
			)
		);
		update_item.addActionListener(
			new MenuItemListener(
				new MenuAction() {
					public void action() {
						parent.panel.updateTable();
					}
				}
			)
		);
		setup_all_item.addActionListener(
			new MenuItemListener(
				new MenuAction() {
					public void action() {
						setupAllBranches(parent.panel);
					}
				}
			)
		);
		make_all_item.addActionListener(
			new MenuItemListener(
				new MenuAction() {
					public void action() {
						makeAllBranches(parent.panel);
					}
				}
			)
		);
		setup_none_item.addActionListener(
			new MenuItemListener(
				new MenuAction() {
					public void action() {
						setupNoneBranches(parent.panel);
					}
				}
			)
		);
		make_none_item.addActionListener(
			new MenuItemListener(
				new MenuAction() {
					public void action() {
						makeNoneBranches(parent.panel);
					}
				}
			)
		);
		pref_item.addActionListener(
			new MenuItemListener(
				parent,
				new PreferencesPopup(parent)
			)
		);
		about_item.addActionListener(
			new MenuItemListener(
				parent,
				new AboutPopup(parent)
			)
		);
		show_log_item.addActionListener(
			new MenuItemListener(
				parent,
				new LogPopup()
			)
		);
		
		/* *****************************
		 * ADDING COMPONENTS TO JMENUBAR
		 * ***************************** */
		branches_menu.add(new_branch_item);
		branches_menu.add(show_log_item);
		branches_menu.addSeparator();
		branches_menu.add(update_item);
		edit_menu.add(setup_all_item);
		edit_menu.add(setup_none_item);
		edit_menu.add(make_all_item);
		edit_menu.add(make_none_item);
		edit_menu.addSeparator();
		edit_menu.add(pref_item);
		about_menu.add(about_item);
		add(branches_menu);
		add(edit_menu);
		add(about_menu);
		
		// WIP FUNCTIONALITIES
		new_branch_item.setEnabled(false);
		
		/* *****************
		 * ADDING MNEUMONICS
		 * ***************** */
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
	
	private void setupAllBranches(MainPanel panel) {
		panel.setValueToAllBranches(BranchTableModel.SETUP,true);
	}
	
	private void makeAllBranches(MainPanel panel) {
		panel.setValueToAllBranches(BranchTableModel.MAKE,true);
	}
	
	private void setupNoneBranches(MainPanel panel) {
		panel.setValueToAllBranches(BranchTableModel.SETUP,false);
	}
	
	private void makeNoneBranches(MainPanel panel) {
		panel.setValueToAllBranches(BranchTableModel.MAKE,false);
	}
	
}
