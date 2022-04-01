package exceptions;

public class QuestionIsNotFoundException extends Exception {
	public QuestionIsNotFoundException (int i) {
		super( "Question " + i + " you searched for is not in the array of questions ");
		}
}
