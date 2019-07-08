package svn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringJoiner;

import gui.error.FatalError;
import gui.error.LightError;
import vars.Language;
import vars.LocalResources;

/**
 * The {@code LauncherLog} class is intended to handle and store log data
 * about previous jobs (setup, makes...) to the log file. It appends each
 * new action to the log file along with a time stamp, the branch name and,
 * optionally, additional parameters related to the action.
 * 
 * @author guidanoli
 * @see {@link Launcher}
 */
public class LauncherLogManager {

	private Language lang = Language.getInstance();
	private String branchName;
	private static String logFile = LocalResources.launchlog;
		
	/**
	 * Constructs a launcher log for a branch
	 * @param branchName - the branch folder name
	 */
	public LauncherLogManager(String branchName) {
		this.branchName = branchName;
	}
	
	/**
	 * Get most recent time stamp attached to branch registered
	 * in log, for any action.
	 * @return time stamp
	 */
	public long getLastSetupMillis() {
		ArrayList<String []> data = readLog();
		if( data == null ) return 0;
		Iterator<String []> iterator = data.iterator();
		long timeStamp = 0;
		while( iterator.hasNext() )
		{
			String [] registry = iterator.next();
			String branch = registry[0];
			if( !branch.equals(branchName) ) continue;
			if( registry.length < 3 )
			{
				LightError.show(lang.get("gui_errmsg_launcher_log_badformat"));
				break;
			}
			try {
				long ts = Long.parseLong(registry[1]);
				if( ts > timeStamp ) timeStamp = ts;
			}
			catch( Exception e )
			{
				LightError.show(e);
				break;
			}
		}
		return timeStamp;
	}
	
	/**
	 * <p>Reads log file (if exists) and returns its data as an array list
	 * with string arrays. The data is ordered as such:
	 * <ol>
	 * <li>branch name</li>
	 * <li>time stamp</li>
	 * <li>action identifier</li>
	 * <li>additional action arguments</li>
	 * </ol>
	 * @return
	 */
	public static ArrayList<String []> readLog() {
		ArrayList<String []> info = new ArrayList<String []>();
		File file = new File(logFile);
		try {
			if ( file.createNewFile() ) return info;
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s;
			while((s=br.readLine()) != null)
			{
				String [] values = s.split("\\s+");
				info.add(values);
			}
			br.close();
		} catch (Exception e) {
			FatalError.show(e,null,false); // does not quit
			return null;
		}
		return info;
	}
	
	/**
	 * Logs a setup action alongside with the old revision
	 * number and the new revision number. If equal, log
	 * won't happen.
	 * @param oldRevision - old revision number
	 * @param newRevision - new revision number
	 * @see TortoiseHandler
	 */
	public void logSetup(Long oldRevision, Long newRevision)
	{
		if( oldRevision.equals(newRevision) ) return;
		register("setup",oldRevision,newRevision);
	}
	
	/**
	 * Logs a compilation job.
	 * @param timeElapsed time elapsed to compile
	 * @see TortoiseHandler
	 */
	public void logMake(long timeElapsed)
	{
		register("make",timeElapsed);
	}

	/**
	 * <p>Adds a log registry with the following information:
	 * <ul>
	 * <li>branch name</li>
	 * <li>time stamp</li>
	 * <li>action identifier</li>
	 * <li>additional action arguments</li>
	 * </ul>
	 * <p>If occurs an IO error, the program will warn the user
	 * with an error dialog but will not terminate the program
	 * since it could be running sensitive procedures during the
	 * log registry.
	 * @param action - action identifier
	 * @param args - additional action arguments (whose toString()
	 * methods will be called to be written on the log)
	 * @see LauncherLogManager#logMake(long) logMake(long)
	 * @see LauncherLogManager#logSetup(Long, Long) logSetup(Long, Long)
	 */
	private void register(String action, Object... args)
	{
		String timestamp = Long.toString(System.currentTimeMillis());
		StringJoiner strj = new StringJoiner(" ");
		strj.add(branchName)
			.add(timestamp)
			.add(action);
		for( Object arg : args ) strj.add(arg.toString());
		String logEntry = strj.toString();
		if( !assertLogFile() ) return;
		try {
			BufferedWriter writer = new BufferedWriter(
				new FileWriter(logFile, true) // true for 'append' mode
			);
			writer.write(logEntry);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			FatalError.show(lang.get("gui_errmsg_launcher_log_registerfailed"), null, false); // does not exit
		}
	}
	
	/**
	 *  Creates log file if inexistent. Warns the user with an error dialog if unsuccessful,
	 *  but does not terminate the program itself for safety reasons (The launcher could still
	 *  be running some sensitive procedure during the log registry, for example).
	 *  @return {@code true} if successful.
	 */
	private boolean assertLogFile()
	{
		new File(LocalResources.datafolder).mkdirs();
		try {
			new File(logFile).createNewFile();
		} catch (IOException e) {
			FatalError.show(lang.get("gui_errmsg_launcher_log_openfailed"),null,false); // does not exit
			return false;
		}
		return true;
	}
	
}
