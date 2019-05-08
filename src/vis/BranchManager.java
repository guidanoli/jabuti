package vis;

public class BranchManager {
	
	public BranchManager() {	}
	
	public String[] getBranchNames() {
		// TODO - Collect branches' names given directory
		return new String[]{"branch1","branch2","branch3"};
	}
	
	public String[] getLastSetupDates() {
		// TODO - Collect branches' last setup dates (configuration file)
		return new String[]{"01/01/2019","02/01/2019","03/01/2019"};
	}
	
	public boolean[] getBoolSetup() {
		// TODO - Collect user default (configuration file)
		return new boolean[]{true,true,false};
	}
	
	public boolean[] getBoolMake() {
		// TODO - Collect user default (configuration file)
		return new boolean[]{false,true,false};
	}
	
}
