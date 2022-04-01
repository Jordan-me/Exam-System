package exceptions;

public class TooManyAnwersOption extends Exception{
	public TooManyAnwersOption () {
		super("Too many answers have been entered, a question can contain between 1-10 answers.");
	}

}
