package svn;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import gui.error.FatalError;
import gui.error.LightError;
import vars.Language;

public class Launcher {

	BranchManager branchManager;
	TortoiseHandler tortoise = new TortoiseHandler();
	LaunchProgressListener listener;
	String [] branchNames;
	static Semaphore threadBufferSem;
	static Semaphore countSem = new Semaphore(1);
	int toBeRunThreads = 0;
	boolean emptyJob = true;
	
	public Launcher(BranchManager manager, LaunchProgressListener listener, int maxThreadCount ) {
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
		tortoise.getInfo(branchNames[i]);
		new Thread() {
			public void run() {
				boolean validDir = tortoise.isTortoiseDir(branchNames[i]);
				int state_setup = setup ? ( validDir ? LaunchProgressListener.WAITING : LaunchProgressListener.INVALID ) : LaunchProgressListener.OFF;
				int state_make = make ? ( validDir ? LaunchProgressListener.WAITING : LaunchProgressListener.INVALID ) : LaunchProgressListener.OFF;
				listener.progressUpdate(i, state_setup, state_make);
				if( validDir )
				{
					ThreadBufferIsRunning(true);
					if(setup) {
						state_setup = LaunchProgressListener.RUNNING;
						listener.progressUpdate(i, state_setup, state_make);
						tortoise.setup(branchNames[i]);
						state_setup = LaunchProgressListener.ENDED;
						listener.progressUpdate(i, state_setup, state_make);
					}
					if(make) {
						state_make = LaunchProgressListener.RUNNING;
						listener.progressUpdate(i, state_setup, state_make);
						tortoise.make(branchNames[i]);
						state_make = LaunchProgressListener.ENDED;
						listener.progressUpdate(i, state_setup, state_make);
					}
					if( make || setup ) branchManager.setLastSetupDate(branchNames[i], System.currentTimeMillis());
					ThreadBufferIsRunning(false);
				}
				endThread();
				if( !validDir ) LightError.show(Language.format("gui_errmsg_launcher_invalidfolder", branchNames[i]));
			}
		}.start();
	}
	
	private void ThreadBufferIsRunning( boolean isRunning ) {
		try {
			if( isRunning ) threadBufferSem.acquire();
			else threadBufferSem.release();
		} catch (InterruptedException e) {
			FatalError.show(e);
		}
	}
		
	private void endThread()
	{
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
				LightError.show(Language.get("gui_errmsg_launcher_emptyjob"));
			}
			listener.launchEnded();
		}
		countSem.release();
	}
	
}
