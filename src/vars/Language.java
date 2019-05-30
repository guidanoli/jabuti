package vars;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import gui.error.FatalError;

public class Language {
	
	public static String [] langs = {
		"English" , "Portuguese"	
	};
	
	public static String default_lang = langs[0];
	
	public static String get(String label, GlobalProperties gp) {
		String langname = gp.getProperty("lang");
		boolean valid_langname = Arrays.stream(langs).anyMatch(langname::equals);
		if( !valid_langname ) FatalError.show("Invalid language loaded.");
		Properties metalang = new Properties();
		Properties lang = null;
		try {
			metalang.loadFromXML(new FileInputStream(LocalResources.metalang));
			lang = new Properties(metalang);
			lang.loadFromXML(new FileInputStream(LocalResources.langfolder+"/"+langname+".xml"));
		}
		catch (IOException e) { FatalError.show(e); }
		return lang.getProperty(label);
	}
	
	public static String get(String label) { return get(label,GlobalProperties.gp); }
	
	public static String format(String format, String... labels)
	{
		Object [] values = new String[labels.length];
		GlobalProperties gp = GlobalProperties.gp;
		int i = 0;
		for( String label : labels ) {
			values[i] = get(label,gp); 
			i += 1;
		}
		return String.format(format, values);
	}
	
}
