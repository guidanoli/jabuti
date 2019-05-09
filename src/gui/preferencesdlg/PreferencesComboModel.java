package gui.preferencesdlg;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

@SuppressWarnings("serial")
public class PreferencesComboModel extends AbstractListModel<String> implements ComboBoxModel<String> {

	Object[][] list = { 
			{ "path" , "Branches directory" , new DefaultPreferenceType() }
	};
	
	String selected;
	
	@Override
	public String getElementAt(int arg0) {
		// TODO Auto-generated method stub
		return (String) list[arg0][1];
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return list.length;
	}

	@Override
	public String getSelectedItem() {
		// TODO Auto-generated method stub
		return selected;
	}

	@Override
	public void setSelectedItem(Object anItem) {
		// TODO Auto-generated method stub
		selected = (String) anItem;
	}
	
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
