package svn;
import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import gui.error.FatalError;
import vars.GlobalProperties;
import vars.Language;

/**
 * <p>The {@code BranchManager} class intends to centralize any operation dealing
 * with branches, their history and their current state on the application.
 * <p><b>It does not directly operate them</b>. It only sets and gets local
 * information stored as {@link vars.GlobalProperties GlobalProperties}, in a
 * very abstract manner. 
 * <p>Every array returned by the getter methods in this class are perfectly
 * aligned. That is, given the same context, must have the same length, and
 * have the same relationship for a given index.
 * <p>Every branchName parameter by the setter methods in this class is related
 * to the same set of strings that are provided by the {@link BranchManager#getBranchNames
 * getBranchNames} method.
 * @author guidanoli
 * @see vars.GlobalProperties GlobalProperties
 */
public class BranchManager {
	
	public static final String KEY_BRANCHES = "branches";
	public static final String INDEX_LAST_SETUP = "lastsetup";
	public static final String INDEX_SETUP = "setup";
	public static final String INDEX_MAKE = "make";
	public static final String INDEX_SETUP_STATUS = "setup_s";
	public static final String INDEX_MAKE_STATUS = "make_s";
	
	private Language lang = Language.getInstance();
	private GlobalProperties gp;
	private String[] branches;
	private int num_branches;
	
	/**
	 * <p>Constructs a Branch Manager instance
	 * @param gp - global properties
	 * @deprecated
	 * @see #BranchManager()
	 */
	public BranchManager(GlobalProperties gp) {
		this.gp = gp;
	}
	
	public BranchManager() { this(GlobalProperties.getInstance()); }
	
