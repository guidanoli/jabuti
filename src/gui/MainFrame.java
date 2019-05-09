package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	// properties
	protected String PROP_PATH = "setmeup.properties";
	protected Properties prop = new Properties();

	// size parameters
	public final int DEF_H = 300;
	public final int DEF_W = 400;

	// containers
	JPanel panel = new MainPanel(prop);

	// components

	public MainFrame(String name) throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		// set frame name
		super(name);
		// set bounds in the middle
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int s_w = d.width;
		int s_h = d.height;
		int x = (s_w - DEF_W) / 2;
		int y = (s_h - DEF_H) / 2;
		setBounds(x, y, DEF_W, DEF_H);
		// properties
		if (new File(PROP_PATH).createNewFile()) { saveDefaultProperties(); }
		prop.load(new FileInputStream(PROP_PATH));
		panel = new MainPanel(prop);
		// exit on close
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// set panel layout
		getContentPane().add(panel);
		// set menu bar
		this.setJMenuBar(new MenuBar(this));
		// set look and feel
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	}

	public static void main(String[] args) {
		MainFrame painel;
		try {
			painel = new MainFrame("SetMeUp v.1.0");
			painel.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// code
	}

	protected void saveDefaultProperties() throws FileNotFoundException, IOException {
		prop.setProperty("path", System.getProperty("user.home"));
		while (prop.getProperty("path") == null) {
			System.out.println("WAITING...");
		}
		prop.store(new FileOutputStream(PROP_PATH), null);
	}
	
}
