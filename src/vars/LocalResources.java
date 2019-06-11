package vars;

import java.io.File;
import java.net.URISyntaxException;

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
	public static final File datafolder = new File("data");
	public static File langfolder = null;
	public static File imgsfolder = null;
	static {
		try {
			langfolder = new File(LocalResources.class.getClassLoader().getResource("lang").toURI());
			imgsfolder = new File(LocalResources.class.getClassLoader().getResource("imgs").toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/* *************
	 * INTERNAL DATA
	 * ************* */
	public static final File launchlog = new File(datafolder,"/launch.log");
	public static final File properties = new File(datafolder,"/properties.xml");
	public static final File metalang = new File(langfolder,"/_meta_.xml");
	
	/* *****
	 * ICONS
	 * ***** */
	public static final File icon = new File(imgsfolder,"/setmeup.png");
	public static final File success = new File(imgsfolder,"/success.png");
	public static final File error = new File(imgsfolder,"/error.png");
	public static final File warning = new File(imgsfolder,"/warning.png");
	public static final File minus = new File(imgsfolder,"/minus.png");
	public static final File pause = new File(imgsfolder,"/pause-1.png");
	public static final File stop = new File(imgsfolder,"/stop-1.png");
	public static final File info = new File(imgsfolder,"/info.png");
	public static final File empty = new File(imgsfolder,"/empty.png");
	public static final File more = new File(imgsfolder,"/more.png");
	public static final File unlock = new File(imgsfolder,"/locked-1.png");
	
}
