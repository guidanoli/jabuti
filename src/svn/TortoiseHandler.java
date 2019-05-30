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

public class TortoiseHandler {

	protected String branchDir;
	
	public TortoiseHandler(String branchDirectory)
	{
		assert(branchDirectory!=null);
		branchDir = branchDirectory;
	}
	
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
		return 0;
	}
	
	public boolean isTortoiseDir(String branchName)
	{
		Path fullPath = Paths.get(branchDir, branchName);
		File f = new File(fullPath.toString());
		if( !f.exists() ) return false;
		return !runCmd(fullPath.toString(),"svn","info").equals("");
	}
	
	protected String runCmd(String dir, boolean error, String... cmd)
	{	
		StringBuilder errsb = new StringBuilder();
		try {  
	      String line;
	      StringBuilder sb = new StringBuilder();
	      ProcessBuilder pb = new ProcessBuilder(cmd);
	      pb.directory(new File(dir));
	      Process p = pb.start();
	      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
	      if(error)
	      {
	    	  BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	    	  while ((line = stdError.readLine()) != null) {
	    		  errsb.append(line);
	    	  }
	      }
	      while ((line = input.readLine()) != null) {
	    	  sb.append(line);  
	      }
	      input.close();
	      return sb.toString();
	    }  
	    catch (Exception e) {
	    	FatalError.show(errsb.toString());
	    	FatalError.show(e,null,false);
	    }
		return null;
	}
	
	protected String runCmd(String dir, String... cmd) { return runCmd(dir,false,cmd); }
	
	public boolean isUpdated(String branchName)
	{
		return 	getTargetLastRevisionNumber(branchName) ==
				getLastRevisionNumber(branchName);
	}
	
	public void setup(String branchName)
	{
		Random r = new Random();
		try {
			TimeUnit.SECONDS.sleep(r.nextInt(5));
		} catch (InterruptedException e) {
			FatalError.show(e);
		}
	}
	
	public void make(String branchName)
	{
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
