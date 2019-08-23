package svn;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

import gui.error.FatalError;
import gui.error.LightError;
import svn.error.ErrorListener;
import svn.error.MakeErrorListener;
import svn.error.SetupErrorListener;
import vars.Language;
import vars.properties.GlobalProperties;

/**
 * <p>The <code>TortoiseHandler</code> handles Tortoise SVN commands as a sort of wrapper to
 * the {@link svn.Launcher Launcher} class mainly, but can be used anywhere else, since
 * it is given the proper parameters.
 * 
 * @author guidanoli
 *
 */
public class TortoiseHandler {

	public static final String [] makeCommands = {
			"mlld",
			"mlldmt",
			"mllda",
			"mlldamt",
			"mlldad",
			"mlldadmt"
	};
		
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
		this(GlobalProperties.getInstance().get("path"));
	}
	
	/**
	 * <p><code>public boolean isTortoiseDir(String branchName)</code>
	 * <p>Checks if the folder of name <code>branchName</code> has a .svn folder - that is -
	 * can be operated via Tortoise SVN functions.
	 * <p><b>Observation:</b> Does not prompt errors (that is, if branchName isn't a SVN Folder)
	 * @param branchName folder name from the branch directory absolute path
	 * @return true if the folder is indeed a Tortoise SVN directory
	 */
	public boolean isTortoiseDir(String branchName)
	{
		File f = openBranchFolder(branchName);
		if(f==null) return false;
		String output = runCmd(f,false,true,"svn", "info");
		return !(output == null || output.equals(""));
	}
	
	/**
	 * <p><code>public long getRevisionNumber(String branchName)</code>
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
		String output = runCmd(f,true,true,"svn","info","--show-item","last-changed-revision");
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
	 * <p>Creates a process that executes a shell command. It serves as a facade
	 * to many of the function of the {@link TortoiseHandler} class, dealing with
	 * input, output and error streams.
	 * @param dir - File object to directory where the command will be executed
	 * @param promptError - if <code>true</code>, on any error output, an error dialog will
	 * be prompted to the user with the error message along with it
	 * @param outputError - if <code>true</code>, on any error output, the method will return
	 * the error message instead of output of any other type
	 * @param cmd - Command arguments
	 * @return Output stream of the command. If <code>error</code> is true, and an error
	 * has been encountered, the <code>return</code> value will also contain the error output string.
	 * If a Java Exception is raised, but no error message has been streamed, <code>null</code>
	 * is returned instead.
	 */
	protected String runCmd(File dir, boolean promptError, boolean outputError, String... cmd) { 
    	StringJoiner sj = new StringJoiner("\n");
    	StringJoiner errsj = new StringJoiner("\n");
		try {  
	    	String line;
	    	ProcessBuilder pb = new ProcessBuilder(cmd);
	    	pb.directory(dir);
	    	Process p = pb.start();
	    	runningProcesses.add(p);
	    	BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    	while ((line = input.readLine()) != null) {
	    		sj.add(line);
	    	}
	    	input.close();
	    	if(promptError || outputError)
	    	{
	    		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	    		while ((line = stdError.readLine()) != null) {
	    			errsj.add(line);
	    		}
	    		stdError.close();
	    		String errout = errsj.toString();
	    		String stdout = sj.toString();
    	  		if(!errout.equals("") && promptError) {
    	    		String logMessage = formatLogMessage(stdout, errout);
		    		FatalError.showLog(logMessage,null,false);
					return logMessage;
		    	}
			}
	    }  
	    catch (Exception e) {
	    	FatalError.show(e,null,false);
	    	if(promptError || outputError)
	    	{
		    	String logMessage = formatLogMessage(sj.toString(), errsj.toString());
		    	if(!logMessage.equals("") && promptError) {
		    		FatalError.showLog(logMessage,null,false);
		    		if( outputError )
		    			return logMessage;
		    	}
	    	}
	    }
    	return sj.toString();
	}
	
	/**
	 * <p><code>boolean runLua(File dir, boolean error, String luaFilePath, String... args)</code>
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
	 * <p><b><code>String output = runLua(f, true, "bin/vis.lua", "s");</code></b>
	 * <p>Note that <code>f</code> stands for a file object that points to the branch folder and is
	 * valid. The lua file path, unlike the simple example above, should be safely formatted by
	 * the {@link Paths.get} function, for the operating system might differ file separators.
	 * @param dir - file object that points to the directory of a certain branch 
	 * @param errorListener - {@link ErrorListener}
	 * @param luaFilePath - lua script relative path from branch directory path
	 * @param args - list of all arguments provided to the lua script
	 * @return <code>true</code> if no errors occurred
	 * @see TortoiseHandler#openBranchFolder(String)
	 * @see Paths.get
	 */
	protected boolean runLua(File dir, ErrorListener errorListener, String luaFilePath, String... args) {
		StringJoiner sj = new StringJoiner("\n");
    	StringJoiner errsj = new StringJoiner("\n");
		try {  
	    	String line;
	    	
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
	    		sj.add(line);
	    	}
	    	input.close();
	    	if(errorListener.isHandling())
	    	{
	    		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	    		while ((line = stdError.readLine()) != null) {
	    			errsj.add(line);
	    		}
	    		stdError.close();
	    		String logMessage = formatLogMessage(sj.toString(), errsj.toString());
    	  		if(!logMessage.equals("")) {
		    		boolean ok = errorListener.handleErrorOutput(logMessage);
		    		if( !ok ) FatalError.showLog(logMessage, null, false);
		    		return ok;
		    	}
			}
	    	return true;
	    }  
	    catch (Exception e) {
	    	// Show Java error
//	    	FatalError.show(e,null,false);
	    	// but don't forget to show Lua error too
	    	if(errorListener.isHandling())
	    	{
		    	String logMessage = formatLogMessage(sj.toString(), errsj.toString());
		    	if(!logMessage.equals("")) {
		    		boolean ok = errorListener.handleErrorOutput(logMessage);
		    		if( !ok ) FatalError.showLog(logMessage, null, false);
		    		return ok;
		    	}
	    	}
	    	return true;
	    }
	}
	
	private String formatLogMessage(String stdout, String stderr) {
		return String.format("Error messages:\n%s\nFull output:\n%s", stderr, stdout);
	}
	
	/**
	 * <code>private File openBranchFolder(String branchName)</code>
	 * <p>Wrapper function that opens the branch folder through its folder name,
	 * loading it to a File object.
	 * @param branchName - branch folder name
	 * @return File object pointing to branch folder
	 */
	private File openBranchFolder(String branchName) {
		return openBranchFolder(branchName,"");
	}
	
	/**
	 * <code>private File openBranchFolder(String branchName)</code>
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
	 * <p><code>void setup(String branchName)</code>
	 * <p>Sets up the branch. Its effect is the same of the './vis s' batch job executed from
	 * the branch source folder.
	 * <p>If it could not set up the branch, an error message will be prompted, but not forcing the
	 * application to be terminated.
	 * @param branchName - the name of the branch folder
	 * @return <code>true</code> on success and <code>false</code> on error
	 */
	public boolean setup(String branchName, SetupErrorListener listener)
	{
		File f = openBranchFolder(branchName);
		if(f==null) FatalError.show(lang.get("gui_errmsg_nobranchrootfolder")); //exits
		String setupLuaPath = Paths.get("bin", "vis.lua").toString();
		return runLua(f, listener, setupLuaPath, "s");
	}
	
	/**
	 * <p><code>void make(String branchName)</code>
	 * <p>Compiles the branch. Its effect is the same of the './vis <make command>' batch job executed from
	 * the branch source folder.
	 * <p>If it could not set up the branch, an error message will be prompted, but not forcing the
	 * application to be terminated.
	 * @param branchName - the name of the branch folder
	 * @return <code>true</code> on success and <code>false</code> on error
	 */
	public boolean make(String branchName, MakeErrorListener listener)
	{
		File f = openBranchFolder(branchName);
		if(f==null) FatalError.show(lang.get("gui_errmsg_nobranchrootfolder")); //exits
		String setupLuaPath = Paths.get("bin", "vis.lua").toString();
		String command = GlobalProperties.getInstance().get("makecmd");
		return runLua(f, listener, setupLuaPath, command);
	}
	
	/**
	 * <p><code>public boolean cleanUp(String branchName, int persistence)</code>
	 * <p>Cleans up the branch. Its effect is the same of the './vis clean' batch job executed from the
	 * branch source folder. 
	 * <p>If it could not clean up the branch after all the tried, an error message will be prompted,
	 * but not forcing the application to be terminated.
	 * @param branchName - the name of the branch folder
	 * @param persistence - maximum number of tries until clean up job runs without errors
	 * @return <code>true</code> on success and <code>false</code> if failed all #persistence times
	 */
	public boolean cleanUp(String branchName, int persistence)
	{
		File f = openBranchFolder(branchName);
		if(f==null) LightError.show(lang.get("gui_errmsg_nobranchrootfolder"));
		for(int i = 0 ; i < persistence ; i++) {
			boolean lastCleanUp = i == persistence - 1;
			String output = runCmd(f,lastCleanUp,true,"svn", "cleanup");
			if( output.equals("") ) return true;
		}
		return false;
	}
	
	/**
	 * <p><code>public static void killProcesses()</code>
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
