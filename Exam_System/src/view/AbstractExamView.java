package view;

import java.util.ArrayList;

import javafx.scene.control.ComboBox;
import listeners.ExamUIEventsListener;

public interface AbstractExamView {
	void autoExamFailedMessage(String message);
	void registerListener(ExamUIEventsListener listener);
	void manualExamFailedMessage(String message);
	void addAnswerToUI(String value , int index, int indexRow, ComboBox<String>all);
	void removeAnswerFromUI(String value, int index, int indexRow);
	void addOpenQuestionToUI(String string, int i);
	void addAmericanQuestionToUI(int id, String theQuestion, ArrayList<String> allAnswers);
	void showWindow(int i);
	void closeWindow(int i);
	void addSAnswerToUI(int numOfQuestion, String theOption, int idAns, int numOfAnswersBefor);
	void removeSpecificAnswerFromUI(int numOfAns, int numOfQ, String theOption, String theQuestion);
	void removeQuestionFromUI(int numOfQ, String theQuestion);
}
