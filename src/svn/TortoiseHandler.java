package svn;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

import gui.error.FatalError;
import gui.error.LightError;
import vars.GlobalProperties;
import vars.Language;

/**
 * <p>The {@code TortoiseHandler} handles Tortoise SVN commands as a sort of wrapper to
 * the {@link svn.Launcher Launcher} class mainly, but can be used anywhere else, since
 * it is given the proper parameters.
 * 
 * @author guidanoli
 *
 */
public class TortoiseHandler {

	protected ArrayList<Process> runningProcesses = new ArrayList<Process>();
	protected String branchDir;
	private Language lang = Language.getInstance();
	
	/**
	 * <p>Creates a Tortoise SVN Handler that operates on a certain branch directory
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
		this(GlobalProperties.getInstance().getProperty("path"));
	}
	
	/**
	 * <p>{@code public boolean isTortoiseDir(String branchName)}
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
		String output = runCmd(f,false,true,false, "svn", "info");
		return !(output == null || output.equals(""));
	}
	
	/**
	 * <p>{@code public long getRevisionNumber(String branchName)}
	 * <p>Gets last changed revision from local copy. Its effect is the same of the
	 * 'svn info --show-item last-changed-revision' SVN command executed from any
	 * branch's root folder.
	 * <p>If it could not execute the command, an error message will be prompted,
	 * not forcing the application to be terminated.
	 * @param branchName - the name of the branch folder
	 * @return local copy revision number or -1 if command was unsuccessful
	 */
	public long getRevisionNumber(String branchName)
	{
		File f = openBranchFolder(branchName);
		if(f==null) LightError.show(lang.get("gui_errmsg_nobranchrootfolder"));
		String output = runCmd(f,true,true,false,"svn","info","--show-item", "last-changed-revision");
		if( output == null )
		{
			LightError.show(lang.get("gui_errmsg_revnumbercmdfailed"));
			return -1; // unsuccessful command
		}
		try {
			return Long.parseLong(output);
		}
		catch(NumberFormatException e)
		{
			return -1; // invalid output
		}
	}
	
	/**
	 * <p>{@code String runCmd(File dir, boolean error, boolean constinput, String... cmd)}
	 * <p>Creates a process that executes a shell command. It serves as a wrapper
	 * to many of the function of the {@link TortoiseHandler} class, dealing with
	 * input, output and error streams in a more abstract manner.
	 * @param dir - File object to directory where the command will be executed
	 * @param promptError - if {@code true}, on any error output, an error dialog will
	 * be prompted to the user with the error message along with it
	 * @param outputError - if {@code true}, on any error output, the method will return
	 * the error message instead of output of any other type
	 * @param constinput - if {@code true}, constantly input data. Generally used for
	 * Batch jobs that request keys to be pressed in order to exit. The input has no
	 * meaningful data and serves only to this very job.
	 * @param cmd - Command arguments
	 * @return Output stream of the command. If {@code error} is true, and an error
	 * has been encountered, the {@code return} value will be the error string itself.
	 * If a Java Exception is raised, but no error message has been streamed, {@code null}
	 * is returned instead.
	 */
	protected String runCmd(File dir, boolean promptError, boolean outputError, boolean constinput, String... cmd) { 
    	StringBuilder sb = new StringBuilder();
		StringBuilder errsb = new StringBuilder();
		try {  
	    	String line;
	    	ProcessBuilder pb = new ProcessBuilder(cmd);
	    	pb.directory(dir);
	    	Process p = pb.start();
	    	runningProcesses.add(p);
	    	BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    	while ((line = input.readLine()) != null) {
	    		sb.append(line);
	    	}
	    	input.close();
	    	if(promptError || outputError)
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
	    }  
	    catch (Exception e) {
	    	FatalError.show(e,null,false);
	    	if(promptError || outputError)
	    	{
		    	String logMessage = errsb.toString();
		    	if(!logMessage.equals("")) {
		    		FatalError.showLog(logMessage,null,false);
		    		if( outputError )
		    			return logMessage;
		    	}
	    	}
	    }
    	return sb.toString();
	}
	
