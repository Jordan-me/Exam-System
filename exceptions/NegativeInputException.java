package exceptions;

public class NegativeInputException extends Exception {
	public NegativeInputException () {
		super("The number that you entered is incorrect, please enter a positive number");
	}
}
