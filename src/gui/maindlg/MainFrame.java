package gui.maindlg;
import javax.swing.JFrame;
import gui.error.FatalError;
import io.GlobalProperties;
import io.GlobalStrings;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	protected GlobalProperties gp;
	public MainPanel panel;

	public MainFrame(String name) {
		super(name);
		gp = GlobalProperties.get();
		if( gp == null ) FatalError.show(GlobalStrings.gui_mainframe_gp_error,this);
		panel = new MainPanel(gp);
		getContentPane().add(panel);
		setJMenuBar(new MenuBar(this,gp));
		gui.DefaultFrame.set(this, new gui.CloseFrameCallback() {
			public void close() {
				gp.cleanUp();
				System.exit(0);
			}
		});
		setVisible(true);
	}
	
}
