package exceptions;

import java.util.Scanner;

public class InputIsNullException extends Exception {

	public InputIsNullException() {
		super("Input is incorrect - the input must not be empty");
		
	}
	
}
