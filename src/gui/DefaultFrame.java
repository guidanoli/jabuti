package gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;

import gui.error.FatalError;

public class DefaultFrame {

	public static void set(JFrame frame, CloseFrameCallback close_cb) {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setIconImage(new ImageIcon("ressources/setmeup.png").getImage());
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			FatalError.show(e.getMessage(),frame);
		}
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				close_cb.close();
		    }
		});
	}

}
