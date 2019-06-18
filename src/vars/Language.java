package vars;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import gui.error.FatalError;
import gui.error.LightError;

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
	
	private static final Language INSTANCE = new Language();
	private Properties lang; 
	
	private String [] langs = {
		"English" , "Portuguese"	
	};
	
	public String default_lang = langs[0];
	
	public static Language getInstance() { return INSTANCE; }
	
	/**
	 * Constructs a Language object
	 */
	private Language() {
		GlobalProperties gp = GlobalProperties.getInstance();
		String langname = gp.getProperty("lang");
		boolean valid_langname = Arrays.stream(langs).anyMatch(langname::equals);
		if( !valid_langname )
		{
			langname = default_lang;
			gp.set("lang", langname);
			LightError.show("Invalid default language. Contact support.","Error",null);
		}
		Properties metalang = new Properties();
		try {
			metalang.loadFromXML(LocalResources.getStream(LocalResources.metalang));
			lang = new Properties(metalang);
			lang.loadFromXML(LocalResources.getStream(LocalResources.langfolder+"/"+langname+".xml"));
		}
		catch (IOException e) {
			FatalError.show(e);
		}
	}
		
	/**
	 * {@code public static String get(String label)}
	 * <p>Get String from the abstract label in the current language
	 * @param label - string label, used to access label in XML
	 * @return string in the current language
	 */
	public String get(String label) {
		return lang.getProperty(label);
	}
	
	/**
	 * {@code public static String format(String formatkey, String... labels)}
	 * <p>Formats String containing many Language labels, in a similar way to
	 * {@link java.lang.String#format(String, Object...) String.format} function
	 * <p>Makes use of formatted strings on the XML language files
	 * <p><b>Note:</b> for a given string key, there must be the same format footprint, in the same order
	 * @param formatkey - key of language string that represent a format
	 * @param labels - labels to be replaced in string
	 * @return formatted string or {@code null} if formatkey is invalid
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
	public String [] getLanguages() { return langs; }
	
}
