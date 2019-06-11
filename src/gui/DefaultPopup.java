package gui;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import gui.error.FatalError;

@SuppressWarnings("serial")
public class DefaultPopup extends JDialog {

	/**
	 * Constructs a modal with the software default icon and Windows Look and Feel
	 * @param owner - parent frame
	 * @param title - pop up title
	 * @see javax.swing.JDialog JDialog
	 */
	public DefaultPopup(JFrame owner, String title) {
		super(owner,title,true);
		setIconImage(new ImageIcon(vars.LocalResources.icon.getAbsolutePath()).getImage());
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			FatalError.show(e,owner,false);
		}
	}

}
