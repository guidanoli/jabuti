package gui;
import javax.swing.SwingUtilities;
import gui.maindlg.MainFrame;
import vars.GlobalProperties;


public class SetMeUp {
	
	public static void main(String[] args) {
		GlobalProperties.gp = GlobalProperties.getGP();
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new MainFrame(vars.Language.get("name")+" "+vars.Language.get("version"));
            }
        });
	}
	
}
