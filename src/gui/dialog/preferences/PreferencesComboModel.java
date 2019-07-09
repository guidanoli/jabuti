package gui.dialog.preferences;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import gui.error.FatalError;
import vars.Language;
import vars.properties.GlobalProperties;
import vars.properties.Property;

@SuppressWarnings("serial")
public class PreferencesComboModel extends AbstractListModel<String> implements ComboBoxModel<String> {

	private final static int KEY = 0;
	private final static int LABEL = 1;
	private final static int TYPE = 2;
	private final static int RESET = 3;

	private Property [] propertiesList = GlobalProperties.getPropertiesArray();
	private Language lang = Language.getInstance();
	private String selected; 
		
	public String getElementAt(int i) {
		assert i < getSize();
		return getLabel(propertiesList[i]);
	}
	
	public int getSize() { return propertiesList.length; }
	
	public void setSelectedItem(Object anItem) { selected = (String) anItem; }
	
	public String getSelectedItem() { return (String) selected; }
		
	private String getLabel( Property prop )
	{
		String propKey = prop.getKey();
		String langKey = String.format("gui_popup_preferences_proplabel_%s",propKey);
		return lang.get(langKey);
	}
	
	private Object getSelectedItemProperty(int propertyId)
	{
		for( Property prop : propertiesList )
		{
			if( getLabel(prop).equals(selected) )
				switch( propertyId )
				{
				case KEY:
					return prop.getKey();
				case LABEL:
					return selected;
				case TYPE:
					return prop.getType();
				case RESET:
					return prop.isReset();
				default:
					FatalError.show("Invalid item property identifier");
					break;
				}
		}
		return null;
	}
	
	public String getSelectedItemKey() { return (String) getSelectedItemProperty(KEY); }
	public String getSelectedItemLabel() { return getSelectedItem(); }
	public PreferenceType getSelectedItemType() { return (PreferenceType) getSelectedItemProperty(TYPE); }
	public boolean getSelectedItemReset() { return (boolean) getSelectedItemProperty(RESET); }
}
