package svn;

/**
 * <p>The <code>LaunchProgressListener</code> interface intends to connect the main application
 * graphical class {@link gui.dialog.main.MainPanel MainPanel} and its kernel classes such
 * as {@link svn.BranchManager BranchManager}, {@link svn.Launcher Launcher} and
 * {@link svn.TortoiseHandler TortoiseHandler}.
 * <p>The communication between these classes is consisted of three main phases:
 * <ol>
 * <li>Beggining - triggers {@link #launchBegan}</li>
 * <li>Progress - triggers {@link #progressUpdate(int, int, int)}</li>
 * <li>End - triggers {@link #launchEnded()}</li>
 * </ol>
 * @author guidanoli
 *
 */
public interface LaunchProgressListener {

	public static final int OFF = 0;
	public static final int WAITING = 1;
	public static final int UNLOCKING = 2;
	public static final int RUNNING = 3;
	public static final int ENDED = 4;
	public static final int INVALID = 5;
	public static final int FAILED = 6;

	/**
	 * Signals the application that the launch job has begun.
	 */
	public void launchBegan();
	
	/**
	 * Indicates the state of a certain branch job.
	 * @param i - index of branch in relation to the array returned by
	 * {@link svn.BranchManager#getBranchNames() getBranchNames}.
	 * @param setup - state of the setup job
	 * @param make - state of the compilation job
	 */
	public void progressUpdate(int i, int setup, int make);
	
	/**
	 * Signals the application that the launch job has ended.
	 * Should update JTable since the last setup dates may change.
	 */
	public void launchEnded();
	
}
