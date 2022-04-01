package exam_system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import exceptions.AnswerIsNotFoundException;
import exceptions.IllegalInputException;
import exceptions.InputIsNullException;
import exceptions.InputNotInOptions;
import exceptions.NegativeInputException;
import exceptions.NoAnswerException;
import exceptions.TooManyAnwersOption;
import listeners.ExamEventsListener;
import validation.ValidateFunc;

public class ExamGenerator implements IExamGenerator {
	private ArrayList<Question> allQuestionDB;
	private ArrayList<AmericanQuestion> allAmericanQuestionDB;
	private Vector<ExamEventsListener> listeners;
	private static int numAQ;
	private static int MaxQuestionsDB;
	private static File fileDB = new File("DataBase.txt");
	private static int counter = 1;

	public ExamGenerator() throws FileNotFoundException, IllegalInputException {
		allQuestionDB = new ArrayList<Question>();
		allAmericanQuestionDB = new ArrayList<AmericanQuestion>();
		listeners = new Vector<ExamEventsListener>();
		setQuestion();
		allAmericanQuestionDB = getAmericanQuestion();
		setMaxQuestionsDB();
		numAQ = allAmericanQuestionDB.size();

	}

	public ExamGenerator(ArrayList<Question> allQuestionDB) throws FileNotFoundException, IllegalInputException {
		this.allQuestionDB = allQuestionDB;
		allAmericanQuestionDB = new ArrayList<AmericanQuestion>();
		listeners = new Vector<ExamEventsListener>();
		allAmericanQuestionDB = getAmericanQuestion();
		setMaxQuestionsDB();
		numAQ = allAmericanQuestionDB.size();

	}

	public static void insertOption(Scanner s, AmericanQuestion q)
			throws InputIsNullException, IllegalInputException, NegativeInputException, InputNotInOptions {
		boolean fcontinue = true;
		System.out.println("Your current question is: \n" + q.toString());
		while (fcontinue) {
			s.nextLine();
			System.out.println("please insert your option to the question: ");
			String input = s.nextLine();
			ValidateFunc.validScanner(input);
			System.out.println("insert your option: ");
			input = s.nextLine();
			Answer theAnswer = new Answer(input);
			q.addAnswer(theAnswer);
			System.out.println("enter another one?");
			fcontinue = ValidateFunc.setChoice(s);

		}
	}

	@Override
	public void addQuestionToDB(OpenQuestion q, int i) throws IOException {
		ArrayList<String> allFileContent = FileHelperManger.readFromFile(fileDB);
		String rgx = ";";
		String typeQues = "<OpenQuestion>";
		allFileContent.add(q.getTheQuestion() + rgx + typeQues);
		allFileContent.add(q.getSolution().getTheAnswer());
		FileHelperManger.write(fileDB, allFileContent);
		allQuestionDB.add(q);
		setMaxQuestionsDB();
		fireAddQuestionEvent(q.getTheQuestion(), i);

	}

	public void fireAddQuestionEvent(String theQuestion, int i) {
		for (ExamEventsListener l : listeners) {
			l.addQuestionToModelEvent(theQuestion, i);
		}

	}

	@Override
	public void addQuestionToDB(AmericanQuestion q, int id)
			throws AnswerIsNotFoundException, NoAnswerException, IOException {
		ArrayList<String> allFileContent = FileHelperManger.readFromFile(fileDB);
		ArrayList<String> allAnswers = new ArrayList<String>();
		String rgx = ";";
		String sepRgx = ", ";
		String typeQues = "<AmericanQuestion>";
		StringBuilder dataAnswerLine = new StringBuilder();
		allFileContent.add(q.getTheQuestion() + rgx + typeQues);
		for (int i = 0; i < q.getNumOfAnswers(); i++) {
			dataAnswerLine.append(q.getAnswerByIndex(i).getTheAnswer() + rgx);
			allAnswers.add(q.getAnswerByIndex(i).getTheAnswer());
			if (q.getAnswerByIndex(i).getIsCorrect()) {
				dataAnswerLine.append("true");
			} else {
				dataAnswerLine.append("false");
			}
			if (i != q.getNumOfAnswers() - 1) {
				dataAnswerLine.append(sepRgx);
			}
		}
		allFileContent.add(dataAnswerLine.toString());
		FileHelperManger.write(fileDB, allFileContent);
		allQuestionDB.add(q);
		MaxQuestionsDB++;
		fireAddAmericanQuestionEvent(id, q.getTheQuestion(), allAnswers);
	}

