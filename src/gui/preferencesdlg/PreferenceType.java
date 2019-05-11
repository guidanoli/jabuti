package gui.preferencesdlg;

public interface PreferenceType {

	// validates value
	public boolean validateState();

	// returns appropriate text field panel for preference
	public PreferenceTypePanel getPanel();	
	
}
