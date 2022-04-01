package validation;

import java.util.Scanner;

import exam_system.AmericanQuestion;
import exam_system.ExamGenerator;
import exceptions.InputIsNullException;
import exceptions.InputNotInOptions;
import exceptions.NegativeInputException;

public class ValidateFunc {
	public static int setSwitchCaseChoice2(String input) throws InputNotInOptions {
		if (!input.matches("[1-2]")) {
			throw new InputNotInOptions();
		}
		int validInput = Integer.parseInt(input);
		return validInput;
	}

	public static int setSwitchCaseChoice(String input) throws InputNotInOptions {
		if (!input.matches("[0-3]")) {
			throw new InputNotInOptions();
		}
		int validInput = Integer.parseInt(input);
		return validInput;
	}

	public static boolean validate(int num, int max) {
		if (num <= max && num > 0) {
			return true;
		}
		return false;
	}
	public static void isPositive(int num) throws NegativeInputException {
		if (num <= 0) {
			throw new NegativeInputException();
		}
	}
	public static boolean setChoice(Scanner s) throws NegativeInputException, InputNotInOptions {
		boolean status = false;
		System.out.println("please press one of the options-");
		System.out.println("yes- 1 \t no- 2");
		int choice = s.nextInt();
		isPositive(choice);
		if (choice != 1 && choice != 2) {
			System.out.println("insert what you asked for");
			throw new InputNotInOptions();
		}
		if (choice == 1) {
			status = true;
		}
		return status;
	}
	public static void validScanner(String input) throws InputIsNullException {
		if (input.equals("")) {
			System.out.println("insert what you asked for");
			throw new InputIsNullException();
		}
	}
	public static boolean validationAutoExam(String num) {
		final int radix = 10;
		if (num.isEmpty()) {
			return false;
		}
		for (int i = 0; i<num.length(); i++) {
			if(i == 0 && num.charAt(i) == '-') { 
	            if(num.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(num.charAt(i),radix) < 0) {
	        	return false;
	        }
	    }
		int parsNum = Integer.parseInt(num);
	 	    
		if (parsNum <= 0 || parsNum > ExamGenerator.getNumAQ()) {
			return false;
		}
		return true;
	}
	public static boolean validationQA(int num, AmericanQuestion aq) {
		if (num <= 0 || num > aq.getNumOfAnswers()) {
			return false; // its not ok
		}
		return true;
	}
	public boolean validationChosenQuestion(int userInput, int numOfQuestions) {
		if (userInput <= 0 || userInput > numOfQuestions) {
			return false;
		}
		return true;
	}

}
