package vis;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import gui.error.FatalError;

public class Launcher {
	
	LaunchProgressListener listener;
	String [] branchNames;
	boolean [] running;
	static Semaphore semaphore = new Semaphore(1);
	
	//TEMP
	Random r = new Random();
	
	public Launcher(BranchManager manager, LaunchProgressListener listener) {
		this.listener = listener;
		this.branchNames = manager.getBranchNames();
		running = new boolean[branchNames.length];
		boolean [] setup = manager.getBoolSetup();
		boolean [] make = manager.getBoolMake();
		if( branchNames == null || setup == null || make == null || listener == null )
			FatalError.show("Could not gather informations to launch.");
		for( int i = 0 ; i < branchNames.length ; i++ )
			launch(i,setup[i],make[i]);
	}
	
	protected void launch(int i, boolean setup, boolean make) {
		new Thread() {
			public void run() {
				//TODO: hook up to vis
				int state_setup = setup ? LaunchProgressListener.WAITING : LaunchProgressListener.OFF;
				int state_make = make ? LaunchProgressListener.WAITING : LaunchProgressListener.OFF;
				setProgressRunning(i,true);
				listener.progressUpdate(i, state_setup, state_make);
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
				setProgressRunning(i,false);
				checkEndedAll();
			}
		}.start();
	}
	
	private void setProgressRunning(int i, boolean b) {
		try {
			semaphore.acquire();
			running[i] = b;
			semaphore.release();
		} catch (InterruptedException e) {
			FatalError.show(e);
		}
	}
	
	private void checkEndedAll() {
		try {
			semaphore.acquire();
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
			semaphore.release();
		} catch (InterruptedException e) {
			FatalError.show(e);
		}
	}
	
}
