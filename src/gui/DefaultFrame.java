package gui;

import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;

import gui.error.FatalError;

public class DefaultFrame {

	public static void set(JFrame frame, CloseFrameCallback close_cb) {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setMinimumSize(frame.getSize());
		frame.setIconImage(new ImageIcon(vars.LocalResources.icon.getAbsolutePath()).getImage());
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			FatalError.show(e,frame);
		}
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				close_cb.close();
		    }
		});
	}
	
	public static void set(JFrame frame) {
		set(frame, new CloseFrameCallback() { public void close() { frame.setVisible(false); } });
	}
	
	public static void forceClosing(JFrame frame) {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
}
