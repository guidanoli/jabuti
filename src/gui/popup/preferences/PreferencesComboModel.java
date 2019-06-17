package gui.popup.preferences;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import gui.popup.preferences.types.ComboPreferenceType;
import gui.popup.preferences.types.DirectoryPreferenceType;
import gui.popup.preferences.types.NumberPreferenceType;
import svn.TortoiseHandler;
import vars.Language;



@SuppressWarnings("serial")
public class PreferencesComboModel extends AbstractListModel<String> implements ComboBoxModel<String> {

	public final static int KEY = 0;
	public final static int LABEL = 1;
	public final static int TYPE = 2;
	public final static int RESET = 3;
	
	private Language lang = Language.getInstance();
	
	protected String selected;
	
	protected String [] makeCmds = TortoiseHandler.makeCommands;
	protected String [] makeCmdsTips = getMakeCmdsTips(makeCmds);
	
	// { property label, property string on screen, preference type, asks for restart }
	protected Object[][] list = { 
			{ "path" , lang.get("gui_popup_preferences_proplabel_path") , new DirectoryPreferenceType() , false } ,
			{ "lang" , lang.get("gui_popup_preferences_proplabel_lang") , new ComboPreferenceType(lang.getLanguages()) , true } ,
			{ "maxthreads" , lang.get("gui_popup_preferences_proplabel_maxthreads") , new NumberPreferenceType(1,10) , false } ,
			{ "cleanups" , lang.get("gui_popup_preferences_proplabel_cleanups") , new NumberPreferenceType(1,10) , false } ,
			{ "makecmd" , lang.get("gui_popup_preferences_proplabel_makecmd") , new ComboPreferenceType(makeCmds,makeCmdsTips,false) , false } ,
	};
	
	public String getElementAt(int i) { return (String) list[i][LABEL]; }
	public int getSize() { return list.length; }
	public void setSelectedItem(Object anItem) { selected = (String) anItem; }
	public String getSelectedItem() { return (String) selected; }
	public Object getSelectedItemProperty(int index)
	{
		for( Object[] item : list )
		{
			if( item[LABEL] == selected )
				return item[index];
		}
		return null;
	}

	private String[] getMakeCmdsTips(String[] cmds) {
		String [] tips = new String[cmds.length];
		for(int i = 0 ; i < tips.length; i++)
			tips[i] = lang.get("gui_popup_preferences_propoption_makecmd_"+cmds[i]);
		return tips;
	}
	
}
