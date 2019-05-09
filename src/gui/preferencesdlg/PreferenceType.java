package gui.preferencesdlg;

public interface PreferenceType {

	// validates value
	public boolean validateNewValue( String value );
	
	// opens dialog for appropriate user input
	public Object openDialog(Object prefvalue);
	
}
