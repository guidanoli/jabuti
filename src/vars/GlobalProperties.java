package vars;
import java.io.*;
import java.util.Properties;

import gui.error.FatalError;
import vis.BranchManager;

@SuppressWarnings("serial")
public class GlobalProperties extends Properties {

	protected static final String configFolderPath = vars.LocalRessources.configfolder;
	protected static final String propertiesFilePath = vars.LocalRessources.properties;
	protected static final String[][] defaultValues = {
		{"path",getDefaultPath()},	
		{"lang",vars.Language.default_lang},
		{"maxthreads","3"}
	};
	
	public GlobalProperties() {
		for( String[] prop : defaultValues )
			if( getProperty(prop[1]) == null )
				setProperty(prop[0], prop[1]);
	}
	
	// gets global properties
	public static GlobalProperties get() {
		GlobalProperties gp = getDefaults();
		try {
			new File(configFolderPath).mkdirs();
			if (new File(propertiesFilePath).createNewFile()) {
				// if XML file isn't found, create one
				if( !gp.save() ) return null; // save it, if possible
			}
			else
			{
				// if XML file is found, load it
				gp.loadFromXML(new FileInputStream(propertiesFilePath));
			}
		} catch (Exception e) {
			FatalError.show(e);
		}
		gp.save();
		return gp;
	}

	// get default properties
	public static GlobalProperties getDefaults() { return new GlobalProperties(); }
	
	// saves global properties
	public boolean save() {
		try {
			storeToXML(new FileOutputStream(propertiesFilePath), null, "UTF-8");
			return true;
		} catch (IOException e) {
			FatalError.show(e,null,false);
			return false;
		}
	}
	
	// cleans unnecessary data saved
	public void cleanUp() {
		for(String key : stringPropertyNames()) {
			boolean removable = false;
			if( key.startsWith(BranchManager.KEY_BRANCHES) )
			{
				removable |= getProperty(key).equals("false"); // removes "false" keys of check boxes from main dialog JTable
				// TODO: remove keys from branches no long existent
			}
			if( removable ) remove(key);
		}
		// save difference
		save();
	}
	
	// get default path
	private static String getDefaultPath()
	{
		String [] candidates = {
				"D:\\users\\"+System.getProperty("user.name"),
				System.getProperty("user.home")
		};
		for( String c : candidates ) if(new File(c).exists()) return c;
		return null;
	}
	
}
