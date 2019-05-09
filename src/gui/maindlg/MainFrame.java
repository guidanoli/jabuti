package gui.maindlg;

import javax.swing.*;

import error.FatalError;
import io.GlobalProperties;
import java.awt.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	// properties
	protected GlobalProperties gp;

	// size parameters
	public final int DEF_H = 300;
	public final int DEF_W = 600;

	// containers
	public MainPanel panel;

	// components

	public MainFrame(String name) {
		// set frame name
		super(name);
		// properties
		gp = GlobalProperties.get();
		if( gp == null )
			FatalError.show("Could not manage preferences",this);
		// set bounds in the middle
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int s_w = d.width;
		int s_h = d.height;
		int x = (s_w - DEF_W) / 2;
		int y = (s_h - DEF_H) / 2;
		setBounds(x, y, DEF_W, DEF_H);
		panel = new MainPanel(gp);
		// exit on close
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// can't resize
		setResizable(false);
		// set panel layout
		getContentPane().add(panel);
		// set menu bar
		this.setJMenuBar(new MenuBar(this,gp));
		// set look and feel
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			FatalError.show(e.getMessage(),this);
		}
	}
	
}
