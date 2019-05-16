package vis;

public class Launcher {
	
	String [] branchNames;
	boolean [] setup;
	boolean [] make;
	
	public Launcher(BranchManager manager) {
		branchNames = manager.getBranchNames();
		setup = manager.getBoolSetup();
		make = manager.getBoolMake();
	}

	//TODO: Launcher functionalities
	
}
