package svn.error;

import vars.properties.bool.LongBooleanProperty;

public class SetupErrorListener extends LongBooleanProperty implements ErrorListener {

	public static enum Type {
		ARC(0, "vis arc install-certificate", true),
		ANY(1, "", true);
		
		private final int value;
		private final String regex;
		private final boolean isErrorWhenEnabled;
	    private Type(int value, String regex, boolean isErrorWhenEnabled) {
	    	this.value = value;
	    	this.regex = regex;
	    	this.isErrorWhenEnabled = isErrorWhenEnabled;
    	}
	    public int getValue() { return value; }
	    public boolean showError(String output, boolean isEnabled) {
	    	return output.matches(".*"+regex+".*") && isEnabled == isErrorWhenEnabled;
	    }
	}
	
	public static int SETUP_ERRORS_COUNT = Type.values().length;
	
	private boolean isHandling = true;
	
	public SetupErrorListener() {
		super(SETUP_ERRORS_COUNT, true, "setup-err");
	}
	
	public boolean handleErrorOutput(String output) {
		boolean isError = true;
		for( Type type : Type.values() ) {
			if( !type.showError(output, isEnabled(type.getValue())) ) {
				isError = false; break;
			}
		}
		return !isError;
	}

	public boolean isHandling() { return isHandling; }
	
	public void setHandling(boolean isHandling) { this.isHandling = isHandling; }

}
