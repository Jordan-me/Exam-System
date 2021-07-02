package exceptions;

public class NoQuestionException extends Exception {
	public NoQuestionException (int i) {
		super("The question in place " + (i+1) + " has not yet been written");
		}
}
