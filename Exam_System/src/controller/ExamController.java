package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import exam_system.AmericanQuestion;
import exam_system.Answer;
import exam_system.ExamGenerator;
import exam_system.OpenQuestion;
import exam_system.Question;
import exceptions.AnswerIsNotFoundException;
import exceptions.IllegalInputException;
import exceptions.NoAnswerException;
import exceptions.TooManyAnwersOption;
import javafx.scene.control.ComboBox;
import listeners.ExamEventsListener;
import listeners.ExamUIEventsListener;
import view.AbstractExamView;

public class ExamController implements ExamEventsListener, ExamUIEventsListener {
	private ExamGenerator examModel;
	private AbstractExamView examView;
	private Map<String, ArrayList<String>> allQuestionToUI = new LinkedHashMap<String, ArrayList<String>>();
	private Map<String, ArrayList<String>> leftOvers = new LinkedHashMap<String, ArrayList<String>>();

	public ExamController(ExamGenerator model, AbstractExamView view) {

		examModel = model;
		examView = view;

		examModel.registerListener(this);
		examView.registerListener(this);
	}

	@Override
	public void creatAutoExamToUI(String text) {
		try {
			examModel.buildAutoExam(text);
			JOptionPane.showMessageDialog(null, "done!");
		} catch (IndexOutOfBoundsException | FileNotFoundException | AnswerIsNotFoundException | NoAnswerException
				| IllegalInputException e) {
			failedAddingAutoExamToModelEvent(e.getMessage());
		}

	}

	@Override
	public void failedAddingAutoExamToModelEvent(String message) {
		examView.autoExamFailedMessage(message);

	}

	public int getNumberOfAQuestionsToUI() throws FileNotFoundException, IllegalInputException {
		int numAQ = examModel.getNumberOfAmericanQuestion();
		return numAQ;
	}

	public void setAllQuestionToUI() {
		int numOfQuestion = getNumOfQuestions();
		for (int i = 0; i < numOfQuestion; i++) {
			ArrayList<String> allAnswersToQues = getAnswersToQuestion(i);
			String textQues = getQuestionToUIByIndex(i);
			allQuestionToUI.put(textQues, allAnswersToQues);
		}
		allQuestionToUI.putAll(leftOvers);

	}

	public Map<String, ArrayList<String>> getAllQuestionToUi() {
		setAllQuestionToUI();
		return allQuestionToUI;
	}

	public ArrayList<Question> getTheQuesDB() {

		return examModel.getQuestion();
	}

	@Override
	public String getQuestionToUIByIndex(int i) {
		String strCBox = examModel.getQuestionByIndex(i + 1).getTheQuestion();
		return strCBox;
	}

	@Override
	public int getNumOfQuestions() {
		int num = examModel.getNumOfallQuestionDB();
		return num;
	}

	@Override
	public int getNumOfAmericanQuestions() {
		return examModel.getNumberOfAmericanQuestion();
	}

	@Override
	public void failedAddingManualExamToModelEvent(String message) {
		examView.manualExamFailedMessage(message);

	}

	@Override
	public int getNumOfAnswers(int i) {
		return examModel.getNumOfOptions(i + 1);
	}

	@Override
	public ArrayList<String> getAnswersToQuestion(int i) {
		ArrayList<String> all = new ArrayList<String>();
		try {
			all = examModel.getAllOfOptions(i + 1);
		} catch (AnswerIsNotFoundException | NoAnswerException e) {
			System.out.println(e.getMessage());
		}
		return all;
	}

	@Override
	public void faildMessageAddingAnswer() {
		examView.manualExamFailedMessage("First choose something");
	}

	@Override
	public void addAnswerToToModelEvent(String value, int indexRow, ComboBox<String> allAns) {
		try {
			String onlyTxt = getTheText(value);
			int idLb = examModel.getIndexByAnswer(examModel.getAllOfOptions(indexRow + 1), onlyTxt);
			String newString = parseToCorrect(value, idLb);
			examView.addAnswerToUI(newString, idLb, indexRow, allAns);

		} catch (AnswerIsNotFoundException | NoAnswerException e) {
			System.out.println(e.getMessage());
		}
	}

	public String getTheText(String value) {
		String[] vals = value.split("\\)");
		String onlyTxt = vals[1];
		return onlyTxt;
	}

	public String parseToCorrect(String value, int idLb) {
		String[] vals = value.split("\\)");
		String correct = "" + idLb + ")" + vals[1];
		return correct;

	}

	@Override
	public void removeAnswerFromModelEvent(String value, int index, int indexRow) {
		examView.removeAnswerFromUI(value, index, indexRow);

	}

