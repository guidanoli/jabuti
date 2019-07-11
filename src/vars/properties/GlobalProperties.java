package vars.properties;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import gui.defaults.DefaultNotificationPopup;
import gui.dialog.preferences.PreferenceType;
import gui.dialog.preferences.types.*;
import gui.dialog.preferences.types.combo.*;
import gui.error.FatalError;
import gui.error.LightError;
import svn.BranchManager;
import vars.LocalResources;
import vars.Metadata;
import vars.Version;
import vars.properties.types.LongBooleanProperty;

/**
 * <h1>Global Properties</h1>
 * 
 * <p>A way to store global data locally in XML files information about
 * the software usage, file states, etc.
 * <ul>
 * <li>Keys are set with the <b>set</b> function</li>
 * <li>and gotten by the <b>get</b> function</li>
 * <li>and stored by the <b>save</b> function, very intuitively</li>
 * </ul>
 * 
 * <p>{@link Property Properties} and their individual validation functions can be accessed
 * through their {@link PreferenceType} objects.
 * 
 * @author guidanoli
 *
 */
@SuppressWarnings("serial")
public class GlobalProperties extends Properties {

	/* Local Resources */
	private static final String configFolder = vars.LocalResources.datafolder;
	private static final String propertiesFile = vars.LocalResources.properties;

	/* Properties */
	private static LongBooleanProperty notificationProperty;
	private static final Property [] properties = {
			new Property( "version", Metadata.getInstance().getProperty("version")),
			new EditableProperty( "path", getDefaultPath(), new DirectoryPreferenceType(), false),
			new EditableProperty( "lang", "English", new ComboPreferenceType(new LanguageComboListener()), true ),
			new EditableProperty( "maxthreads", "3", new NumberPreferenceType(1,10), false ),
			new EditableProperty( "cleanups", "2", new NumberPreferenceType(1,10), false ),
			new EditableProperty( "makecmd", "mlldamt", new ComboPreferenceType(new MakeCmdCommandListener()), false ),
			new EditableProperty( "notify", getDefaultNotifications(), new TogglePreferenceType(GlobalProperties.notificationProperty), false),
	};
	
	/* Singleton instance */
	private static final GlobalProperties INSTANCE = new GlobalProperties();
	
	/**
	 * @return Global Properties singleton
	 */
	public static GlobalProperties getInstance() { return INSTANCE; }
	
