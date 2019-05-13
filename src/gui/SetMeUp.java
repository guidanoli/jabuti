package gui;
import javax.swing.SwingUtilities;
import gui.maindlg.MainFrame;


public class SetMeUp {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new MainFrame(vars.Language.get("name")+" "+vars.Language.get("version"));
            }
        });
	}
	
}