	/**
	 * <p>Gets the name of every folder on the
	 * globally set property 'path'. It does not
	 * test if said folder is a Tortoise SVN controlled
	 * folder or not. This task is left to the
	 * {@link svn.TortoiseHandler#isTortoiseDir(String)
	 * isTortoiseDir} method.
	 * @return array of branch names
	 * @see svn.TortoiseHandler#isTortoiseDir(String) isTortoiseDir
	 */
	public String[] getBranchNames() {
		String path = gp.get("path");
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
	
	/**
	 * <p>Gets the date of the last setup made on
	 * each branch. If not a single setup in said
	 * branch has been recorded, a dummy value set
	 * by the {@link vars.Language Language} class
	 * will fill the gap.
	 * <p>The date is formatted depending on the
	 * current language. Each language has a said
	 * Locale value that interprets Date format
	 * differently and is set by the language.
	 * @return array of last branch setup dates
	 * @see vars.Language Language
	 */
	public String[] getLastSetupDates() {
		if( branches == null ) return null;
		String[] v = new String[num_branches];
		for( int i = 0 ; i < v.length; i++ )
		{
			String prop = gp.get(KEY_BRANCHES,branches[i],INDEX_LAST_SETUP);
			if( prop == null )
			{
				v[i] = lang.get("gui_branchtable_defval_lastsetup");
				continue;
			}
			try {
				long timestamp = Long.parseLong(prop);
				v[i] = getDateString(timestamp);
			}
			catch( Exception e )
			{
				FatalError.show(e); // error while parsing bad format "Long"
			}
		}
		return v;
	}
	
	/**
	 * <p>Sets last setup date to a specific branch
	 * @param branchName - name of branch folder
	 * @param timestamp - time stamp given by the
	 * {@link System.currentTimeMillis} function
	 */
	public void setLastSetupDate(String branchName, long timestamp) {
		String value_str = Long.toString(timestamp);
		gp.set(value_str,KEY_BRANCHES,branchName,INDEX_LAST_SETUP);
	}
	
	/**
	 * <p>Gets boolean corresponding to whether a given
	 * branch is scheduled to be set up by the Launcher.
	 * @return array of booleans (i-th branch will be set up?)
	 */
	public boolean[] getBoolSetup() {
		if( branches == null ) return null;
		boolean[] v = new boolean[num_branches];
		for( int i = 0 ; i < v.length; i++ ) {
			String b = gp.get(KEY_BRANCHES,branches[i],INDEX_SETUP);
			if( b == null ) v[i] = false; else v[i] = b.equals("true");
		}
		return v;
	}

	/**
	 * <p>Sets boolean corresponding to whether a given
	 * branch is scheduled to be set up by the Launcher.
	 * @param branchName - name of the branch folder
	 * @param value - new boolean value 
	 */
	public void setBoolSetup(String branchName, boolean value) {
		String value_str = value ? "true" : "false";
		gp.set(value_str,KEY_BRANCHES,branchName,INDEX_SETUP);
	}
	
	/**
	 * <p>Gets boolean corresponding to whether a given
	 * branch is scheduled to be compiled by the Launcher.
	 * @return array of booleans (i-th branch will be compiled?)
	 */
	public boolean[] getBoolMake() {
		if( branches == null ) return null;
		boolean[] v = new boolean[num_branches];
		for( int i = 0 ; i < v.length; i++ )
		{
			String b = gp.get(KEY_BRANCHES,branches[i],INDEX_MAKE);
			if( b == null ) v[i] = false; else v[i] = b.equals("true");
		}
		return v;
	}
	
	/**
	 * <p>Sets boolean corresponding to whether a given
	 * branch is scheduled to be compiled by the Launcher.
	 * @param branchName - name of the branch folder
	 * @param value - new boolean value 
	 */
	public void setBoolMake(String branchName, boolean value) {
		String value_str = value ? "true" : "false";
		gp.set(value_str,KEY_BRANCHES,branchName,INDEX_MAKE);
	}
	
	/**
	 * <p>Gets the status of a given branch in relation to
	 * its set up. The possible values are determined by the
	 * {@link svn.LaunchProgressListener LaunchProgressListener}
	 * public integers. 
	 * @return array of setup status
	 */
	public int[] getStatusSetup() {
		if( branches == null ) return null;
		int[] v = new int[num_branches];
		for( int i = 0 ; i < v.length; i++ )
		{
			String s = gp.get(KEY_BRANCHES,branches[i],INDEX_SETUP_STATUS);
			if( s == null ) v[i] = 0; else v[i] = Integer.parseInt(s);
		}
		return v;
	}
	
	/**
	 * <p>Sets the status of a given branch in relation to
	 * its set up. The possible values are determined by the
	 * {@link svn.LaunchProgressListener LaunchProgressListener}
	 * public integers.
	 * @param branchName - name of the branch folder
	 * @param value - new status value
	 */
	public void setStatusSetup(String branchName, int value) {
		if( value == 0 ) return;
		String value_str = Integer.toString(value);
		gp.set(value_str,KEY_BRANCHES,branchName,INDEX_SETUP_STATUS);
	}

	/**
	 * <p>Gets the status of a given branch in relation to
	 * its compilation. The possible values are determined by the
	 * {@link svn.LaunchProgressListener LaunchProgressListener}
	 * public integers. 
	 * @return array of compilation status
	 */
	public int[] getStatusMake() {
		if( branches == null ) return null;
		int[] v = new int[num_branches];
		for( int i = 0 ; i < v.length; i++ )
		{
			String s = gp.get(KEY_BRANCHES,branches[i],INDEX_MAKE_STATUS);
			if( s == null ) v[i] = 0; else v[i] = Integer.parseInt(s);
		}
		return v;
	}
	
	/**
	 * <p>Sets the status of a given branch in relation to
	 * its compilation. The possible values are determined by the
	 * {@link svn.LaunchProgressListener LaunchProgressListener}
	 * public integers.
	 * @param branchName - name of the branch folder
	 * @param value - new status value
	 */
	public void setStatusMake(String branchName, int value) {
		if( value == 0 ) return;
		String value_str = Integer.toString(value);
		gp.set(value_str,KEY_BRANCHES,branchName,INDEX_MAKE_STATUS);
	}
	
	private String getDateString(long timeStamp) {
		Locale locale = new Locale(lang.get("meta_locale"));
		String dateFormat = lang.get("meta_dateformat");
		Date date = new Date(timeStamp);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat,locale);
		return sdf.format(date);
	}
	
	
}
