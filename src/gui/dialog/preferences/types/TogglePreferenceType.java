package gui.dialog.preferences.types;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import gui.dialog.preferences.PreferenceType;
import vars.Language;
import vars.properties.bool.BooleanProperty;

public class TogglePreferenceType implements PreferenceType {

	private JPanel panel = new JPanel();
	private JCheckBox [] checkBoxes;
	private BooleanProperty booleanProperty;
	
	public TogglePreferenceType(BooleanProperty booleanProperty) {
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		this.booleanProperty = booleanProperty;
	}

	private void buildPanel() {
		String [] propertyLabels = booleanProperty.getPropertyLabels();
		checkBoxes = new JCheckBox[propertyLabels.length];
		for( int i = 0 ; i < propertyLabels.length; i++ )
		{
			String propertyLabel = propertyLabels[i];
			checkBoxes[i] = new JCheckBox(propertyLabel);
		}
		for( JCheckBox checkBox : checkBoxes ) { panel.add(checkBox); }
	}
	
	public JPanel getPanel(Language lang) {
		buildPanel();
		return panel;
	}

	public void setState(String value) {
		boolean [] booleanArray = booleanProperty.toBooleanArray(value);
		for( int i = 0 ; i < booleanArray.length; i++ )
		{
			checkBoxes[i].setSelected(booleanArray[i]);
		}
	}

	public String getState() {
		boolean [] booleanArray = getBooleanArray();
		return booleanProperty.toPropertyString(booleanArray);
	}

	private boolean [] getBooleanArray() {
		boolean [] booleanArray = new boolean[checkBoxes.length];
		for( int i = 0 ; i < checkBoxes.length; i++ )
		{
			booleanArray[i] = checkBoxes[i].isSelected();
		}
		return booleanArray;
	}
	
	public boolean validateValue(String value) {
		boolean [] booleanArray = booleanProperty.toBooleanArray(value);
		return booleanProperty.validateProperty(booleanArray);
	}

}
