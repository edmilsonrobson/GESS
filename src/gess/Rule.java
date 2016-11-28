package gess;
import exceptions.NoMatchException;

public abstract interface Rule {
	
	/**
	 * 
	 * @return
	 */
	public abstract String apply() throws NoMatchException;
	
}
