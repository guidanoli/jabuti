package gui.error;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vars.Language;

public class LightError {
	
	/* Show message */
	public static void show(String msg, String title, Component parent)
	{
		JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/* Show exception */
	public static void show(Exception e, String title, Component parent) {
		String msg = e.getMessage();
		Language lang = Language.getInstance();
		if( msg == null ) msg = lang.get("gui_error_light_general_msg");
		show(msg,title,parent);
	}
	
	// SIGNATURE OVERLOAD -- missing title
	public static void show(String msg, Component parent) {
		Language lang = Language.getInstance();
		show(msg,lang.get("gui_error_light_msg_title"),parent);
	}
	public static void show(Exception e, Component parent) {
		Language lang = Language.getInstance();
		show(e,lang.get("gui_error_light_msg_title"),parent);
	}
	
	// SIGNATURE OVERLOAD -- missing parent
	public static void show(String msg, String title) { show(msg,title,(JFrame)null); }
	public static void show(Exception e, String title) { show(e,title,(JFrame)null); }
	
	// SIGNATURE OVERLOAD -- missing parent and title
	public static void show(String msg) { show(msg,(JFrame)null); }
	public static void show(Exception e) { show(e,(JFrame)null); }
	
}