	public void fireAddAmericanQuestionEvent(int id, String theQuestion, ArrayList<String> allAnswers) {
		for (ExamEventsListener l : listeners) {
			l.addAmericanQuestionToModelEvent(id, theQuestion, allAnswers);
		}

	}

	public void setQuestion() throws FileNotFoundException, IllegalInputException {
		if (allQuestionDB.isEmpty()) {
			allQuestionDB.clear();
			Scanner in = new Scanner(fileDB);
			while (in.hasNextLine()) {
				String questionLine = in.nextLine();
				String[] question = questionLine.split(";");
				if (questionLine.contains("AmericanQuestion")) {
					AmericanQuestion q = new AmericanQuestion(question[0]);
					String optionLine = in.nextLine();
					String[] options = optionLine.split(",");
					boolean isCorrect;
					for (int i = 0; i < options.length; i++) {
						String[] answer = options[i].split(";");
						if (answer[1].equalsIgnoreCase("true")) {
							isCorrect = true;
						} else {
							isCorrect = false;
						}
						Answer a = new Answer(answer[0], isCorrect);
						q.addAnswer(a);
					}
					allQuestionDB.add(q);
				} else if (questionLine.contains("OpenQuestion")) {
					Answer a = new Answer(in.nextLine());
					OpenQuestion q = new OpenQuestion(question[0], a);
					allQuestionDB.add(q);
				}
			}
			in.close();
		}
	}

	public ArrayList<Question> getQuestion() {
		return allQuestionDB;
	}

	@Override
	public void showQuestion() throws FileNotFoundException, IllegalInputException {
		int index = 1;
		for (Question q : allQuestionDB) {
			if (q instanceof AmericanQuestion) {
				Question aq = (AmericanQuestion) q;
				System.out.println(index + ") " + aq.toString());
			} else if (q instanceof OpenQuestion) {
				OpenQuestion op = (OpenQuestion) q;
				System.out.println(index + ") " + op.getTheQuestion());
			}
			index++;
		}
	}

	// cast IntegerList to an AnswerList
	@Override
	public ArrayList<Answer> getChosenOption(ArrayList<Integer> options, AmericanQuestion aq)
			throws AnswerIsNotFoundException, NoAnswerException, IndexOutOfBoundsException {
		Scanner s = new Scanner(System.in);
		ArrayList<Answer> chosenOption = new ArrayList<Answer>();
		for (int i = 0; i < options.size(); i++) {
			chosenOption.add(aq.getAnswerByIndex(options.get(i)));
		}
		addBuildInOption(1, chosenOption);
		s.close();
		return chosenOption;
	}

	// Adds pre-built answers
	public void addBuildInOption(int i, ArrayList<Answer> chosenOption) {
		Answer a1 = new Answer("No answer is true.");
		chosenOption.add(a1);
		if (i == 1) {
			Answer a2 = new Answer("More than one correct answer.");
			chosenOption.add(a2);
		}
	}

	// Builds a manual test with the help of the user
	@Override
	public void buildExamManually(ArrayList<Question> chosenQuestion) throws InputMismatchException,
			TooManyAnwersOption, AnswerIsNotFoundException, NoAnswerException, FileNotFoundException {
		Scanner s = new Scanner(System.in);
		ArrayList<Question> QExam = new ArrayList<Question>();
		for (int i = 0; i < chosenQuestion.size(); i++) {
			Question q = chosenQuestion.get(i);
			if (q instanceof OpenQuestion) {
				OpenQuestion op = (OpenQuestion) q;
				QExam.add(op);
			} else if (q instanceof AmericanQuestion) {
				AmericanQuestion aq = (AmericanQuestion) q;
				System.out.println(aq.toString());
				System.out.println("How many options would you like for the question?");
				int length = s.nextInt();
				while (!(ValidateFunc.validationQA(length, aq))) {
					length = s.nextInt();
				}
				ArrayList<Integer> options = new ArrayList<>(length);
				System.out.println("Type the options numbers of the question you want:");
				for (int j = 0; j < length; j++) {
					Integer chosenOption = s.nextInt();
					while (!(ValidateFunc.validationQA(chosenOption, aq)) || options.contains(chosenOption)) {
						if (options.contains(chosenOption)) {
							System.out.println("Select an unpicked option");
						}
						chosenOption = s.nextInt();
					}
					options.add(chosenOption);
				}
				ArrayList<Answer> manualAnswers = getChosenOption(options, aq);
				aq.setAllAnswerOption(manualAnswers);
				generateCorrectAnswer(aq);
				QExam.add(aq);
			}
		}
		Exam manuallyExam = new Exam(QExam);
		creatTheExam(manuallyExam);
		creatTheSolution(manuallyExam);
		System.out.println("The test was created successfully.");
		s.close();
	}

