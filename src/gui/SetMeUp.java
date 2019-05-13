package gui;
import javax.swing.SwingUtilities;
import gui.maindlg.MainFrame;
import io.GlobalStrings;

public class SetMeUp {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new MainFrame(GlobalStrings.getFullName());
            }
        });
	}
	
}
