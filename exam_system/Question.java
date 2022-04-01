package exam_system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import exceptions.AnswerIsNotFoundException;
import exceptions.NoAnswerException;
import exceptions.TooManyAnwersOption;

public abstract class Question {
	private String theQuestion;
	private static int autoGenerator;

	public Question(String theQuestion) {
		setTheQuestion(theQuestion);
		autoGenerator++;
	}

	public void save(String fileName, PrintWriter pw) throws FileNotFoundException {
		File file = new File(fileName);
		pw.println(autoGenerator + 1);
		pw.println(theQuestion);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(theQuestion + "\n");
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Question)) {
			return false;
		}
		Question q = (Question) o;
		if (q.getTheQuestion().equals(this.getTheQuestion())) {
			return true;
		}
		return false;
	}

	public void updateTheQuestion(String addition) {
		if (!theQuestion.contains(addition)) {
			String newQuestion = theQuestion + addition;
			setTheQuestion(newQuestion);
		}
	}

	public void rewriterQuestion() {
		theQuestion = "choose " + theQuestion;
		theQuestion = theQuestion.replace('?', '.');
	}

	public String getTheQuestion() {
		return theQuestion;
	}

	private void setTheQuestion(String theQuestion) {
		this.theQuestion = theQuestion;

	}

}
