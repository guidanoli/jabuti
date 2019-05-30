package vars;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import gui.error.FatalError;

/**
 * The {@code Language} class deals with language strings that can vary depending on
 * the user preferences. It causes the programmer to have full control of which string
 * will be shown where, independently of the actual language.
 * <p> All the language strings are stored in Properties format virtually and in XML
 * format locally. To speed up the registration of new keys and values to each language,
 * there is a Python script in the same folder as the XML.
 * @see java.util.Properties Properties
 * @author guidanoli
 *
 */
public class Language {
	
	public static String [] langs = {
		"English" , "Portuguese"	
	};
	
	public static String default_lang = langs[0];
	
	/**
	 * Get String from the abstract label
	 * @param label - string label, used to access label in XML
	 * @param gp - Global Properties object, used to get current language
	 * @return string in the current language
	 */
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
	
	/**
	 * Get String from the abstract label in the current language
	 * @param label - string label, used to access label in XML
	 * @return string in the current language
	 */
	public static String get(String label) { return get(label,GlobalProperties.gp); }
	
	/**
	 * Formats String containing many Language labels
	 * <p>Similar to {@link java.lang.String#format(String, Object...) String.format} function
	 * @param format - identical to {@code format} parameter
	 * @param labels - labels to be replaced in string
	 * @return formatted string
	 */
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