	private GlobalProperties() {
		for( Property prop : properties )
			setProperty(prop.getKey(),prop.getDefaultValue());
		try {
			new File(configFolder).mkdirs();
			if ( !new File(propertiesFile).createNewFile() )
				loadFromXML(new FileInputStream(propertiesFile)); // if XML file is found, load it
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		validateProperties();
		save();
	}
	
	/* **************
	 * MAIN FUNCTIONS
	 * ************** */
	
	/**
	 * Validates properties according to each validation routine.
	 * If a property value is invalid, it will be overwritten by
	 * the default value.
	 */
	private void validateProperties() {
		boolean sameVersion = get("version").equals(getDefaultValue("version"));
		if( !sameVersion )
		{
			Version thisVersion = new Version(getDefaultValue("version"));
			Version fileVersion = new Version(get("version"));
			boolean earlier = thisVersion.earlierThan(fileVersion);
			String messageStr = earlier ? "Your software needs to be updated.\nYou can either update your copy of Jabuti or close program." :
				"Some configuration files need to be updated.\nYou can either update them or close the program\nnot to damage any internal data.";
			String titleStr = "Jabuti";
			String [] options = {earlier ? "Update software" : "Update files", "Close program"};
			Icon icon = null;
			try {
				icon = new ImageIcon(ImageIO.read(LocalResources.getStream(LocalResources.icon_bw)));
			} catch (IOException e) {
				FatalError.show(e);
			}
			int choice = JOptionPane.showOptionDialog(null, messageStr, titleStr,
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE,
						icon, options, options[0]);
			if( choice == JOptionPane.CLOSED_OPTION || choice == 1 ) {
				System.exit(0);
			}
		}
		ArrayList<EditableProperty> propList = getEditablePropertiesList();
		Iterator<EditableProperty> iter = propList.iterator();
		while( iter.hasNext() ) {
			EditableProperty prop = iter.next();
			PreferenceType type = prop.getType();
			String key = prop.getKey();
			if( !type.validateValue(get(key)) ) {
				set(prop.getDefaultValue(), key);
				if( sameVersion ) {
					LightError.show(String.format("Property '%s' was corrupted and got recovered.", key));
					// has to be in English because cannot initialize Language object on GlobalProperties constructor
				}
			}
		}
		// Now that the conflicts have been resolved,
		// the new version can be set
		resetPropertyToDefault("version");
	}

	/**
	 * Get property default value by key
	 * @param key - property key
	 * @return property default value for said key
	 */
	public String getDefaultValue(String key) {
		for( Property prop : properties ) {
			if( prop.getKey().equals(key) ) {
				return prop.getDefaultValue();
			}
		}
		return null;
	}
	
	/**
	 * Get property value by dot separated key
	 * If key is branches.branch.name then the parameters
	 * passed should be "branches", "branch", "name".
	 * @param dot_separated_keys - strings separated by '.' in key
	 * @return property value for said key
	 */
	public String get(String... dot_separated_keys) {
		String resulting_key = String.join(".", dot_separated_keys);
		return get(resulting_key);
	}
	
	/**
	 * Get property value by its key
	 * @param key - property key
	 * @return property value for said key
	 */
	public String get(String key) { return getProperty(key); }
	
	/**
	 * Sets value to property of key equal to the result
	 * of joining the dot separated keys with '.'
	 * @param value - the value that will be attributed to key
	 * @param dot_separated_keys - fragments of key separated by '.'
	 */
	public void set(String value, String... dot_separated_keys ) {
		String resulting_key = String.join(".", dot_separated_keys);
		setProperty(resulting_key, value);
	}
	
	/**
	 * Sets value to property of a given key
	 * @param value - the value to be attributed to key
	 * @param key - the property key
	 */
	public void set(String value, String key) { setProperty(value,key); }
	
	/**
	 * Saves all changes to property object to a XML file in UTF-8 encoding
	 * @return true if stored successfully
	 */
	public boolean save() {
		try {
			storeToXML(new FileOutputStream(propertiesFile), null, "UTF-8");
			return true;
		} catch (IOException e) {
			FatalError.show(e,null,false);
			return false;
		}
	}
	
	/**
	 * Deletes unnecessary information stored within the XML file
	 * For example: keys set to their default value are removed
	 */
	public void cleanUp() {
		for(String key : stringPropertyNames()) {
			boolean removable = false;
			if( key.startsWith(BranchManager.KEY_BRANCHES) )
			{
				removable |= get(key).equals("false"); // removes "false" keys of check boxes from main dialog JTable
			}
			if( removable ) remove(key);
		}
		// save difference
		save();
	}
	
	/**
	 * Resets property value to default value
	 * @param key - property key
	 * @return the property default and current value or
	 * {@code null} if property key does not exist
	 * 
	 */
	public String resetPropertyToDefault(String key)
	{
		for( Property prop : properties )
		{
			if( prop.getKey().equals(key) )
			{
				setProperty(key, prop.getDefaultValue());
				return get(key);
			}
		}
		return null;
	}
	
	/**
	 * @return array of property objects
	 * @see Property
	 */
	public static ArrayList<EditableProperty> getEditablePropertiesList() {
		ArrayList<EditableProperty> list = new ArrayList<EditableProperty>();
		for( Property prop : properties ) {
			if( prop instanceof EditableProperty ) {
				list.add((EditableProperty) prop);
			}
		}
		return list;
	}
	
	/* *******************
	 * AUXILIARY FUNCITONS
	 * ******************* */
	
	private static String getDefaultPath()
	{
		String [] candidates = {
				"D:\\users\\"+System.getProperty("user.name"),
				System.getProperty("user.home")
		};
		for( String c : candidates ) if(new File(c).exists()) return c;
		return null;
	}
	
	private static String getDefaultNotifications()
	{
		notificationProperty = new LongBooleanProperty(DefaultNotificationPopup.NOTIFICATION_TYPES_COUNT, true, "notify");
		return notificationProperty.getDefaultPropertyValue();
	}
	
}
