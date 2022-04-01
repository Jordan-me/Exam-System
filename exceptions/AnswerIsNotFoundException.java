package exceptions;

public class AnswerIsNotFoundException extends Exception {
	public AnswerIsNotFoundException (int i) {
	super( "Answer " + i + " you searched for is not in the array of answers ");
	}
}