	// Creates a test solution file
	private void creatTheSolution(Exam manuallyExam) throws FileNotFoundException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");
		String fileName = ("ExamSolution_" + manuallyExam.getExamDate().format(dtf) + "(" + counter + ")" + ".txt");
		counter++;
		File file = new File(fileName);
		PrintWriter pw = new PrintWriter(file);
		manuallyExam.saveSolution(pw);
		pw.close();
	}

	// Creates a test file
	private void creatTheExam(Exam manuallyExam) throws FileNotFoundException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");
		String fileName = ("Exam_" + manuallyExam.getExamDate().format(dtf) + "(" + counter + ")" + ".txt");
		File file = new File(fileName);
		PrintWriter pw = new PrintWriter(file);
		manuallyExam.saveExam(pw);
		pw.close();
	}

	// Creates a test in a lottery of questions and answers
	@Override
	public void buildAutoExam(String numOfQuestions) throws IndexOutOfBoundsException, AnswerIsNotFoundException,
			NoAnswerException, FileNotFoundException, IllegalInputException {
		if (!(ValidateFunc.validationAutoExam(numOfQuestions))) {
			throw new IllegalInputException();
		}
		int selectNum = Integer.parseInt(numOfQuestions);
		ArrayList<Integer> aL = new ArrayList<Integer>();
		ArrayList<Question> QExam = new ArrayList<Question>();
		Random randomGenerator = new Random();
		for (int i = 0; i < selectNum; i++) {
			int questionNumber = randomGenerator.nextInt(allQuestionDB.size());
			Question q = allQuestionDB.get(questionNumber);
			while (aL.contains(questionNumber) || q instanceof OpenQuestion) {
				questionNumber = randomGenerator.nextInt(allQuestionDB.size());
				q = allQuestionDB.get(questionNumber);
			}
			aL.add(questionNumber);
			AmericanQuestion currentQuestion = (AmericanQuestion) allQuestionDB.get(questionNumber);

			while (currentQuestion.getNumOfAnswers() >= 4) {
				int random = randomGenerator.nextInt(currentQuestion.getNumOfAnswers());
				if (currentQuestion.getAnswerByIndex(random) != null) {
					currentQuestion.deleteSpecificAnswer(random);
				}
			}
			ArrayList<Answer> chosenOption = currentQuestion.getAllAnswerOption();
			addBuildInOption(0, chosenOption);
			generateCorrectAnswer(currentQuestion);
			QExam.add(currentQuestion);
		}
		Exam autoExam = new Exam(QExam);
		creatTheExam(autoExam);
		creatTheSolution(autoExam);
		System.out.println("The test was created successfully");
	}

	// Initializes built-in answers to be correct
	private void generateCorrectAnswer(AmericanQuestion currentQuestion)
			throws AnswerIsNotFoundException, NoAnswerException {
		int numOfCorrectAnswers = (currentQuestion.getTheCorrectAnswer().size());
		boolean isCorrect = true;
		for (int i = 0; i < currentQuestion.getNumOfAnswers(); i++) {
			Answer ansForCheck = currentQuestion.getAnswerByIndex(i);
			if (numOfCorrectAnswers == 0) {
				if ((ansForCheck.getTheAnswer()).contains("No answer is true")) {
					ansForCheck.setIsCorrect(isCorrect);
				}
			} else if (numOfCorrectAnswers > 1) {
				if ((ansForCheck.getTheAnswer()).contains("More than one correct answer")) {
					ansForCheck.setIsCorrect(isCorrect);
				}
			}
		}
	}

	// TODO:change it with input
	// will cast integerList type to question type
	public ArrayList<Question> chosenQuestion() throws InputMismatchException, IndexOutOfBoundsException {
		Scanner s = new Scanner(System.in);
		ArrayList<Question> chosenQuestion = new ArrayList<Question>();
		ArrayList<Integer> numOfQuestions = new ArrayList<Integer>();
		System.out.println("How many questions would you like on the test?");
		String numOfQuestion = s.next();
		while (!(ValidateFunc.validationAutoExam(numOfQuestion))) {
			numOfQuestion = s.next();
		}
		int selectNum = Integer.parseInt(numOfQuestion);
		for (int i = 0; i < selectNum; i++) {
			System.out.println("Insert number of question");
			String num = s.next();
			while (numOfQuestions.contains(num) || !(ValidateFunc.validationAutoExam(num))) {
				System.out.println("insert again");
				num = s.next();
			}
			int selectNum2 = Integer.parseInt(num);
			chosenQuestion.add(allQuestionDB.get(selectNum2 - 1));
		}
		s.close();
		return chosenQuestion;
	}

