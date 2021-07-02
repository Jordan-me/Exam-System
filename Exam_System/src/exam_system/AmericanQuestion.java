package exam_system;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import exceptions.AnswerIsNotFoundException;
import exceptions.IllegalInputException;
import exceptions.NoAnswerException;
import exceptions.TooManyAnwersOption;

public class AmericanQuestion extends Question {
	private ArrayList<Answer> allAnswersOption;
	private final static int MAX_ANSWERS = 10;
	private String solution;

	public AmericanQuestion(String theQuestion) {
		super(theQuestion);
		allAnswersOption = new ArrayList<Answer>(MAX_ANSWERS);
	}
	public AmericanQuestion(String theQuestion, ArrayList<Answer>all) {
		super(theQuestion);
		allAnswersOption.addAll(all);
	}
	public AmericanQuestion(String theQuestion, ArrayList<Answer>all, ArrayList<String>solution) {
		super(theQuestion);
		allAnswersOption.addAll(all);
	}
	

	public void save(String fileName, PrintWriter pw) throws FileNotFoundException {
		super.save(fileName, pw);
		for (int i = 0; i < allAnswersOption.size(); i++) {
			pw.print("\t" + i + ")");
			allAnswersOption.get(i).save(pw);
		}
	}

	public boolean addAnswer(Answer theAnswer) throws IllegalInputException {
		if (allAnswersOption.contains(theAnswer)) {
			throw new IllegalInputException();
		}
		allAnswersOption.add(theAnswer);
		return true;
	}

	public void deleteSpecificAnswer(int i) throws IndexOutOfBoundsException {
		allAnswersOption.remove(i);
	}

	public void deleteAllAnswers() {
		allAnswersOption.clear();
	}

	public ArrayList<Answer> getTheCorrectAnswer() throws IndexOutOfBoundsException {
		ArrayList<Answer> correctAnswers = new ArrayList<Answer>(MAX_ANSWERS);
		int index = 0;
		for (int i = 0; i < allAnswersOption.size(); i++) {
			if (allAnswersOption.get(i).getIsCorrect()) {
				correctAnswers.add(index, allAnswersOption.get(i));
				index++;
			}
		}
		return correctAnswers;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		if (allAnswersOption.size() != 0) {
			for (int i = 0; i < allAnswersOption.size(); i++) {
				sb.append("\t" + (i + 1) + ") ");
				sb.append(allAnswersOption.get(i).toString() + "\n");
			}
		} else {
			sb.append("null");
		}
		return sb.toString();
	}

	public boolean equals(Object o) {
		if (super.equals(o)) {

			AmericanQuestion q = (AmericanQuestion) o;
			for (int i = 0; i < allAnswersOption.size(); i++) {
				if (!q.allAnswersOption.get(i).equals(allAnswersOption.get(i))) {
					return false;
				}

			}
			return true;
		}
		return false;
	}

	public String printSolution() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getTheQuestion() + "\n");
		ArrayList<Answer> correctAnswers = getTheCorrectAnswer();
		int numOfCorrectAnswer = correctAnswers.size();
		if (numOfCorrectAnswer != 0) {
			sb.append("The solution is:");
			for (int i = 0; i < numOfCorrectAnswer; i++) {
				sb.append(correctAnswers.get(i).toString() + ", ");
			}
		} else {
			sb.append("null");
		}
		return sb.toString();

	}

	public Answer getAnswerByIndex(int answerNumber) throws AnswerIsNotFoundException, NoAnswerException {
		if (answerNumber < 0 || answerNumber > allAnswersOption.size()) {
			throw new AnswerIsNotFoundException(answerNumber);
		}
		if (answerNumber >= allAnswersOption.size() || allAnswersOption.get(answerNumber) == null) {
			throw new NoAnswerException(answerNumber);
		}
		return allAnswersOption.get(answerNumber);
	}

	public ArrayList<Answer> getAllAnswerOption() {
		return allAnswersOption;
	}

	public void setAllAnswerOption(ArrayList<Answer> allAnswerOption) throws TooManyAnwersOption {
		if (allAnswerOption.size() > MAX_ANSWERS) {
			throw new TooManyAnwersOption();
		}
		this.allAnswersOption = allAnswerOption;
	}

	public int getNumOfAnswers() {
		return allAnswersOption.size();
	}

	public static int getMaxAnswers() {
		return MAX_ANSWERS;
	}
	public int getIndexByAnswer(String string) {
		for (int i = 0; i<allAnswersOption.size(); i++) {
			if (allAnswersOption.get(i).getTheAnswer().equals(string)) {
				return i ;
			}
		}
		return -1;
		
	}

}
