package svn;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import gui.defaults.DefaultNotificationPopup;
import gui.error.FatalError;
import gui.error.LightError;
import svn.error.MakeErrorListener;
import svn.error.SetupErrorListener;
import vars.Language;
import vars.properties.GlobalProperties;
import vars.properties.bool.NotificationProperty;

/**
 * <p>The {@code Launcher} class servers the purpose of managing the set up and compile jobs, 
 * with respect to thread buffers, semaphores and call backs to the main panel.
 * <p>It does not handles the processes directly. This functionality is delegated to the methods of the
 * {@link svn.TortoiseHandler TortoiseHandler} wrapper class.
 * <p>Every progress update triggered by a branch calls the
 * {@link svn.LaunchProgressListener#progressUpdate(int, int, int) LaunchProgressListener.progressUpdate}
 * method, that updates the JTable from the {@link gui.dialog.main.MainPanel} with the corresponding status
 * for each branch (if there is any activity at all).
 * It operates the following way:
 * <ol>
 * <li>List all branches that have been scheduled to setup or compile</li>
 * <li>For each branch, create a thread (even for the ones that have no jobs scheduled) and put it on the
 * thread buffer. The thread buffer maximum size is set by the property 'maxthreads'.</li>
 * <li>When a thread is allowed to be run, it first checks if there is any setup job scheduled and executes
 * it if so. The setup command is delegated to the {@link svn.TortoiseHandler#setup setup} method.</li>
 * <li>Then, after the setup job is complete, it checks if there is any compile job scheduled and executes
 * it if so. The make command is delegated to the {@link svn.TortoiseHandler#make make} method.</li>
 * <li>The thread is removed from the buffer.</li>
 * <li>When the last branch job has been completed, the {@link LaunchProgressListener#launchEnded launchEnded}
 * method is triggered, making the application return to its previously idle state.</li>
 * </ol>
 * @author guidanoli
 * @see svn.TortoiseHandler TortoiseHandler
 */
public class Launcher {

	// managers
	Language lang = Language.getInstance();
	GlobalProperties gp = GlobalProperties.getInstance();
	BranchManager branchManager;
	TortoiseHandler tortoise = new TortoiseHandler();
	LaunchProgressListener progressListener;
	SetupErrorListener setupListener;
	MakeErrorListener makeListener;
	
	// branches
	String [] branchNames;
	
	// launch options
	int maxThreadCount = Integer.parseInt(gp.get("maxthreads"));
	int cleanUps = Integer.parseInt(gp.get("cleanups"));
	
	// threads
	Thread [] threads;
	static Semaphore threadBufferSem;
	static Semaphore countSem = new Semaphore(1);
	int toBeRunThreads = 0;
	boolean interrupted = false;
	boolean emptyJob = true;
	
	/**
	 * Launches every branch setup and/or compile job as set on the Main Panel JTable.
	 * @param manager - an instance of the Branch Manager class
	 * @param listener - an implementation of the LaunchProgressListener interface
	 * @see svn.BranchManager
	 * @see svn.LaunchProgressListener
	 */
	public Launcher( BranchManager manager, LaunchProgressListener listener , SetupErrorListener setupListener ,
			MakeErrorListener makeListener ) {
		this.progressListener = listener;
		this.setupListener = setupListener;
		this.makeListener = makeListener;
		this.branchManager = manager;
		this.branchNames = manager.getBranchNames();
		if( maxThreadCount < 1 )
			FatalError.show("Maximum number of threads is invalid.");
		threadBufferSem = new Semaphore(maxThreadCount);
		boolean [] setup = manager.getBoolSetup();
		boolean [] make = manager.getBoolMake();
		if( branchNames == null || setup == null || make == null || listener == null )
			FatalError.show("Could not gather informations to launch.");
		toBeRunThreads = branchNames.length;
		for( boolean s : setup ) if( s ) emptyJob = false;
		if( emptyJob ) for( boolean m : make ) if( m ) emptyJob = false;
		threads = new Thread[branchNames.length];
		listener.launchBegan();
		for( int i = 0 ; i < branchNames.length ; i++ )
			threads[i] = launch(i,setup[i],make[i]);
	}
	
	public void interrupt() {
		interrupted = true;
		tortoise.killProcesses();
		progressListener.launchEnded();
	}
	
