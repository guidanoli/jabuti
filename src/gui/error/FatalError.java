package gui.error;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FatalError {

	public static void show (String msg, JFrame parent) {
		JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

	public static void show (String msg) { show(msg,null); }
	
}
