package svn;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import gui.error.FatalError;
import vars.GlobalProperties;

/**
 * The {@code TortoiseHandler} handles Tortoise SVN commands as a sort of wrapper to
 * the {@link svn.Launcher Launcher} class mainly, but can be used anywhere else, since
 * it is given the proper parameters.
 * 
 * @author guidanoli
 *
 */
public class TortoiseHandler {

	protected String branchDir;
	
	/**
	 * Creates a Tortoise SVN Handler that operates on a certain branch directory
	 * @param branchDirectory - branch directory absolute path
	 */
	public TortoiseHandler(String branchDirectory)
	{
		assert(branchDirectory!=null);
		branchDir = branchDirectory;
	}
	
	/**
	 * Creates a Tortoise SVN Handler that operates on the default branch directory
	 */
	public TortoiseHandler()
	{
		this(GlobalProperties.gp.getProperty("path"));
	}
		
	public int getTargetLastRevisionNumber(String branchName)
	{
		return 0;
	}
	
	public int getLastRevisionNumber(String branchName)
	{
		Path fullPath = Paths.get(branchDir, branchName);
		File f = new File(fullPath.toString());
		if( !f.exists() ) return 0;
		return -1; // TODO: Get last revision number command
	}
	
	/**
	 * {@code public boolean isTortoiseDir(String branchName)}
	 * <p>Checks if the folder of name {@code branchName} has a .svn folder - that is -
	 * can be operated via Tortoise SVN functions.
	 * <p><b>Observation:</b> Does not prompt errors (that is, if branchName isn't a SVN Folder)
	 * @param branchName folder name from the branch directory absolute path
	 * @return true if the folder is indeed a Tortoise SVN directory
	 */
	public boolean isTortoiseDir(String branchName)
	{
		File f = openBranchFolder(branchName);
		if(f==null) return false;
		return !runCmd(branchName,false,"svn","info").equals("");
	}
	
	/**
	 * Creates a process that executes a shell command. It serves as a wrapper
	 * to many of the function of the {@link TortoiseHandler} class, dealing with
	 * input, output and error streams in a more abstract manner.
	 * @param dir - Full path to directory where the command will be executed
	 * @param error - true if when an error is found, it will be the returned string
	 * @param cmd - Command arguments
	 * @return Output stream of the command. If {@code error} is true, and an error
	 * has been encountered, the {@code return} value will be the error string itself.
	 * If a Java Exception is raised, but no error message has been streamed, {@code null}
	 * is returned instead.
	 */
	protected String runCmd(String branchName, boolean error, String... cmd)
	{	
		File f = openBranchFolder(branchName);
		if(f==null) return null;
		String dir = f.getAbsolutePath();
		StringBuilder errsb = new StringBuilder();
		try {  
	    	String line;
	    	StringBuilder sb = new StringBuilder();
	    	ProcessBuilder pb = new ProcessBuilder(cmd);
	    	pb.directory(new File(dir));
	    	Process p = pb.start();
	    	BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    	while ((line = input.readLine()) != null) {
	    		sb.append(line);  
	    	}
	    	input.close();
	    	if(error)
	    	{
	    		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	    		while ((line = stdError.readLine()) != null) {
	    			errsb.append(line);
	    		}
	    		stdError.close();
	    		String logMessage = errsb.toString();
    	  		if(!logMessage.equals("")) {
		    		FatalError.showLog(logMessage,null,false);
					return logMessage;
		    	}
			}
	    	return sb.toString();
	    }  
	    catch (Exception e) {
	    	FatalError.show(e,null,false);
	    	if(error)
	    	{
		    	String logMessage = errsb.toString();
		    	if(!logMessage.equals("")) {
		    		FatalError.showLog(logMessage,null,false);
					return logMessage;
		    	}
	    	}
	    	return null;
	    }
	}
	
	/**
	 * Creates a process that executes a shell command. It serves as a wrapper
	 * to many of the function of the {@link TortoiseHandler} class, dealing with
	 * input, output and error streams in a more abstract manner. Any error output
	 * will be simply ignored.
	 * @param dir - Full path to directory where the command will be executed
	 * @param cmd - Command arguments
	 * @return Output string given by the command or {@code null} if an error occurred
	 * @deprecated
	 */
	protected String runCmd(String dir, String... cmd) { return runCmd(dir,false,cmd); }
	
	/**
	 * {@code private File openBranchFolder(String branchName)}
	 * <p>Wrapper function that opens branch folder File object through its folder name
	 * @param branchName - branch folder name
	 * @return File object pointing to branch folder
	 */
	private File openBranchFolder(String branchName) {
		Path fullPath = Paths.get(branchDir, branchName);
		File f = new File(fullPath.toString());
		if( !f.exists() ) return null;
		return f;
	}
	
	public boolean isUpdated(String branchName)
	{
		return 	getTargetLastRevisionNumber(branchName) ==
				getLastRevisionNumber(branchName);
	}
	
	public void setup(String branchName)
	{
		// TODO: setup branch functionality
		Random r = new Random();
		try {
			TimeUnit.SECONDS.sleep(r.nextInt(5));
		} catch (InterruptedException e) {
			FatalError.show(e);
		}
	}
	
	public void make(String branchName)
	{
		// TODO: compile branch functionality
		Random r = new Random();
		try {
			TimeUnit.SECONDS.sleep(r.nextInt(5));
		} catch (InterruptedException e) {
			FatalError.show(e);
		}
	}
	
	public void getInfo(String branchName)
	{
		System.out.printf("isTortoiseSVN folder? %s\n", isTortoiseDir(branchName)?"yes":"no");
	}
	
}
