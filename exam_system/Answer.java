package exam_system;

import java.io.PrintWriter;

public class Answer {
	private String theAnswer;
	private boolean isCorrect;

	public Answer(String theAnswer) {
		setTheAnswer(theAnswer);

	}

	public Answer(String theAnswer, boolean isCorrect) {
		setTheAnswer(theAnswer);
		setIsCorrect(isCorrect);
	}

	public void rewriterAnswer(String str, boolean isCorrect) { // option to add some words to the answer
		if (!this.theAnswer.contains(str)) {
			setTheAnswer(this.theAnswer + str);
			setIsCorrect(isCorrect);
		}
	}

	@Override
	public String toString() {
		return theAnswer;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Answer)) {
			return false;
		}
		Answer obj = (Answer) o;
		return (theAnswer.equals(obj.theAnswer));
	}

	public void save(PrintWriter pw) {
		pw.println(theAnswer);

	}

	public void setIsCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public boolean getIsCorrect() {
		return isCorrect;
	}

	public void setTheAnswer(String theAnswer) { // this to update the answer
		this.theAnswer = theAnswer;
	}

	public String getTheAnswer() {
		return theAnswer;
	}

}
