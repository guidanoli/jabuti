package gui;
import javax.swing.SwingUtilities;
import gui.maindlg.MainFrame;

public class SetMeUp {

	protected final static String version = "1.0";
	protected final static String name = "SetMeUp";
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new MainFrame(String.format("%s %s", name,version));
            }
        });
	}

}