	/**
	 * <p>{@code String runLua(File dir, boolean error, String luaFilePath, String... args)}
	 * <p>Runs Lua scripts making use of the lua5posix executable that should be on the
	 * binaries folder of every SVN branch folder.
	 * <p><b>Observations:</b>
	 * <ul>
	 * <li> Be aware that this will not work in Linux Operating Systems since
	 * .exe files are Windows Operating Systems exclusive executable formats. </li>
	 * <li> The absence of the lua5posix executable in the binaries folder will
	 * surely cause the ineffectiveness of this function. Thus, if this executable
	 * gets moved to another folder or is simply missing, errors messages will always
	 * be thrown.</li>
	 * </ul>
	 * <p>Example of usage:
	 * <p><b>{@code String output = runLua(f, true, "bin/vis.lua", "s");}</b>
	 * <p>Note that {@code f} stands for a file object that points to the branch folder and is
	 * valid. The lua file path, unlike the simple example above, should be safely formatted by
	 * the {@link Paths.get} function, for the operating system might differ file separators.
	 * @param dir - file object that points to the directory of a certain branch 
	 * @param error - if {@code true}, outputs error instead of standard output, if
	 * there is any error caught from the standard error stream.
	 * @param luaFilePath - lua script relative path from branch directory path
	 * @param args - list of all arguments provided to the lua script
	 * @return output or error string (see <b>error</b> parameter)
	 * @see TortoiseHandler#openBranchFolder(String)
	 * @see Paths.get
	 */
	protected String runLua(File dir, boolean error, String luaFilePath, String... args) {
		StringBuilder errsb = new StringBuilder();
		try {  
	    	String line;
	    	StringBuilder sb = new StringBuilder();
	    	
	    	/* source directory, lua file and lua5posix executable path */
	    	File srcDir = new File(Paths.get(dir.getAbsolutePath(),"src").toString());
	    	String fullLuaFilePath = Paths.get(srcDir.getAbsolutePath(),"..",luaFilePath).toString();
	    	String lua5posixPath = Paths.get(srcDir.getAbsolutePath(),"..","bin", "lua5posix.exe").toString();
	    	
	    	/* building command string array */
	    	String [] cmd = new String[args.length+2];
	    	cmd[0] = lua5posixPath;
	    	cmd[1] = fullLuaFilePath;
	    	for(int i = 0; i < args.length; i++) cmd[i+2] = args[i];
	    	
	    	/* building process */
	    	ProcessBuilder pb = new ProcessBuilder(cmd);
	    	Map<String, String> env = pb.environment();
	    	env.put("CVS_RSH", "ssh");
	    	pb.directory(srcDir);
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
	 * {@code private File openBranchFolder(String branchName)}
	 * <p>Wrapper function that opens the branch folder through its folder name,
	 * loading it to a File object.
	 * @param branchName - branch folder name
	 * @return File object pointing to branch folder
	 */
	private File openBranchFolder(String branchName) {
		return openBranchFolder(branchName,"");
	}
	
	/**
	 * {@code private File openBranchFolder(String branchName)}
	 * <p>Wrapper function that opens a subfolder in a branch folder through its folder name,
	 * loading it to a File object.
	 * @param branchName - branch folder name
	 * @return File object pointing to the subfolder in the branch folder
	 */
	private File openBranchFolder(String branchName, String subfolder) {
		Path fullPath = Paths.get(branchDir, branchName,subfolder);
		File f = new File(fullPath.toString());
		if( !f.exists() ) return null;
		return f;
	}
	
	/**
	 * <p>{@code void setup(String branchName)}
	 * <p>Sets up the branch. Its effect is the same of the './vis s' batch job executed from any
	 * branch's source folder.
	 * <p>If it could not set up the branch, an error message will be prompted, not forcing the
	 * application to be terminated.
	 * @param branchName - the name of the branch folder
	 * @return {@code true} on success and {@code false} on error
	 */
	public boolean setup(String branchName)
	{
		File f = openBranchFolder(branchName);
		if(f==null) FatalError.show(lang.get("gui_errmsg_nobranchrootfolder")); //exits
		String setupLuaPath = Paths.get("bin", "vis.lua").toString(); 
		String output = runLua(f, true, setupLuaPath, "s");
		if(output==null || output.equals(""))
		{
			LightError.show(lang.format("gui_errmsg_failedsetup", branchName));
			return false;
		}
		return true;
	}
	
	/**
	 * <p>{@code void setup(String branchName)}
	 * <p>Compiles the branch. Its effect is the same of the './vis mlldamt' batch job executed from any
	 * branch's source folder.
	 * <p>If it could not set up the branch, an error message will be prompted, not forcing the
	 * application to be terminated.
	 * @param branchName - the name of the branch folder
	 * @return {@code true} on success and {@code false} on error
	 */
	public boolean make(String branchName)
	{
		File f = openBranchFolder(branchName);
		if(f==null) FatalError.show(lang.get("gui_errmsg_nobranchrootfolder")); //exits
		String setupLuaPath = Paths.get("bin", "vis.lua").toString(); 
		String output = runLua(f, true, setupLuaPath, "mlldamt");
		if(output==null || output.equals(""))
		{
			LightError.show(lang.format("gui_errmsg_failedmake", branchName));
			return false;
		}
		
		return true;
	}
	
	/**
	 * <p>{@code public boolean cleanUp(String branchName, int persistence)}
	 * <p>Cleans up the branch. Its effect is the same of the './vis clean' batch job executed from any
	 * branch's source folder. 
	 * <p>If it could not clean up the branch, an error message will be prompted, not forcing the
	 * application to be terminated.
	 * @param branchName - the name of the branch folder
	 * @param persistence - maximum number of tries until clean up job runs without errors
	 * @return {@code true} if successful, {@code false} if failed all #persistence times
	 */
	public boolean cleanUp(String branchName, int persistence)
	{
		File f = openBranchFolder(branchName);
		if(f==null) LightError.show(lang.get("gui_errmsg_nobranchrootfolder"));
		for(int i = 0 ; i < persistence ; i++) {
			String output = runCmd(f,false,true,false, "svn", "cleanup");
			if( output.equals("") ) return true;
		}
		return false;
	}
	
	/**
	 * <p>{@code public static void killProcesses}
	 * <p>Kills all currently running processes
	 * <p>May be unsafe and leave objects in an unstable state
	 */
	public void killProcesses()
	{
		for( Process p : runningProcesses )
			if( p.isAlive() ) p.destroy();
		runningProcesses.clear();
	}
	
}
