package gui.error;

import java.util.HashSet;
import java.util.Set;
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
		Set<String> solutions = new HashSet<String>();
		Set<String> unknownErrors = new HashSet<String>();
		while(matcher.find())
		{
			String error = matcher.group(1);
			String solution = lang.get("gui_errhelper_"+error);
			if( solution == null ) { unknownErrors.add(error); }
			else { solutions.add(solution); }
		}
		for( String error : unknownErrors ) sj.add(String.format("Error %s is not known. Report this issue!",error));
		for( String solution : solutions ) sj.add(solution);
		return sj.toString();
	}

}
