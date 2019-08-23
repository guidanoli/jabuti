package vars;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import gui.error.FatalError;
import gui.error.LightError;
import vars.properties.GlobalProperties;

/**
 * The <code>Language</code> class deals with language strings that can vary depending on
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
	
	private Properties lang; 
	
	private static String [] langs = {
		"English" , "Portuguese"	
	};
	
	public static String default_lang = langs[0];
	
	public static Language getInstance() { return INSTANCE; }

	private static final Language INSTANCE = new Language();
	
	/**
	 * Constructs a Language object
	 */
	private Language() {
		GlobalProperties gp = GlobalProperties.getInstance();
		String langname = gp.get("lang");
		boolean valid_langname = Arrays.stream(langs).anyMatch(langname::equals);
		if( !valid_langname )
		{
			langname = default_lang;
			gp.set("lang", langname);
			LightError.show("Invalid default language. Contact support.","Error",null);
		}
		try {
			lang = new Properties(Metadata.getInstance());
			lang.loadFromXML(LocalResources.getStream(LocalResources.langfolder+"/"+langname+".xml"));
		}
		catch (IOException e) {
			FatalError.show(e);
		}
	}
		
	/**
	 * <code>public static String get(String label)</code>
	 * <p>Get String from the abstract label in the current language
	 * @param label - string label, used to access label in XML
	 * @return string in the current language
	 */
	public String get(String label) {
		String value = lang.getProperty(label);
		while( value != null && value.contains("#") )
		{
			label = label.replaceAll("#", ""); // remove # on the front
			value = lang.getProperty(label);
			label = value;
		}
		return value;
	}
	
	/**
	 * <code>public static String format(String formatkey, String... labels)</code>
	 * <p>Formats String containing many Language labels, in a similar way to
	 * {@link java.lang.String#format(String, Object...) String.format} function
	 * <p>Makes use of formatted strings on the XML language files
	 * <p><b>Note:</b> for a given string key, there must be the same format footprint, in the same order
	 * @param formatkey - key of language string that represent a format
	 * @param labels - labels to be replaced in string
	 * @return formatted string or <code>null</code> if formatkey is invalid
	 */
	public String format(String formatkey, Object... values)
	{
		String formatval = get(formatkey);
		if(formatval==null) return null;
		return String.format(formatval, values);
	}
	
	/**
	 * @return all accepted languages
	 */
	public static String [] getLanguages() { return langs; }
	
}
