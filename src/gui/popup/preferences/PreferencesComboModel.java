package gui.popup.preferences;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;



@SuppressWarnings("serial")
public class PreferencesComboModel extends AbstractListModel<String> implements ComboBoxModel<String> {

	String selected;
	Object[][] list = { 
			{ "path" , vars.Language.get().gui_popup_preferences_proplabel_path , new DirectoryPreferenceType() } ,
			{ "lang" , "Language" , new ComboPreferenceType(vars.Language.langs) } ,
	};
	
	public String getElementAt(int arg0) { return (String) list[arg0][1]; }
	public int getSize() { return list.length; }
	public String getSelectedItem() { return selected; }
	public void setSelectedItem(Object anItem) { selected = (String) anItem; }
	public String getSelectedItemLabel()
	{
		for( Object[] item : list )
		{
			if( item[1] == selected )
				return (String) item[0];
		}
		return null;
	}
	public PreferenceType getSelectedItemType()
	{
		for( Object[] item : list )
		{
			if( item[1] == selected )
				return (PreferenceType) item[2];
		}
		return null;
	}

}
