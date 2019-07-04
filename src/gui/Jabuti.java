package gui;
import javax.swing.SwingUtilities;

import gui.dialog.main.MainFrame;
import vars.Language;

/**
 * 
 * <p>The {@code SetMeUp} class serves as a starting point on the program call chain. 
 * It initializes the main frame component safely with the use of the
 * {@link javax.swing.SwingUtilities#invokeLater(Runnable) invokeLater} function.
 * 
 * @author guidanoli
 * @see gui.dialog.main.MainFrame MainFrame
 *
 */
public class Jabuti {
	
	private static Language lang = Language.getInstance();
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new MainFrame(lang.get("name"));
            }
        });
	}
	
}
