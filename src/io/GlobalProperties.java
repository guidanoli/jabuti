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
				gp.store(new FileOutputStream(propertiesFilePath), null);
			}
			else
			{
				gp.load(new FileInputStream(propertiesFilePath));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gp;
	}

	
	
}
