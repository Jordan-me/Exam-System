package listeners;

import java.util.ArrayList;

import javafx.scene.control.ComboBox;

public interface ExamUIEventsListener {

	void creatAutoExamToUI(String text);

	void showWindow(int i);

	String getQuestionToUIByIndex(int i);

	int getNumOfQuestions();

	int getNumOfAmericanQuestions();

	int getNumOfAnswers(int i);

	ArrayList<String> getAnswersToQuestion(int i) ;

	void faildMessageAddingAnswer();

	void addAnswerToToModelEvent(String value , int index, ComboBox<String> allAnswers);

	void removeAnswerFromModelEvent(String string , int index, int indexRow);

	int getIndexOfQuestionToUI(String text);

	void faildMessageAddingExistingAnswer();

	void faildCreatingExam();

	void addOpenQuestionToModelExam(String tQues, String sQues, int id);
 
	void buildExamManually(ArrayList<Integer> allQuestionChosen, ArrayList<ArrayList<Integer>> syncAnsr);

	void addAmericanQuestionToUI(int id, String theQ, ArrayList<String> allOptionsAQ, ArrayList<Integer> solution);

	void closeWindow(int i);

	void addSpecificAnswerToUI(int numOfQuestion, String theOption, Boolean isCorrect);

	void removeAnswerFromUI(String theOption, int numOfQ);

	void removeQuestionFromUI(String theQuestion, int numOfQ);




	

}
