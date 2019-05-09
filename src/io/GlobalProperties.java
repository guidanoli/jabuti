package io;
import java.io.*;
import java.util.Properties;

@SuppressWarnings("serial")
public class GlobalProperties extends Properties {

	protected static final String propertiesFilePath = "setmeup.properties";
	
	public static GlobalProperties get() {
		GlobalProperties gp = new GlobalProperties();
		try {
			if (new File(propertiesFilePath).createNewFile()) {
				gp.setProperty("path", System.getProperty("user.home"));
				if( !gp.save() ) return null; 
			}
			else
			{
				gp.load(new FileInputStream(propertiesFilePath));
			}
		} catch (IOException e) {
			return null;
		}
		return gp;
	}

	public boolean save() {
		try {
			store(new FileOutputStream(propertiesFilePath), null);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
}
