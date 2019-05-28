package svn;

import java.util.concurrent.Semaphore;
import gui.error.FatalError;

public class Launcher {

	TortoiseHandler tortoise = new TortoiseHandler();
	LaunchProgressListener listener;
	String [] branchNames;
	static Semaphore threadBufferSem;
	static Semaphore countSem = new Semaphore(1);
	int toBeRunThreads = 0;
	
	public Launcher(BranchManager manager, LaunchProgressListener listener, int maxThreadCount ) {
		this.listener = listener;
		this.branchNames = manager.getBranchNames();
		if( maxThreadCount < 1 )
			FatalError.show("Maximum number of threads is invalid.");
		threadBufferSem = new Semaphore(maxThreadCount);
		boolean [] setup = manager.getBoolSetup();
		boolean [] make = manager.getBoolMake();
		if( branchNames == null || setup == null || make == null || listener == null )
			FatalError.show("Could not gather informations to launch.");
		toBeRunThreads = branchNames.length;
		listener.launchBegan();
		for( int i = 0 ; i < branchNames.length ; i++ )
			launch(i,setup[i],make[i]);
	}
	
	protected void launch(int i, boolean setup, boolean make) {
		tortoise.getInfo(branchNames[i]);
		new Thread() {
			public void run() {
				//TODO: hook up to vis
				int state_setup = setup ? LaunchProgressListener.WAITING : LaunchProgressListener.OFF;
				int state_make = make ? LaunchProgressListener.WAITING : LaunchProgressListener.OFF;
				listener.progressUpdate(i, state_setup, state_make);
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
				ThreadBufferIsRunning(false);
				endThread();
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
			listener.launchEnded();
		countSem.release();
	}
	
}
