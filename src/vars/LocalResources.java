package vars;

import java.io.InputStream;

/**
 * Local Resources store global data in one spot
 * Centralizing it will make it easier to change certain values
 * in the future for the programmer - in this case - me.
 * 
 * @author guidanoli
 *
 */
public class LocalResources {

	/* *******
	 * FOLDERS
	 * ******* */
	public static final String datafolder = System.getenv("userprofile") + "/.jabuti";
	public static final String langfolder = "lang";
	public static final String imgsfolder = "imgs";
	
	/* *************
	 * INTERNAL DATA
	 * ************* */
	public static final String launchlog = datafolder+"/launch.log";
	public static final String properties = datafolder+"/properties.xml";
	public static final String metalang = langfolder+"/_meta_.xml";
	
	/* *****
	 * ICONS
	 * ***** */
	public static final String icon = imgsfolder+"/turtle.png";
	public static final String icon_bw = imgsfolder+"/turtle-bw.gif";
	public static final String success = imgsfolder+"/success.png";
	public static final String error = imgsfolder+"/error.png";
	public static final String warning = imgsfolder+"/warning.png";
	public static final String minus = imgsfolder+"/minus.png";
	public static final String pause = imgsfolder+"/pause-1.png";
	public static final String stop = imgsfolder+"/stop-1.png";
	public static final String info = imgsfolder+"/info.png";
	public static final String empty = imgsfolder+"/empty.png";
	public static final String more = imgsfolder+"/more.png";
	public static final String unlock = imgsfolder+"/locked-1.png";
	
	/* ********
	 * SHORTCUT
	 * ******** */
	
	public static InputStream getStream(String path)
	{
		return LocalResources.class.getClassLoader().getResourceAsStream(path);
	}
	
}
