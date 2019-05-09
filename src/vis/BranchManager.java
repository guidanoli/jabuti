package vis;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Properties;

public class BranchManager {
	
	Properties prop;
	int num_branches;
	
	public BranchManager(Properties prop) {
		this.prop = prop;
	}
	
	public String[] getBranchNames() {
		String path = prop.getProperty("path");
		if( path == null ) { return new String[]{}; }
		File file = new File(path);
		String[] dirs = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		num_branches = dirs.length;
		return dirs;
	}
	
	public String[] getLastSetupDates() {
		// TODO - Collect branches' last setup dates (configuration file)
		return new String[num_branches];
	}
	
	public boolean[] getBoolSetup() {
		// TODO - Collect user default (configuration file)
		return new boolean[num_branches];
	}
	
	public boolean[] getBoolMake() {
		// TODO - Collect user default (configuration file)
		return new boolean[num_branches];
	}
	
}
