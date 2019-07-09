package gui.dialog.preferences.types.combo;

import svn.TortoiseHandler;
import vars.Language;

public class MakeCmdCommandListener implements ComboPreferenceTypeListener {

	private String [] makeCmds = null;
	protected String [] makeCmdsTips = null;
	
	public MakeCmdCommandListener() {}
	
	public String[] getOptionToolTips(Language lang) {
		cacheStrings(lang);
		return makeCmdsTips;
	}

	public String[] getOptionLabels(Language lang) {
		cacheStrings(lang);
		return makeCmds;
	}

	public String formatLabel(String option, String tooltip) {
		return String.format("%s (%s)", tooltip, option);
	}

	private void cacheStrings(Language lang) {
		if( makeCmds == null ) makeCmds = TortoiseHandler.makeCommands;
		if( makeCmdsTips == null ) makeCmdsTips = getMakeCmdsTips(lang,makeCmds);
	}
	
	private String[] getMakeCmdsTips(Language lang, String[] cmds) {
		String [] tips = new String[cmds.length];
		for(int i = 0 ; i < tips.length; i++)
			tips[i] = lang.get("gui_popup_preferences_propoption_makecmd_"+cmds[i]);
		return tips;
	}
	
}
