package gui.maindlg;
import javax.swing.JFrame;
import gui.error.FatalError;
import vars.GlobalProperties;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	public static final int CLOSE = 0;
	public static final int TRAY = 1;
	
	protected GlobalProperties gp = GlobalProperties.gp;
	public MainPanel panel;
	protected int closeOperation = CLOSE;

	public MainFrame(String name) {
		super(name);
		if( gp == null ) FatalError.show(vars.Language.get("gui_mainframe_gp_error"),this);
		panel = new MainPanel(gp);
		getContentPane().add(panel);
		setJMenuBar(new MenuBar(this,gp));
		MainFrame aux = this;
		gui.DefaultFrame.set(this, new gui.CloseFrameCallback() {
			public void close() {
				switch(closeOperation)
				{
				case CLOSE:
					gp.cleanUp();
					System.exit(0);
					break;
				case TRAY:
					aux.setExtendedState(JFrame.ICONIFIED);
				}
			}
		});
		setVisible(true);
	}
	
	public void setCloseOperation(int op)
	{
		if( op != CLOSE && op != TRAY ) return;
		closeOperation = op;
	}
	
}
