package gess;
import exceptions.NoMatchException;

public interface Rule {
	
	/**
	 * 
	 * @return
	 */
	public String apply(String body) throws NoMatchException;
	
	
	public String getRuleName();
}
