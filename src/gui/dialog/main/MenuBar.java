package gui.dialog.main;
import java.awt.event.KeyEvent;

import javax.swing.*;

import gui.dialog.about.AboutPopup;
import gui.dialog.branch.NewBranchPopup;
import gui.dialog.log.LogPopup;
import gui.dialog.preferences.PreferencesPopup;
import vars.Language;

public class MenuBar extends JMenuBar {

	private static final long serialVersionUID = -2627301045318942787L;

	private Language lang = Language.getInstance();
	
	private JMenu branchesMenu = new JMenu(lang.get("gui_menubar_branch_menu"));
	private JMenu editMenu = new JMenu(lang.get("gui_menubar_edit_menu"));
	private JMenu setupMenu = new JMenu(lang.get("gui_menubar_setup_menu"));
	private JMenu makeMenu = new JMenu(lang.get("gui_menubar_make_menu"));
	private JMenu aboutMenu = new JMenu(lang.get("gui_menubar_about_menu"));
	
	private JMenuItem newBranchItem = new JMenuItem(lang.get("gui_menubar_branch_new"));
	private JMenuItem showLogItem = new JMenuItem(lang.get("gui_menubar_branch_log"));
	private JMenuItem updateItem = new JMenuItem(lang.get("gui_menubar_branch_update"));
	private JMenuItem setupAllItem = new JMenuItem(lang.get("gui_menubar_setup_all"));
	private JMenuItem setupNoneItem = new JMenuItem(lang.get("gui_menubar_setup_none"));
	private JMenuItem makeAllItem = new JMenuItem(lang.get("gui_menubar_make_all"));
	private JMenuItem makeNoneItem = new JMenuItem(lang.get("gui_menubar_make_none"));
	private JMenuItem prefItem = new JMenuItem(lang.get("gui_menubar_edit_preferences"));
	private JMenuItem aboutItem = new JMenuItem(lang.get("name"));
	
	private JMenuItem SEPARATOR;
	
	private JMenuItem [] allItemsArray = {
			branchesMenu,
			setupMenu,
			makeMenu,
			editMenu,
			aboutMenu,
			newBranchItem,
			updateItem,
			prefItem,
			setupAllItem,
			makeAllItem,
			setupNoneItem,
			makeNoneItem,
			aboutItem,
			showLogItem,
	};
	
	private JMenu [] menusArray = {
			branchesMenu,
			editMenu,
			setupMenu,
			makeMenu,
			aboutMenu,
	};
	
	private JMenuItem [][] itemsArray = {
			{
				// BRANCHES MENU
				newBranchItem,
				showLogItem,
				SEPARATOR,
				updateItem,
			},
			{
				// EDIT MENU
				prefItem,
			},
			{
				// SETUP MENU
				setupAllItem,
				setupNoneItem,
			},
			{
				// MAKE MENU
				makeAllItem,
				makeNoneItem,
			},
			{
				// ABOUT MENU
				aboutItem,
			},
	};
	
	public MenuBar(MainFrame parent) {
				
		/* ****************
		 * ACTION LISTENERS
		 * **************** */
		newBranchItem.addActionListener(
			new MenuItemListener(
				parent,
				new NewBranchPopup()
			)
		);
		updateItem.addActionListener(
			new MenuItemListener(
				new MenuAction() {
					public void action() {
						parent.panel.updateTable();
					}
				}
			)
		);
		setupAllItem.addActionListener(
			new MenuItemListener(
				new MenuAction() {
					public void action() {
						setupAllBranches(parent.panel);
					}
				}
			)
		);
		makeAllItem.addActionListener(
			new MenuItemListener(
				new MenuAction() {
					public void action() {
						makeAllBranches(parent.panel);
					}
				}
			)
		);
		setupNoneItem.addActionListener(
			new MenuItemListener(
				new MenuAction() {
					public void action() {
						setupNoneBranches(parent.panel);
					}
				}
			)
		);
		makeNoneItem.addActionListener(
			new MenuItemListener(
				new MenuAction() {
					public void action() {
						makeNoneBranches(parent.panel);
					}
				}
			)
		);
		prefItem.addActionListener(
			new MenuItemListener(
				parent,
				new PreferencesPopup()
			)
		);
		aboutItem.addActionListener(
			new MenuItemListener(
				parent,
				new AboutPopup(parent)
			)
		);
		showLogItem.addActionListener(
			new MenuItemListener(
				parent,
				new LogPopup()
			)
		);
		
		/* *****************************
		 * ADDING COMPONENTS TO JMENUBAR
		 * ***************************** */
		addItemsToBar();
				
		/* *****************
		 * ADDING MNEUMONICS
		 * ***************** */
		for( JMenuItem item : allItemsArray ) setCustomMneumonic(item);
	}
	
	/**
	 * Adds all the menus {@link #menusArray} to the menu bar, plus
	 * all its children to it, from {@link #itemsArray}.
	 * If a {@link #SEPARATOR} is found, a separator is added. 
	 */
	private void addItemsToBar() {
		for( int i = 0 ; i < menusArray.length; i++ )
		{
			JMenu menu = menusArray[i];
			for( JMenuItem menuItem : itemsArray[i] )
			{
				if( menuItem == SEPARATOR ) menu.addSeparator();
				else menu.add(menuItem);
			}
			add(menu);
		}
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
