package exam_system;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;

import exceptions.IllegalInputException;
import exceptions.NoQuestionException;
import exceptions.QuestionIsNotFoundException;

public class Exam {
	private final int TOTAL_SCORE = 100; // in Sat there is option to get 0-800.
	private ArrayList<Question> allQuestions;
	// private String Subject;
	private LocalDateTime examDate;// for future use
	private double scoreForEachQuestion;
	private int currentQuestion;

	public Exam(ArrayList<Question> allQuestions) {
		this.allQuestions = allQuestions;
		setScoreForEachQuestion();
		setExamDate();
		currentQuestion = 0;
	}

	public void saveExam(PrintWriter pw) throws FileNotFoundException {
		for (int i = 0; i < allQuestions.size(); i++) {
			pw.print((i + 1) + ")");
			if (allQuestions.get(i) instanceof AmericanQuestion) {
				AmericanQuestion q = (AmericanQuestion) allQuestions.get(i);
				pw.print(q.toString());
			} else if (allQuestions.get(i) instanceof OpenQuestion) {
				OpenQuestion q = (OpenQuestion) allQuestions.get(i);
				pw.print(q.getTheQuestion());
			}
			pw.println();
		}
	}

	public void saveSolution(PrintWriter pw) throws FileNotFoundException {
		for (int i = 0; i < allQuestions.size(); i++) {
			pw.print((i + 1) + ")");
			if (allQuestions.get(i) instanceof AmericanQuestion) {
				AmericanQuestion q = (AmericanQuestion) allQuestions.get(i);
				pw.print(q.printSolution());
			} else if (allQuestions.get(i) instanceof OpenQuestion) {
				OpenQuestion q = (OpenQuestion) allQuestions.get(i);
				pw.print(q.toString());
			}
			pw.println();
		}
	}

	public boolean addQuestion(Question theQuestion) throws IllegalInputException {
		for (int i = 0; i < allQuestions.size(); i++) {
			if (allQuestions.get(i).equals(theQuestion)) {
				throw new IllegalInputException();
			}
		}
		allQuestions.add(theQuestion);
		currentQuestion++;
		return true;
	}

	public void deleteQuestion(int numOfQuestion) {
		if (allQuestions.get(numOfQuestion) instanceof AmericanQuestion) {
			AmericanQuestion q = (AmericanQuestion) allQuestions.get(numOfQuestion);
			q.deleteAllAnswers();
		}
		allQuestions.remove(numOfQuestion);
	}

	public void deleteAllQuestions() { // option to restart the question array
		for (int i = 0; i < currentQuestion; i++) {
			deleteQuestion(i);
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Exam_" + examDate + ": \n");
		setScoreForEachQuestion();
		boolean justOneTime;
		for (int i = 0; i < currentQuestion; i++) {
			justOneTime = true;
			if (allQuestions.get(i) != null) {
				if (justOneTime) {
					allQuestions.get(i).updateTheQuestion("(" + scoreForEachQuestion + ")"); // add score near by the
																								// question
				}
				justOneTime = false;
				sb.append((i + 1) + ") ");
				String str = allQuestions.get(i).toString();
				sb.append(str);
			}
			sb.append("\n");
		}
		String[] str = sb.toString().trim().split(":"); // if the exam is empty return null to the screen.
		if (str.length == 1) {
			sb.append("null");

		}
		return sb.toString();
	}

	public void printSolution() {
		System.out.println("Solution" + getExamDate() + ":");
		for (int i = 0; i < currentQuestion; i++) {
			System.out.println(allQuestions.get(i).getTheQuestion());
			if (allQuestions.get(i) instanceof AmericanQuestion) {
				AmericanQuestion q = (AmericanQuestion) allQuestions.get(i);
				System.out.println(q.printSolution());
			} else if (allQuestions.get(i) instanceof OpenQuestion) {
				OpenQuestion q = (OpenQuestion) allQuestions.get(i);
				System.out.println(q.getSolution().toString());
			}
		}
	}

	public Question getQuestionByNumber(int questionIndex) throws QuestionIsNotFoundException, NoQuestionException {

		if (questionIndex < 0 || questionIndex > currentQuestion) {
			throw new QuestionIsNotFoundException(questionIndex);
		}
		if (allQuestions.get(questionIndex) == null) {
			throw new NoQuestionException(questionIndex);
		}
		return allQuestions.get(questionIndex);
	}

	public int getCurrentQuestion() {
		return currentQuestion;
	}

	public void setScoreForEachQuestion() {
		scoreForEachQuestion = (double) TOTAL_SCORE / (double) currentQuestion;
	}

	public double getScoreForEachQuestion() {
		return scoreForEachQuestion;
	}

	public void setExamDate() {
		LocalDateTime ldt = LocalDateTime.now();
		this.examDate = ldt;
	}

	public LocalDateTime getExamDate() {
		return examDate;
	}

}
