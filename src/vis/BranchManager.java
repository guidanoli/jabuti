package vis;
import java.io.File;
import java.io.FilenameFilter;
import io.GlobalProperties;

public class BranchManager {
	
	GlobalProperties gp;
	String[] branches;
	int num_branches;
	
	public BranchManager(GlobalProperties gp) {
		this.gp = gp;
	}
	
	public String[] getBranchNames() {
		String path = gp.getProperty("path");
		if( path == null ) { return null; }
		File file = new File(path);
		branches = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		if( branches == null ) { return null; }
		num_branches = branches.length;
		return branches;
	}
	
	public String[] getLastSetupDates() {
		if( branches == null ) { return null; }
		String[] v = new String[num_branches];
		for( int i = 0 ; i < v.length; i++ )
		{
			String prop = gp.getProperty("branches."+branches[i]+".lastsetupdate");
			v[i] = prop == null ? "Unknown" : prop;
		}
		return v;
	}
	
	public boolean[] getBoolSetup() {
		if( branches == null ) { return null; }
		boolean[] v = new boolean[num_branches];
		for( int i = 0 ; i < v.length; i++ )
			v[i] = gp.getProperty("branches."+branches[i]+".setup") == "true";
		return v;
	}
	
	public boolean[] getBoolMake() {
		if( branches == null ) { return null; }
		boolean[] v = new boolean[num_branches];
		for( int i = 0 ; i < v.length; i++ )
			v[i] = gp.getProperty("branches."+branches[i]+".make") == "true";
		return v;
	}
	
}
