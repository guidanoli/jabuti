package gui;
import javax.swing.*;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private JMenu file_menu = new JMenu("File");
	private JMenu edit_menu = new JMenu("Edit");
	private JMenu about_menu = new JMenu("About");
		
	public MenuBar() {
		add(file_menu);
		add(edit_menu);
		add(about_menu);
		// constructor
	}
	
}
