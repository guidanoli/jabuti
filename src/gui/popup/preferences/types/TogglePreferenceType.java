package gui.popup.preferences.types;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import gui.error.LightError;
import gui.popup.preferences.PreferenceType;
import vars.Language;

public class TogglePreferenceType implements PreferenceType {

	private JPanel panel;
	private JCheckBox [] checkBoxes;
	private Language lang = Language.getInstance();
	
	public TogglePreferenceType(String [] keys) {
		panel = new JPanel(new BoxLayout(panel,BoxLayout.Y_AXIS));
		checkBoxes = new JCheckBox[keys.length];
		for( int i = 0 ; i < keys.length; i++ )
		{
			String langSuffix = keys[i].replaceAll("\\.", "_"); // escape regular expression dot
			String label = lang.get("meta_keylabel_"+langSuffix);
			if( label == null )
			{
				LightError.show(lang.format("gui_errmsg_accesskeylabelstringfailed",keys[i]));
				label = keys[i];
			}
			checkBoxes[i] = new JCheckBox(label);
			panel.add(checkBoxes[i]);
		}
	}

	public JPanel getPanel() { return panel; }

	@Override
	public void setState(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validateState() {
		// TODO Auto-generated method stub
		return false;
	}

}
