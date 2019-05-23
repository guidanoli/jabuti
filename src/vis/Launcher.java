package vis;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import gui.error.FatalError;

public class Launcher {
	
	LaunchProgressListener listener;
	String [] branchNames;
	boolean [] running;
	static Semaphore accessRunningSem = new Semaphore(1);
	static Semaphore threadBufferSem;
	
	// TODO: REMOVE RANDOM
	Random r = new Random();
	
	public Launcher(BranchManager manager, LaunchProgressListener listener, int maxThreadCount ) {
		this.listener = listener;
		this.branchNames = manager.getBranchNames();
		running = new boolean[branchNames.length];
		if( maxThreadCount < 1 )
			FatalError.show("Maximum number of threads is invalid.");
		threadBufferSem = new Semaphore(maxThreadCount);
		boolean [] setup = manager.getBoolSetup();
		boolean [] make = manager.getBoolMake();
		if( branchNames == null || setup == null || make == null || listener == null )
			FatalError.show("Could not gather informations to launch.");
		listener.launchBegan();
		for( int i = 0 ; i < branchNames.length ; i++ )
			launch(i,setup[i],make[i]);
	}
	
	protected void launch(int i, boolean setup, boolean make) {
		new Thread() {
			public void run() {
				//TODO: hook up to vis
				int state_setup = setup ? LaunchProgressListener.WAITING : LaunchProgressListener.OFF;
				int state_make = make ? LaunchProgressListener.WAITING : LaunchProgressListener.OFF;
				listener.progressUpdate(i, state_setup, state_make);
				ThreadBufferIsRunning(true);
				setProgressRunning(i,true);
				if(setup) {
					state_setup = LaunchProgressListener.RUNNING;
					listener.progressUpdate(i, state_setup, state_make);
					try {
						TimeUnit.SECONDS.sleep(r.nextInt(5));
					} catch (InterruptedException e) {
						FatalError.show(e);
					}
					state_setup = LaunchProgressListener.ENDED;
					listener.progressUpdate(i, state_setup, state_make);
				}
				if(make) {
					state_make = LaunchProgressListener.RUNNING;
					listener.progressUpdate(i, state_setup, state_make);
					try {
						TimeUnit.SECONDS.sleep(r.nextInt(5));
					} catch (InterruptedException e) {
						FatalError.show(e);
					}
					state_make = LaunchProgressListener.ENDED;
					listener.progressUpdate(i, state_setup, state_make);
				}
				ThreadBufferIsRunning(false);
				setProgressRunning(i,false);
				checkEndedAll();
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
	
	private void setProgressRunning(int i, boolean b) {
		try {
			accessRunningSem.acquire();
			running[i] = b;
			accessRunningSem.release();
		} catch (InterruptedException e) {
			FatalError.show(e);
		}
	}
		
	private void checkEndedAll() {
		try {
			accessRunningSem.acquire();
			boolean all_ended = true;
			for( boolean rb : running ) {
				if( rb ) {
					all_ended = false;
					break;
				}
			}
			if(all_ended) {
				listener.launchEnded();
			}
			accessRunningSem.release();
		} catch (InterruptedException e) {
			FatalError.show(e);
		}
	}
	
}
