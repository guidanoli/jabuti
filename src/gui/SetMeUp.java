package gui;
import javax.swing.SwingUtilities;
import gui.maindlg.MainFrame;

public class SetMeUp {

	public final static String version = "1.0";
	public final static String name = "SetMeUp";
	public final static String[] authors = {"Guilherme Dantas"};
	public final static String repository = "https://github.com/guidanoli/setmeup";
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new MainFrame(getName());
            }
        });
	}

	public static String getName() { return String.format("%s %s", name,version); }
	public static String getAuthors() { return String.join(", ", authors); }
	
}
