package listeners;

import java.util.ArrayList;

public interface ExamEventsListener {
	void failedAddingAutoExamToModelEvent(String message);
	void failedAddingManualExamToModelEvent(String message);
	void addQuestionToModelEvent(String theQuestion, int i);
	void addAmericanQuestionToModelEvent(int id, String theQuestion, ArrayList<String> allAnswers);
	void fireShowWindow(int i);
	void fireCloseWindow(int i);
	void addAnswerToModelEvent(int numOfQuestion, String theOption, int idAns , int numOfAnswersBefor);
	void fireRemoveAnswer(int numOfAns, int numOfQ, String theOption, String theQuestion);
	void fireRemoveQuestion(int numOfQ, String theQuestion);
	
	
}
