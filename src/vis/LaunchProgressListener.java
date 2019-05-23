package vis;

public interface LaunchProgressListener {

	public static final int OFF = 0;
	public static final int RUNNING = 1;
	public static final int WAITING = 2;
	public static final int ENDED = 3;
	public static final int FAILED = 4;
	
	// Signal that launch will begin
	public void launchBegan();
	
	// Indicates the launch of a certain branch began
	// setup and make receive OFF, RUNNING or WAITING
	public void progressUpdate(int i, int setup, int make);
	
	// Signal that all branches have been set up 
	public void launchEnded();
	
}
