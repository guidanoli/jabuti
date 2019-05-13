package vars;
import gui.error.FatalError;
import vars.lang.*;

public class Language {

	public static final String [] langs = {
			"English" ,
			"Portuguese"
	};
	
	public static GlobalStrings get(GlobalProperties gp) {
		String lang = gp.getProperty("lang");
		GlobalStrings gs = null;
		if( lang.equals("English") )
			gs = new English();
		else {
			FatalError.show("Could not determine language");
		}
		return gs;
	}
	
	public static GlobalStrings get() {
		return get(GlobalProperties.get());
	}

}
