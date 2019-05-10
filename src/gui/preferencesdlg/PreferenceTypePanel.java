package gui.preferencesdlg;
import javax.swing.JPanel;

public interface PreferenceTypePanel {
	
	// returns appropriate text field panel
	public JPanel getPanel();
	
	// returns panel name for identification
	public String getPanelName();
	
	// sets panel state based on property value
	public void setState( String value );
	
	// gets current property value based on the panel state
	public String getValue();
	
}
