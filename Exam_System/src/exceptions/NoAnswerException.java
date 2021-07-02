package exceptions;

public class NoAnswerException extends Exception {
	public NoAnswerException (int i) {
		super("The answer in place" + i + "has not yet been written");
		}
}
