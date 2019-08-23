package gui.defaults;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import gui.error.FatalError;
import vars.LocalResources;

public class DefaultPopup extends JDialog {

	private static final long serialVersionUID = 5239596504066756266L;

	/**
	 * Constructs a modal with the software default icon and Windows Look and Feel
	 * @param owner - parent frame
	 * @param title - pop up title
	 * @see javax.swing.JDialog JDialog
	 */
	public DefaultPopup(JFrame owner, String title) {
		super(owner,title,true);
		try {
			InputStream is = LocalResources.getStream(LocalResources.icon);
			setIconImage(new ImageIcon(ImageIO.read(is)).getImage());
		} catch (IOException e) {
			FatalError.show(e);
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			FatalError.show(e,owner,false);
		}
	}

}
