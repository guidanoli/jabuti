package gui.error;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vars.Language;

public class ErrorHelper {
	
	public static String getHelp(String errorContent)
	{
		Language lang = Language.getInstance();
		StringBuilder sb = new StringBuilder();
		Pattern pattern = Pattern.compile("E(\\d*)"); // SVN errors
		Matcher matcher = pattern.matcher(errorContent);
		while(matcher.find())
		{
			String error = matcher.group(1);
			sb.append("\n"+lang.get("gui_errhelper_"+error));
		}
		return sb.toString();
	}

}
