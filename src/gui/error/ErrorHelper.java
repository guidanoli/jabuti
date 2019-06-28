package gui.error;

import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vars.Language;

public class ErrorHelper {
	
	public static String getHelp(String errorContent)
	{
		Language lang = Language.getInstance();
		StringJoiner sj = new StringJoiner("\n");
		Pattern pattern = Pattern.compile("E(\\d+)"); // SVN errors
		Matcher matcher = pattern.matcher(errorContent);
		while(matcher.find())
		{
			String error = matcher.group(1);
			String solution = lang.get("gui_errhelper_"+error);
			if( solution != null ) sj.add(solution);
		}
		return sj.toString();
	}

}
