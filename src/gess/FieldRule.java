package gess;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.NoMatchException;

public class FieldRule implements Rule {

	public String body;
	public String ruleText;
	
	public FieldRule (String body, String ruleText){
		this.body = body;
		this.ruleText = ruleText;
	}
	
	@Override
	public String apply() throws NoMatchException {
		Pattern pattern = Pattern.compile(ruleText + "(.*)$", Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(body);
		if (matcher.find()){
			String extractedText = matcher.group(1).trim();
			if (extractedText != null){
				return extractedText;
			}
		}
		else{
			throw new NoMatchException("No match was found.");			
		}
		return "";
		
	}

}
