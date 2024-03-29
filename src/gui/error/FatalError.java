package gui.error;
import java.awt.Component;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import vars.Language;

public class FatalError {

	protected final static Language lang = Language.getInstance();
	protected final static String ERROR_MSG_TITLE = lang.get("gui_error_fatal_msg_title");
	protected final static String ERROR_LOG_TITLE = lang.get("gui_error_fatal_log_title");
	protected final static String ERROR_GENERAL_MSG = lang.get("gui_error_fatal_general_msg");
	protected final static JFrame DEFAULT_PARENT = null;
	protected final static boolean DEFAULT_EXIT = true;
	
	public static void show (String msg, Component parent, boolean exit) {
		JOptionPane.showMessageDialog(parent, msg, ERROR_MSG_TITLE, JOptionPane.ERROR_MESSAGE);
		if( exit ) System.exit(0);
	}
	
	public static void showLog (String msg, Component parent, boolean exit) {
		/* Setting up */
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(getScrollingPane(msg));
		/* Helper */
		String helper = ErrorHelper.getHelp(msg);
		if( !helper.equals("") ) {
			panel.add(getScrollingPane(helper));
		}
		/* Display */
		JOptionPane.showMessageDialog(parent, panel, ERROR_LOG_TITLE, JOptionPane.ERROR_MESSAGE);
		if( exit ) System.exit(0);
	}
	
	private static JScrollPane getScrollingPane(String str) {
		JTextArea txt = new JTextArea(str);
		JScrollPane sp = new JScrollPane();
		txt.setEditable(false);
		txt.setLineWrap(true);
		txt.setWrapStyleWord(true);
		sp.setViewportView(txt);
		sp.setPreferredSize(new Dimension(1000,200));
		return sp;
	}
	
	public static void show (Exception e, Component parent, boolean exit) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String encoding = "UTF-8";
	    try (PrintStream ps = new PrintStream(baos, true, encoding)) {
	        e.printStackTrace(ps);
	    } catch (UnsupportedEncodingException e1) {
			show(String.format("Could not encode error message because '%s' is not supported.", encoding),parent,true);
		}
	    String data = new String(baos.toByteArray(), StandardCharsets.UTF_8);
	    String [] options = {"OK","Show log"};
	    String msg = e.getMessage();
	    if( msg == null ) msg = ERROR_GENERAL_MSG;
	    int choice = JOptionPane.showOptionDialog(	parent,
				    								msg,
			    									ERROR_MSG_TITLE,
			    									JOptionPane.OK_CANCEL_OPTION,
			    									JOptionPane.ERROR_MESSAGE,
			    									null, options, options[0]);
	    if( choice == 1 )
	    	showLog(data,parent,false);
	    if( exit ) System.exit(0);
	}
	
	// SIGNATURE OVERLOAD -- missing exit
	public static void show (String msg, Component parent) { show(msg,parent,DEFAULT_EXIT); }
	public static void show (Exception e, Component parent) { show(e,parent,DEFAULT_EXIT); }
	public static void showScrollPaneMessage (String msg, Component parent) {
		showLog(msg,parent,DEFAULT_EXIT);
	}
	
	// SIGNATURE OVERLOAD -- missing parent
	public static void show (String msg) { show(msg,DEFAULT_PARENT); }
	public static void show (Exception e) { show(e,DEFAULT_PARENT); }
	public static void showScrollPaneMessage (String msg) {
		showScrollPaneMessage(msg,DEFAULT_PARENT);
	}
	
	
}

