package exam_system;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;

import exceptions.AnswerIsNotFoundException;
import exceptions.IllegalInputException;
import exceptions.InputNotInOptions;
import exceptions.NegativeInputException;
import exceptions.NoAnswerException;
import exceptions.TooManyAnwersOption;

public interface IExamGenerator {
	// update our repository
	void addQuestionToDB(AmericanQuestion q,int i)
			throws FileNotFoundException, AnswerIsNotFoundException, NoAnswerException, IOException;

	void addQuestionToDB(OpenQuestion q) throws FileNotFoundException, IOException;

	// User selection on specific questions
	ArrayList<Question> chosenQuestion();

	// get our dataBase to the client
	ArrayList<Question> getQuestion() throws FileNotFoundException, IllegalInputException;

	// show our dataBase to the client on the screen
	void showQuestion() throws FileNotFoundException, IllegalInputException;

	ArrayList<Answer> getChosenOption(ArrayList<Integer> options, AmericanQuestion aq)
			throws AnswerIsNotFoundException, NoAnswerException;

	void buildExamManually(ArrayList<Question> chosenQuestion) throws InputMismatchException, TooManyAnwersOption,
			AnswerIsNotFoundException, NoAnswerException, FileNotFoundException;

	void buildAutoExam(String numOfQuestions) throws IndexOutOfBoundsException, AnswerIsNotFoundException,
			NoAnswerException, FileNotFoundException, IllegalInputException;

	void addAnswerToSpecifiecQuestionToDB(int numOfQuestion, String ans)
			throws NegativeInputException, InputNotInOptions, IllegalInputException;

	void addQuestionToDB(OpenQuestion q, int i) throws IOException;
}
