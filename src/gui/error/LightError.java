package gui.error;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vars.Language;

public class LightError {

	protected final static Language lang = Language.getInstance();
	public final static String LIGHT_MSG_TITLE = lang.get("gui_lighterror_msg_title");
	public final static String LIGHT_GENERAL_MSG = lang.get("gui_lighterror_general_msg");
	
	/* Show message */
	public static void show(String msg, String title, JFrame parent)
	{
		JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/* Show exception */
	public static void show(Exception e, String title, JFrame parent) {
		String msg = e.getMessage();
		if( msg == null ) msg = LIGHT_GENERAL_MSG;
		show(msg,title,parent);
	}
	
	// SIGNATURE OVERLOAD -- missing title
	public static void show(String msg, JFrame parent) { show(msg,LIGHT_MSG_TITLE,parent); }
	public static void show(Exception e, JFrame parent) { show(e,LIGHT_MSG_TITLE,parent); }
	
	// SIGNATURE OVERLOAD -- missing parent
	public static void show(String msg, String title) { show(msg,title,(JFrame)null); }
	public static void show(Exception e, String title) { show(e,title,(JFrame)null); }
	
	// SIGNATURE OVERLOAD -- missing parent and title
	public static void show(String msg) { show(msg,(JFrame)null); }
	public static void show(Exception e) { show(e,(JFrame)null); }
	
}
