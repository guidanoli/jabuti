package gui;
import javax.swing.SwingUtilities;
import gui.maindlg.MainFrame;

public class SetMeUp {

	public static final String version = "1.0";
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new MainFrame("SetMeUp v."+version);
            }
        });
	}

}
