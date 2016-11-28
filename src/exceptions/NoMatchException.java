package exceptions;

public class NoMatchException extends Exception{
	//Parameterless Constructor
    public NoMatchException() {}

    //Constructor that accepts a message
    public NoMatchException(String message)
    {
       super(message);
    }
}