	@Override
	public int getIndexOfQuestionToUI(String text) {
		int intla = examModel.getIndexByQuestion(text);
		return intla;
	}

	@Override
	public void faildMessageAddingExistingAnswer() {
		examView.manualExamFailedMessage("CHOOSE SOMETHING ELSE!");

	}

	public List<String> mappingQuestion() {
		List<String> allQuest = new ArrayList<String>();
		int numOfQuestion = getNumOfQuestions();
		for (int i = 0; i < numOfQuestion; i++) {
			String textQues = getQuestionToUIByIndex(i);
			allQuest.add(textQues);
		}
		return allQuest;
	}

	public Map<Integer, List<String>> mappingAnswer() {
		Map<Integer, List<String>> allMappingAnswer = new LinkedHashMap<Integer, List<String>>();
		int numOfQuestion = getNumOfQuestions();
		for (int i = 0; i < numOfQuestion; i++) {
			ArrayList<String> allAnswersToQues = getAnswersToQuestion(i);
			allMappingAnswer.put(i, allAnswersToQues);
		}
		return allMappingAnswer;
	}

	@Override
	public void faildCreatingExam() {
		examView.manualExamFailedMessage("Something went Wrong...");

	}

	@Override
	public void addOpenQuestionToModelExam(String tQues, String sQues, int id) {
		OpenQuestion q = new OpenQuestion(tQues, sQues);
		try {
			examModel.addQuestionToDB(q, id);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	@Override
	public void buildExamManually(ArrayList<Integer> allQuestionChosen, ArrayList<ArrayList<Integer>> syncAnsr) {
		try {
			examModel.creatExamManuallyToUI(allQuestionChosen, syncAnsr);
			examView.manualExamFailedMessage("Done");
		} catch (InputMismatchException | IndexOutOfBoundsException | FileNotFoundException | AnswerIsNotFoundException
				| NoAnswerException | TooManyAnwersOption e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

	}

	@Override
	public void addQuestionToModelEvent(String theQuestion, int i) {
		examView.addOpenQuestionToUI(theQuestion, i);

	}

	@Override
	public void addAmericanQuestionToUI(int id, String theQ, ArrayList<String> allOptionsAQ,
			ArrayList<Integer> solution) {
		try {
			AmericanQuestion q = new AmericanQuestion(theQ);
			for (int i = 0; i < allOptionsAQ.size(); i++) {
				Answer thisGirl = new Answer(allOptionsAQ.get(i));
				for (int j = 0; j < solution.size(); j++) {
					if (solution.get(j) == i) {
						thisGirl.setIsCorrect(true);
					} else {
						thisGirl.setIsCorrect(false);
					}
				}

				q.addAnswer(thisGirl);
			}
			examModel.addQuestionToDB(q, id);
		} catch (IllegalInputException | AnswerIsNotFoundException | NoAnswerException | IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

	}

	@Override
	public void addAmericanQuestionToModelEvent(int id, String theQuestion, ArrayList<String> allAnswers) {
		examView.addAmericanQuestionToUI(id, theQuestion, allAnswers);

	}

	@Override
	public void showWindow(int i) {
		examModel.startTheProgram(i);

	}

	@Override
	public void fireShowWindow(int i) {
		examView.showWindow(i);

	}

	@Override
	public void closeWindow(int i) {
		examModel.finishTheProgram(i);

	}

	@Override
	public void fireCloseWindow(int i) {
		examView.closeWindow(i);

	}

	@Override
	public void addSpecificAnswerToUI(int numOfQuestion, String theOption, Boolean isCorrect) {
		try {
			examModel.addAnswerTAQ(numOfQuestion, theOption, isCorrect);
		} catch (IllegalInputException e) {
			System.out.println(e.getMessage());
			;
		}

	}

	@Override
	public void addAnswerToModelEvent(int numOfQuestion, String theOption, int idAns, int numOfAnswersBefor) {
		examView.addSAnswerToUI(numOfQuestion, theOption, idAns, numOfAnswersBefor);

	}

	@Override
	public void removeAnswerFromUI(String theOption, int numOfQ) {
		examModel.removeSpecificAnswer(theOption, numOfQ);

	}

	@Override
	public void fireRemoveAnswer(int numOfAns, int numOfQ, String theOption, String theQuestion) {
		examView.removeSpecificAnswerFromUI(numOfAns, numOfQ, theOption, theQuestion);

	}

	@Override
	public void removeQuestionFromUI(String theQuestion, int numOfQ) {
		examModel.removeQuestion(theQuestion, numOfQ);

	}

	@Override
	public void fireRemoveQuestion(int numOfQ, String theQuestion) {
		examView.removeQuestionFromUI(numOfQ, theQuestion);

	}

}
