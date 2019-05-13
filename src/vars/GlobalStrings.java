package vars;

public class GlobalStrings {

	// Metadata
	public final String version = "1.0";
	public final String name = "SetMeUp";
	public final String[] authors = {"Guilherme Dantas"};
	public final String repository = "https://github.com/guidanoli/setmeup";
	
	// Error
	public String gui_error_msg_title = "Error";
	public String gui_error_log_title = "Error log";
	public String gui_error_general_msg = "An unhandled error occurred.";
	
	// Main Frame
	public String gui_mainframe_gp_error = "Could not manage preferences";
	
	// Main Panel
	public String gui_mainpanel_btnlabel_launch = "Launch";
	public String gui_mainpanel_btnlabel_close = "Close";
	
	// Branch Table
	public String gui_branchtable_columns_branch = "Branch name";
	public String gui_branchtable_columns_lastsetup = "Last Setup";
	public String gui_branchtable_columns_setup = "Setup";
	public String gui_branchtable_columns_make = "Make";
	
	// Menu bar
	public String gui_menubar_branch_menu = "Branches";
	public String gui_menubar_branch_new = "New Branch...";
	public String gui_menubar_branch_update = "Update";
	public String gui_menubar_edit_menu = "Edit";
	public String gui_menubar_edit_preferences = "Preferences...";
	public String gui_menubar_about_menu = "About";
	public String gui_menubar_about_about = "About "+name;
	
	// Preferences pop-up
	public String gui_popup_preferences_type_dir_panel_btnlabel = "Open...";
	public String gui_popup_preferences_type_dir_dlg_title = "Open a directory";
	public String gui_popup_preferences_type_dir_dlg_btnlabel = "Open";
	public String gui_popup_preferences_title = "Preferences";
	
	public String gui_popup_preferences_proplabel_path = "Branches directory";
	public String gui_popup_preferences_proplabel_lang = "Language";
	
	public String gui_popup_preferences_btn_apply = "Apply";
	public String gui_popup_preferences_btn_restore = "Restore Default";
	public String gui_popup_preferences_btn_cancel = "Cancel";
	
	public String gui_popup_preferences_applymsg_ok = "Preference successfully saved.";
	public String gui_popup_preferences_applymsg_error = "Could not save preference properly.";
	
	public final String getFullName() { return String.format("%s %s", name,version); }
	public final String getAuthors() { return String.join(", ", authors); }
	
}
