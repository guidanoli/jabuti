package vis;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Properties;

public class BranchManager {
	
	Properties prop;
	String[] branches;
	int num_branches;
	
	public BranchManager(Properties prop) {
		this.prop = prop;
	}
	
	public String[] getBranchNames() {
		String path = prop.getProperty("path");
		if( path == null ) { return null; }
		File file = new File(path);
		branches = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		num_branches = branches.length;
		return branches;
	}
	
	public String[] getLastSetupDates() {
		if( branches == null ) { return null; }
		String[] v = new String[num_branches];
		for( int i = 0 ; i < v.length; i++ )
			v[i] = prop.getProperty("branches."+branches[i]+".lastsetupdate");
		return v;
	}
	
	public boolean[] getBoolSetup() {
		if( branches == null ) { return null; }
		boolean[] v = new boolean[num_branches];
		for( int i = 0 ; i < v.length; i++ )
			v[i] = prop.getProperty("branches."+branches[i]+".setup") == "true";
		return v;
	}
	
	public boolean[] getBoolMake() {
		if( branches == null ) { return null; }
		boolean[] v = new boolean[num_branches];
		for( int i = 0 ; i < v.length; i++ )
			v[i] = prop.getProperty("branches."+branches[i]+".make") == "true";
		return v;
	}
	
}