//ForGui
	public void creatExamManuallyToUI(ArrayList<Integer> indexOfques, ArrayList<ArrayList<Integer>> syncAnsr)
			throws InputMismatchException, IndexOutOfBoundsException, AnswerIsNotFoundException, NoAnswerException,
			TooManyAnwersOption, FileNotFoundException {
		ArrayList<Question> QExam = new ArrayList<Question>();
		for (int i = 0; i < indexOfques.size(); i++) {
			Question q = getQuestionByIndex(indexOfques.get(i) + 1);
			if (q instanceof OpenQuestion) {
				QExam.add(q);
			} else if (q instanceof AmericanQuestion) {
				AmericanQuestion aqCopy = new AmericanQuestion(q.getTheQuestion());
				AmericanQuestion aqOrigin = (AmericanQuestion) q;
				ArrayList<Integer> ansTOpars = new ArrayList<Integer>();
				ansTOpars.addAll(syncAnsr.get(i));
				ArrayList<Answer> manualAnswers = getChosenOption(ansTOpars, aqOrigin);
				aqCopy.setAllAnswerOption(manualAnswers);
				generateCorrectAnswer(aqCopy);
				QExam.add(aqCopy);
			}
		}
		Exam manuallyExam = new Exam(QExam);
		creatTheExam(manuallyExam);
		creatTheSolution(manuallyExam);
		System.out.println("The test was created successfully.");

	}

	public int getIndexByAnswer(String string, Integer in) {
		AmericanQuestion aq = allAmericanQuestionDB.get(in);
		int numOfAns = aq.getIndexByAnswer(string);
		return numOfAns;
	}

	public Question getQuestionByIndex(int i) {
		int index = 0;
		for (Question q : allQuestionDB) {
			if (index == (i - 1)) {
				if (q instanceof AmericanQuestion) {
					AmericanQuestion aq = (AmericanQuestion) q;
					return aq;

				} else if (q instanceof OpenQuestion) {
					OpenQuestion oq = (OpenQuestion) q;
					return oq;
				}
			}
			index++;
		}
		return null;

	}

	public Question getAmericanQuestionByIndex(int i) {
		return allAmericanQuestionDB.get(i - 1);
	}

	@Override
	public void addAnswerToSpecifiecQuestionToDB(int numOfQuestion, String ans)
			throws NegativeInputException, InputNotInOptions, IllegalInputException {
		Scanner s = new Scanner(System.in);
		boolean isCorrect;
		Question q1 = allQuestionDB.get(numOfQuestion - 1);
		if (q1 instanceof OpenQuestion) {
			s.close();
			throw new InputNotInOptions();
		}
		AmericanQuestion q = (AmericanQuestion) allQuestionDB.get(numOfQuestion - 1);
		System.out.println("this option is true?");
		if (ValidateFunc.setChoice(s)) {
			isCorrect = true;
		} else {
			isCorrect = false;
		}
		Answer ansToAdd = new Answer(ans, isCorrect);
		q.addAnswer(ansToAdd);
	}

	public void setMaxQuestionsDB() {
		MaxQuestionsDB = allQuestionDB.size();
	}

	public static int getMaxQuestionsDB() {
		return MaxQuestionsDB;
	}

	public int getNumOfallQuestionDB() {
		return allQuestionDB.size();
	}

	public void showAmericanQuestion() {
		int index = 1;
		for (Question q : allQuestionDB) {
			if (q instanceof AmericanQuestion) {
				Question aq = (AmericanQuestion) q;
				System.out.println(index + ") " + aq.toString());
			} else if (q instanceof OpenQuestion) {
				continue;
			}
			index++;
		}

	}

	public int getNumberOfAmericanQuestion() {
		int counter = 0;
		for (Question q : allQuestionDB) {
			if (q instanceof AmericanQuestion) {
				AmericanQuestion aq = (AmericanQuestion) q;
				if (aq.getNumOfAnswers() >= 3) {
					counter++;
				}
			}
		}
		return counter;
	}

	public ArrayList<AmericanQuestion> getAmericanQuestion() {
		for (Question q : allQuestionDB) {
			if (q instanceof AmericanQuestion) {
				AmericanQuestion aq = (AmericanQuestion) q;
				allAmericanQuestionDB.add(aq);
			}
		}
		return allAmericanQuestionDB;

	}

	public static int getNumAQ() {
		return numAQ;
	}

	public void registerListener(ExamEventsListener listener) {
		listeners.add(listener);
	}

	public int getNumOfOptions(int i) {
		Question q = getQuestionByIndex(i);
		int num = 0;
		if (q instanceof AmericanQuestion) {
			AmericanQuestion aq = (AmericanQuestion) q;
			num = aq.getNumOfAnswers();
		}
		return num;
	}

	public ArrayList<String> getAllOfOptions(int i) throws AnswerIsNotFoundException, NoAnswerException {
		Question q = getQuestionByIndex(i);
		ArrayList<String> allAnswers = new ArrayList<String>();
		int num = 0;
		if (q instanceof AmericanQuestion) {
			AmericanQuestion aq = (AmericanQuestion) q;
			num = aq.getNumOfAnswers();
			for (int j = 0; j < num; j++) {
				allAnswers.add(aq.getAnswerByIndex(j).getTheAnswer());
			}
		}
		return allAnswers;
	}

	public int getIndexByAnswer(ArrayList<String> allOfOptions, String value) {
		for (int i = 0; i < allOfOptions.size(); i++) {
			if (allOfOptions.get(i).equalsIgnoreCase(value)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexByQuestion(String text) {
		int index = 0;
		for (Question q : allQuestionDB) {
			if (q instanceof AmericanQuestion) {
				AmericanQuestion aq = (AmericanQuestion) q;
				if (aq.getTheQuestion().equalsIgnoreCase(text)) {
					return index;
				}
			} else if (q instanceof OpenQuestion) {

				OpenQuestion oq = (OpenQuestion) q;
				if (oq.getTheQuestion().equals(text)) {
					return index;
				}
			}
			index++;
		}
		return -1;
	}

	@Override
	public void addQuestionToDB(OpenQuestion q) throws FileNotFoundException, IOException {

	}

	public void startTheProgram(int i) {
		for (ExamEventsListener l : listeners) {
			l.fireShowWindow(i);
		}

	}

	public void finishTheProgram(int i) {
		for (ExamEventsListener l : listeners) {
			l.fireCloseWindow(i);
		}

	}

	public void addAnswerTAQ(int numOfQuestion, String theOption, Boolean isCorrect) throws IllegalInputException {
		Answer additiAnswer = new Answer(theOption, isCorrect);
		Question q1 = allQuestionDB.get(numOfQuestion);
		AmericanQuestion aq = (AmericanQuestion) q1;
		int numOfAnsBefor = aq.getNumOfAnswers();
		aq.addAnswer(additiAnswer);
		int idAns = aq.getNumOfAnswers();
		for (ExamEventsListener l : listeners) {
			l.addAnswerToModelEvent(numOfQuestion, theOption, idAns, numOfAnsBefor);
		}

	}

	public void removeSpecificAnswer(String theOption, int numOfQ) {
		Question q = allQuestionDB.get(numOfQ);
		AmericanQuestion aq = (AmericanQuestion) q;
		int numOfAns = aq.getIndexByAnswer(theOption);
		aq.deleteSpecificAnswer(numOfAns);
		int numAq = allAmericanQuestionDB.indexOf(aq);
		allAmericanQuestionDB.remove(aq);
		allAmericanQuestionDB.add(numAq, aq);
		allQuestionDB.remove(numOfQ);
		allQuestionDB.add(numOfQ, aq);
		for (ExamEventsListener l : listeners) {
			l.fireRemoveAnswer(numOfAns, numOfQ, theOption, aq.getTheQuestion());
		}

	}

	public void removeQuestion(String theQuestion, int numOfQ) {
		Question q = allQuestionDB.get(numOfQ);
		AmericanQuestion aq = (AmericanQuestion) q;
		allQuestionDB.remove(numOfQ);
		allAmericanQuestionDB.remove(aq);
		for (ExamEventsListener l : listeners) {
			l.fireRemoveQuestion(numOfQ, theQuestion);
		}

	}

}
