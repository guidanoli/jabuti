package vars;
import java.io.*;
import java.util.Properties;
import vis.BranchManager;

@SuppressWarnings("serial")
public class GlobalProperties extends Properties {

	protected static final String propertiesFilePath = vars.LocalRessources.properties;
	
	public GlobalProperties() {
		String[][] defaultValues = {
				{"path","D:\\users\\"+System.getProperty("user.name")},	
				{"lang",vars.Language.default_lang}
		};
		for( String[] prop : defaultValues )
			if( getProperty(prop[1]) == null )
				setProperty(prop[0], prop[1]);
	}
	
	// gets global properties
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
		gp.save();
		return gp;
	}

	// saves global properties
	public boolean save() {
		try {
			storeToXML(new FileOutputStream(propertiesFilePath), null, "UTF-8");
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	// cleans unnecessary data saved
	public void cleanUp() {
		for(String key : stringPropertyNames()) {
			if( key.startsWith(BranchManager.KEY_BRANCHES) && getProperty(key).equals("false") )
			{
				// removes "false" keys of check boxes from main dialog JTable
				remove(key);
			}
		}
		// save difference
		save();
	}
	
}
