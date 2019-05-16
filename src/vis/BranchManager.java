package vis;
import java.io.File;
import java.io.FilenameFilter;

import vars.GlobalProperties;

public class BranchManager {
	
	public static final String KEY_BRANCHES = "branches";
	public static final String INDEX_LAST_SETUP = "lastsetup";
	public static final String INDEX_SETUP = "setup";
	public static final String INDEX_MAKE = "make";
	
	GlobalProperties gp;
	String[] branches;
	int num_branches;
	
	public BranchManager(GlobalProperties gp) {
		this.gp = gp;
	}
	
	public void launch() {
		// TODO: launcher
		Launcher launcher = new Launcher(this);
		System.out.println("Launched!");
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
			String prop = gp.getProperty(KEY_BRANCHES+"."+branches[i]+"."+INDEX_LAST_SETUP);
			v[i] = prop == null ? "Unknown" : prop;
		}
		return v;
	}
	
	public boolean[] getBoolSetup() {
		if( branches == null ) { return null; }
		boolean[] v = new boolean[num_branches];
		for( int i = 0 ; i < v.length; i++ ) {
			String b = gp.getProperty(KEY_BRANCHES+"."+branches[i]+"."+INDEX_SETUP);
			if( b == null ) v[i] = false; else v[i] = b.equals("true");
		}
		return v;
	}

	public void setBoolSetup(String branchName, boolean value) {
		String value_str = value ? "true" : "false";
		gp.setProperty(KEY_BRANCHES+"."+branchName+"."+INDEX_SETUP, value_str);
	}
	
	public boolean[] getBoolMake() {
		if( branches == null ) { return null; }
		boolean[] v = new boolean[num_branches];
		for( int i = 0 ; i < v.length; i++ )
		{
			String b = gp.getProperty(KEY_BRANCHES+"."+branches[i]+"."+INDEX_MAKE);
			if( b == null ) v[i] = false; else v[i] = b.equals("true");
		}
		return v;
	}
	
	public void setBoolMake(String branchName, boolean value) {
		String value_str = value ? "true" : "false";
		gp.setProperty(KEY_BRANCHES+"."+branchName+"."+INDEX_MAKE, value_str);
	}
	
}
