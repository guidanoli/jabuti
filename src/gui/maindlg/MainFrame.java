package gui.maindlg;
import javax.swing.JFrame;
import gui.error.FatalError;
import vars.GlobalProperties;
import vars.Language;

/**
 * 
 * <p>The {@code MainFrame} class defines the barebones of the JFrame component for the
 * application, setting default closing operations, which can be manipulated later on
 * the application when a sensitive task is being executed and closing promptly the process
 * might damage the user's data - for example, when setting up or compiling branches. This
 * functionality is coordinated by the {@link MainFrame#setCloseOperation(int) setCloseOperation}
 * function.
 * 
 * @author guidanoli
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	/**
	 * <p>Closes the window by clicking on the X button on the upper right corner
	 */
	public static final int CLOSE = 0;
	
	/**
	 * <p>Does not allow the user to close the window by the X button, although it can
	 * still be terminated by force by the use of the Task Manager, on Windows, for example.
	 */
	public static final int TRAY = 1;
	
	private Language lang = Language.getInstance();
	protected GlobalProperties gp = GlobalProperties.getInstance();
	public MainPanel panel;
	protected int closeOperation = CLOSE;

	/**
	 * 
	 * Constructs the Main Frame, displaying it on screen.
	 * The default closing operation is {@link #CLOSE}.
	 * 
	 * @param name
	 */
	public MainFrame(String name) {
		super(name);
		if( gp == null ) FatalError.show(lang.get("gui_mainframe_gp_error"),this);
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
	
	/**
	 * Sets the Main Frame close operation mode
	 * @param op - operation mode<br>It can assume of the following values:
	 * <ul>
	 * <li>{@link #CLOSE}</li>
	 * <li>{@link #TRAY}</li>
	 * </ul>
	 */
	public void setCloseOperation(int op)
	{
		if( op != CLOSE && op != TRAY ) return;
		closeOperation = op;
	}
	
}
