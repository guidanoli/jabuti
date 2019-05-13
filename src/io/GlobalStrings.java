package io;

public class GlobalStrings {

	// Metadata
	public final static String version = "1.0";
	public final static String name = "SetMeUp";
	public final static String[] authors = {"Guilherme Dantas"};
	public final static String repository = "https://github.com/guidanoli/setmeup";
	
	// Error
	public static final String gui_error_msg_title = "Error";
	public static final String gui_error_log_title = "Error log";
	public static final String gui_error_general_msg = "An unhandled error occurred.";
	
	// Main Frame
	public static final String gui_mainframe_gp_error = "Could not manage preferences";
	
	// Main Panel
	public static final String gui_mainpanel_btnlabel_launch = "Launch";
	public static final String gui_mainpanel_btnlabel_close = "Close";
	
	// Branch Table
	public static final String gui_branchtable_columns_branch = "Branch name";
	public static final String gui_branchtable_columns_lastsetup = "Last Setup";
	public static final String gui_branchtable_columns_setup = "Setup";
	public static final String gui_branchtable_columns_make = "Make";
	
	// Menu bar
	public static final String gui_menubar_branch_menu = "Branches";
	public static final String gui_menubar_branch_new = "New Branch...";
	public static final String gui_menubar_branch_update = "Update";
	public static final String gui_menubar_edit_menu = "Edit";
	public static final String gui_menubar_edit_preferences = "Preferences...";
	public static final String gui_menubar_about_menu = "About";
	public static final String gui_menubar_about_about = "About SetMeUp";
	
	// Preferences pop-up
	public static final String gui_popup_preferences_type_dir_panel_btnlabel = "Open...";
	public static final String gui_popup_preferences_type_dir_dlg_title = "Open a directory";
	public static final String gui_popup_preferences_type_dir_dlg_btnlabel = "Open";
	
	public static final String gui_popup_preferences_proplabel_path = "Branches directory";
	
	public static final String gui_popup_preferences_btn_apply = "Apply";
	public static final String gui_popup_preferences_btn_restore = "Restore Default";
	public static final String gui_popup_preferences_btn_cancel = "Cancel";
	
	public static final String gui_popup_preferences_applymsg_ok = "Preference successfully saved.";
	public static final String gui_popup_preferences_applymsg_error = "Could not save preference properly.";
	
	public static String getFullName() { return String.format("%s %s", name,version); }
	public static String getAuthors() { return String.join(", ", authors); }
	
}
