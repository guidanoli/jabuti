package vars;

import java.io.IOException;
import java.util.Properties;

import gui.error.FatalError;

@SuppressWarnings("serial")
public class Metadata extends Properties {

	/* Singleton instance */
	public static Metadata getInstance() { return INSTANCE; }
	private static final Metadata INSTANCE = new Metadata();
	
	/* Private singleton constructor */
	private Metadata() {
		try {
			loadFromXML(LocalResources.getStream(LocalResources.metalang));
		}
		catch (IOException e) {
			FatalError.show(e);
		}
	}
	
}
