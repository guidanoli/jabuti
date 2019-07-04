package gui.dialog.preferences;
import javax.swing.JPanel;

public interface PreferenceType {

	// returns appropriate text field panel for preference
	public JPanel getPanel();	
	
	// sets panel state based on property value
	public void setState( String value );
	
	// gets current property value based on the panel state
	public String getState();
	
	// validates value
	public boolean validateState();
	
}