	/**
	 * Initiates launch thread of i-th branch
	 * @param i - index of branch in branchNames array
	 * @param setup - if the branch will do a setup job
	 * @param make - if the branch will be compiled
	 * @return Thread object
	 * @see java.lang.Thread Thread
	 */
	protected Thread launch(int i, boolean setup, boolean make) {
		String name = branchNames[i];
		LauncherLogManager logManager = new LauncherLogManager(name);
		Thread t = new Thread() {
			public void run() {
				boolean success;
				boolean validDir = tortoise.isTortoiseDir(name);
				int state_setup = setup ? ( validDir ? LaunchProgressListener.WAITING : LaunchProgressListener.INVALID ) : LaunchProgressListener.OFF;
				int state_make = make ? ( validDir ? LaunchProgressListener.WAITING : LaunchProgressListener.INVALID ) : LaunchProgressListener.OFF;
				update(i, state_setup, state_make);
				joinThread();
				if( setup || make )
				{
					if( validDir )
					{
						if( setup )
						{
							state_setup = LaunchProgressListener.UNLOCKING;
							update(i, state_setup, state_make);
							if(interrupted) return;
							success = tortoise.cleanUp(name,cleanUps);
							if(interrupted) return;
							if(success)
							{
								new DefaultNotificationPopup(
										NotificationProperty.Type.CLEANUP,
										lang.format("gui_notification_launcher_clean_success", name));
								state_setup = LaunchProgressListener.RUNNING;
								update(i, state_setup, state_make);
							}
							else
							{
								new DefaultNotificationPopup(
										NotificationProperty.Type.CLEANUP,
										lang.format("gui_notification_launcher_clean_fail", name));
								state_setup = LaunchProgressListener.FAILED;
								if( make ) state_make = LaunchProgressListener.FAILED;
								update(i, state_setup, state_make);
								exitThread();
								return;
							}
							if(interrupted) return;
							Long oldRevisionNumber = tortoise.getRevisionNumber(name);
							success = tortoise.setup(name, setupListener);
							if(interrupted) return;
							if(success)
							{
								new DefaultNotificationPopup(
										NotificationProperty.Type.SETUP,
										lang.format("gui_notification_launcher_setup_success", name));
								state_setup = LaunchProgressListener.ENDED;
								update(i, state_setup, state_make);
								Long newRevisionNumber = tortoise.getRevisionNumber(name);
								logManager.logSetup(oldRevisionNumber, newRevisionNumber);
							}
							else
							{
								new DefaultNotificationPopup(
										NotificationProperty.Type.SETUP,
										lang.format("gui_notification_launcher_setup_fail", name));
								state_setup = LaunchProgressListener.FAILED;
								if( make ) state_make = LaunchProgressListener.FAILED;
								update(i, state_setup, state_make);
								exitThread();
								return;
							}
						}
						if(interrupted) return;
						if( make ) {
							state_make = LaunchProgressListener.RUNNING;
							update(i, state_setup, state_make);
							Instant start = Instant.now();
							success = tortoise.make(name, makeListener);
							if(interrupted) return;
							if(success)
							{
								Instant end = Instant.now();
								new DefaultNotificationPopup(
										NotificationProperty.Type.MAKE,
										lang.format("gui_notification_launcher_make_success", name));
								Duration timeElapsed = Duration.between(start, end);
								state_make = LaunchProgressListener.ENDED;
								update(i, state_setup, state_make);
								logManager.logMake(timeElapsed.toMillis());
							}
							else
							{
								new DefaultNotificationPopup(
										NotificationProperty.Type.MAKE,
										lang.format("gui_notification_launcher_make_fail", name));
								state_make = LaunchProgressListener.FAILED;
								update(i, state_setup, state_make);
								exitThread();
								return;
							}
						}
					}
					else
					{
						LightError.show(lang.format("gui_errmsg_launcher_invalidfolder", name));
					}
				}
				exitThread();
			}
		};
		
		t.start();
		return t;
	}
	
	/**
	 * Updates JTable icons with current progress
	 * @param i - branch index
	 * @param setup - setup state
	 * @param make - compilation state
	 * @see LaunchProgressListener#progressUpdate(int, int, int) progressUpdate(int, int, int)
	 */
	private void update(int i, int setup, int make)
	{
		if(interrupted) return;
		progressListener.progressUpdate(i, setup, make);
	}
	
	/**
	 * <p>Safely joins the thread buffer
	 */
	private void joinThread()
	{
		if(interrupted) return;
		try {
			threadBufferSem.acquire();
		} catch (InterruptedException e) {
			FatalError.show(e);
		}
	}
			
	/**
	 * <p>Safely exits the thread buffer. If it is the last thread to exit
	 * the buffer, {@link LaunchProgressListener#launchEnded() launchEnded()}
	 * will be called.
	 */
	private void exitThread()
	{
		if(interrupted) return;
		threadBufferSem.release();
		try {
			countSem.acquire();
		} catch (InterruptedException e) {
			FatalError.show(e);
		}
		toBeRunThreads--;
		System.out.println(toBeRunThreads);
		if(toBeRunThreads==0)
		{
			if(!emptyJob)
			{
				new DefaultNotificationPopup(
						NotificationProperty.Type.GENERAL,
						lang.get("gui_notification_launcher_done"));
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					FatalError.show(e);
				}
			}
			else
			{
				LightError.show(lang.get("gui_errmsg_launcher_emptyjob"));
			}
			progressListener.launchEnded();
		}
		countSem.release();
	}
	
}
