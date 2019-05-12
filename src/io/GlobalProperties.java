package io;
import java.io.*;
import java.util.Properties;

@SuppressWarnings("serial")
public class GlobalProperties extends Properties {

	protected static final String propertiesFilePath = "properties.xml";
	
	public GlobalProperties() {
		String[][] defaultValues = {
				{"path","D:\\users\\"+System.getProperty("user.name")},	
		};
		for( String[] prop : defaultValues ) {
			setProperty(prop[0], prop[1]);
		}
	}
	
	public static GlobalProperties get() {
		GlobalProperties gp = new GlobalProperties();
		try {
			if (new File(propertiesFilePath).createNewFile()) {
				// if XML file isn't found, create one
				if( !gp.save() ) return null; // save it, if possible
			}
			else
			{
				// if XML file is found, load it
				gp.loadFromXML(new FileInputStream(propertiesFilePath));
			}
		} catch (IOException e) {
			return null;
		}
		gp.list(System.out);
		gp.save();
		return gp;
	}

	public boolean save() {
		try {
			storeToXML(new FileOutputStream(propertiesFilePath), null, "UTF-8");
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
}
