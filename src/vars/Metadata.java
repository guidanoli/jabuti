package vars;

import java.io.IOException;
import java.util.Properties;

import gui.error.FatalError;

public class Metadata extends Properties {

	private static final long serialVersionUID = -6578015679206165199L;

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
