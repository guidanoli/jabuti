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
import vars.properties.GlobalProperties;

/**
 * The <code>LauncherLog</code> class is intended to handle and store log data
 * about previous jobs (setup, makes...) to the log file. It appends each
 * new action to the log file along with a time stamp, the branch name and,
 * optionally, additional parameters related to the action.
 * 
 * @author guidanoli
 * @see {@link Launcher}
 */
public class LauncherLogManager {

	private static Language lang = Language.getInstance();
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
	 * <p>Restrains log to a maximum file size and, and if it surpasses that
	 * threshold, reduces the log size to a percentage so that:
	 * <p><code>new size = maximum size * percentage</code>
	 * <p>The number of entries that will be deducted is roughly estimated by
	 * the current log file size and the current number of entries.
	 * @param maxLogSize - maximum log file size <b>in bytes</b> ( > 0 )
	 * @param percentage - % of file size after reduction, between 0 and 1
	 * @return <code>true</code> if log has been reduced, or <code>false</code> if else.
	 */
	public static boolean restrainLogSize(long maxLogSize, float percentage) {
		ArrayList<String []> logArray = readLog();
		if( logArray == null ) return false;
		int entriesCount = logArray.size();
		File f = new File(logFile);
		long fileSize = f.length();
		if( fileSize < maxLogSize ) return false;
		int maxEntriesCount = (int) (entriesCount * ((double) maxLogSize / (double) fileSize));
		return restrainLogEntriesCount(maxEntriesCount, percentage);
	}
		
	/**
	 * <p>Restrains log to a maximum number of entries and, and if it surpasses that
	 * threshold, reduces the log size to a percentage so that:
	 * <p><code>new size = maximum size * percentage</code>
	 * @param maxEntriesCount - maximum number of log entries ( > 0 )
	 * @param percentage - % of entries after reduction, between 0 and 1
	 * @return <code>true</code> if log has been reduced, or <code>false</code> if else.
	 */
	public static boolean restrainLogEntriesCount(int maxEntriesCount, float percentage) {
		ArrayList<String []> logArray = readLog();
		if( logArray == null ) return false;
		int logSize = logArray.size();
		if( logSize < maxEntriesCount ) return false;
		int newLogSize = (int) (maxEntriesCount * percentage);
		int oldestEntryIndex = logSize - newLogSize;
		ArrayList<String []> newLogArray = new ArrayList<String []>(logArray.subList(oldestEntryIndex, logSize));
		return registerArray(newLogArray);
	}
	
	/**
	 * <p>Registers array entries to log file
	 * @param logEntries - array of entries, in the same format as returned from
	 * the {@link #readLog()} method.
	 * @return <code>true</code> if log has been reduced, or <code>false</code> if else.
	 */
	public static boolean registerArray(ArrayList<String []> logEntries) {
		if( !assertLogFile() ) return false;
		try {
			BufferedWriter writer = new BufferedWriter(
				new FileWriter(logFile, false) // false for 'write' mode
			);
			for( String [] entryArray : logEntries ) {
				String entryString = String.join(" ", entryArray);
				writer.write(entryString);
				writer.newLine();
			}
			writer.close();
			return true;
		} catch (IOException e) {
			FatalError.show(lang.get("gui_errmsg_launcher_log_registerfailed"), null, false); // does not exit
			return false;
		}
	}
		
	/**
	 *  Creates log file if inexistent. Warns the user with an error dialog if unsuccessful,
	 *  but does not terminate the program itself for safety reasons (The launcher could still
	 *  be running some sensitive procedure during the log registry, for example).
	 *  @return <code>true</code> if successful.
	 */
	private static boolean assertLogFile()
	{
		new File(LocalResources.datafolder).mkdirs();
		File f = null;
		try {
			f = new File(logFile);
			f.createNewFile(); // if does not exists, will create an empty one.
		} catch (IOException e) {
			FatalError.show(lang.get("gui_errmsg_launcher_log_openfailed"),null,false); // does not exit
			return false;
		}
		return f.exists() && f.isFile();
	}
	
	/**
	 * Checks whether the log file size exceeds the maximum size allowed, and if so, restraints
	 * it size by a percentage (both adjustable by the preferences dialog)
	 */
	public static void cleanUp() {
		GlobalProperties gp = GlobalProperties.getInstance();
		String maxLogSizeString = gp.get("maxlogsize");
		String percentageString = gp.get("logreduction");
		long maxLogSize;
		float percentage;
		try {
			maxLogSize = Long.parseLong(maxLogSizeString);
			percentage = (float) Integer.parseInt(percentageString) / 100;
			restrainLogSize(maxLogSize, percentage);
		} catch( NumberFormatException e ) {
			LightError.show(e);
		}
	}
		
}
