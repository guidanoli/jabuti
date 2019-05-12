package gui.maindlg;
import javax.swing.JFrame;
import gui.error.FatalError;
import io.GlobalProperties;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	// properties
	protected GlobalProperties gp;

	// size parameters
	public final int DEF_H = 300;
	public final int DEF_W = 600;

	// containers
	public MainPanel panel;

	public MainFrame(String name) {
		gp = GlobalProperties.get();
		if( gp == null ) FatalError.show("Could not manage preferences",this);
		panel = new MainPanel(gp);
		getContentPane().add(panel);
		this.setJMenuBar(new MenuBar(this,gp));
		gui.DefaultFrame.set(this, new gui.CloseFrameCallback() {
			public void close() {
				gp.cleanUp();
				System.exit(0);
			}
		});
		setVisible(true);
	}
	
}
