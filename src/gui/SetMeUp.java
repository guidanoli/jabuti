package gui;
import javax.swing.SwingUtilities;
import gui.maindlg.MainFrame;
import vars.GlobalProperties;

/**
 * 
 * <p>The {@code SetMeUp} class serves as a starting point on the program call chain. 
 * It initializes global properties and the main frame component safely with the use
 * of the {@link javax.swing.SwingUtilities#invokeLater(Runnable) invokeLater} function.
 * 
 * @author guidanoli
 * @see gui.maindlg.MainFrame MainFrame
 *
 */
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
