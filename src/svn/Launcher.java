package svn;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import gui.error.FatalError;
import gui.error.LightError;
import vars.GlobalProperties;
import vars.Language;

/**
 * <p>The {@code Launcher} class servers the purpose of managing the set up and compile jobs, 
 * with respect to thread buffers, semaphores and call backs to the main panel.
 * <p>It does not handles the processes directly. This functionality is delegated to the methods of the
 * {@link svn.TortoiseHandler TortoiseHandler} wrapper class.
 * <p>Every progress update triggered by a branch calls the
 * {@link svn.LaunchProgressListener#progressUpdate(int, int, int) LaunchProgressListener.progressUpdate}
 * method, that updates the JTable from the {@link gui.maindlg.MainPanel} with the corresponding status
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
	LaunchProgressListener listener;
	
	// branches
	String [] branchNames;
	
	// launch options
	int maxThreadCount = Integer.parseInt(gp.get("maxthreads"));
	int cleanUps = Integer.parseInt(gp.get("cleanups"));
	
	// threads
	static Semaphore threadBufferSem;
	static Semaphore countSem = new Semaphore(1);
	int toBeRunThreads = 0;
	boolean emptyJob = true;
	
	/**
	 * Launches every branch setup and/or compile job as set on the Main Panel JTable.
	 * @param manager - an instance of the Branch Manager class
	 * @param listener - an implementation of the LaunchProgressListener interface
	 * @param maxThreadCount - the maximum number of threads running
	 * @see svn.BranchManager
	 * @see svn.LaunchProgressListener
	 */
	public Launcher(BranchManager manager, LaunchProgressListener listener ) {
		this.listener = listener;
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
		listener.launchBegan();
		for( int i = 0 ; i < branchNames.length ; i++ )
			launch(i,setup[i],make[i]);
	}
	
	protected void launch(int i, boolean setup, boolean make) {
		new Thread() {
			public void run() {
				boolean validDir = tortoise.isTortoiseDir(branchNames[i]);
				int state_setup = setup ? ( validDir ? LaunchProgressListener.WAITING : LaunchProgressListener.INVALID ) : LaunchProgressListener.OFF;
				int state_make = make ? ( validDir ? LaunchProgressListener.WAITING : LaunchProgressListener.INVALID ) : LaunchProgressListener.OFF;
				listener.progressUpdate(i, state_setup, state_make);
				if( validDir )
				{
					joinThread();
					if( setup )
					{
						state_setup = LaunchProgressListener.UNLOCKING;
						listener.progressUpdate(i, state_setup, state_make);
						for(int i = 0 ; i < cleanUps; i++) tortoise.cleanUp(branchNames[i]);
						state_setup = LaunchProgressListener.RUNNING;
						listener.progressUpdate(i, state_setup, state_make);
						boolean success = tortoise.setup(branchNames[i]);
						if(success)
						{
							state_setup = LaunchProgressListener.ENDED;
							listener.progressUpdate(i, state_setup, state_make);
						}
						else
						{
							state_setup = LaunchProgressListener.FAILED;
							listener.progressUpdate(i, state_setup, state_make);
							exitThread();
							return;
						}
					}
					if( make ) {
						state_make = LaunchProgressListener.RUNNING;
						listener.progressUpdate(i, state_setup, state_make);
						boolean success = tortoise.make(branchNames[i]);
						if(success)
						{
							state_make = LaunchProgressListener.ENDED;
							listener.progressUpdate(i, state_setup, state_make);
						}
						else
						{
							state_make = LaunchProgressListener.FAILED;
							listener.progressUpdate(i, state_setup, state_make);
							exitThread();
							return;
						}
					}
					if( make || setup )
					{
						branchManager.setLastSetupDate(branchNames[i], System.currentTimeMillis());
					}
				}
				exitThread();
				if( !validDir ) LightError.show(lang.format("gui_errmsg_launcher_invalidfolder", branchNames[i]));
			}
		}.start();
	}
	
	/**
	 * <p>Safely joins the thread buffer
	 */
	private void joinThread()
	{
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
			listener.launchEnded();
		}
		countSem.release();
	}
	
}
