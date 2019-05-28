package svn;
import java.io.File;
import java.io.FilenameFilter;

import vars.GlobalProperties;

public class BranchManager {
	
	public static final String KEY_BRANCHES = "branches";
	public static final String INDEX_LAST_SETUP = "lastsetup";
	public static final String INDEX_SETUP = "setup";
	public static final String INDEX_MAKE = "make";
	public static final String INDEX_SETUP_STATUS = "setup_s";
	public static final String INDEX_MAKE_STATUS = "make_s";
	
	GlobalProperties gp;
	String[] branches;
	int num_branches;
	
	public BranchManager(GlobalProperties gp) {
		this.gp = gp;
	}
	
	public String[] getBranchNames() {
		String path = gp.getProperty("path");
		if( path == null ) return null;
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
		if( branches == null ) return null;
		String[] v = new String[num_branches];
		for( int i = 0 ; i < v.length; i++ )
		{
			String prop = gp.getProperty(KEY_BRANCHES+"."+branches[i]+"."+INDEX_LAST_SETUP);
			v[i] = prop == null ? "Unknown" : prop;
		}
		return v;
	}
	
	public boolean[] getBoolSetup() {
		if( branches == null ) return null;
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
		if( branches == null ) return null;
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
	
	public int[] getStatusSetup() {
		if( branches == null ) return null;
		int[] v = new int[num_branches];
		for( int i = 0 ; i < v.length; i++ )
		{
			String s = gp.getProperty(KEY_BRANCHES+'.'+branches[i]+'.'+INDEX_SETUP_STATUS);
			if( s == null ) v[i] = 0; else v[i] = Integer.parseInt(s);
		}
		return v;
	}
	
	public void setStatusSetup(String branchName, int value) {
		if( value == 0 ) return;
		String value_str = Integer.toString(value);
		gp.setProperty(KEY_BRANCHES+'.'+branchName+'.'+INDEX_SETUP_STATUS, value_str);
	}

	public int[] getStatusMake() {
		if( branches == null ) return null;
		int[] v = new int[num_branches];
		for( int i = 0 ; i < v.length; i++ )
		{
			String s = gp.getProperty(KEY_BRANCHES+'.'+branches[i]+'.'+INDEX_MAKE_STATUS);
			if( s == null ) v[i] = 0; else v[i] = Integer.parseInt(s);
		}
		return v;
	}
	
	public void setStatusMake(String branchName, int value) {
		if( value == 0 ) return;
		String value_str = Integer.toString(value);
		gp.setProperty(KEY_BRANCHES+'.'+branchName+'.'+INDEX_MAKE_STATUS, value_str);
	}
}
