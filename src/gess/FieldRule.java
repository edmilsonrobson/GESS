package gess;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.NoMatchException;

public class FieldRule implements Rule {

	public String ruleText;
	public String ruleName;
	
	public FieldRule (String ruleText){
		this.ruleText = ruleText;		
	}
	
	@Override
	public String apply(String body) throws NoMatchException {
		System.out.println(body);
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

	@Override
	public String getRuleName() {
		return this.ruleText;
	}
	
	

}
