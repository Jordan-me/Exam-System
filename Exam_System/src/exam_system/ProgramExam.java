//Name: Yarden Dahan.
//ID:208730523
package exam_system;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import exceptions.AnswerIsNotFoundException;
import exceptions.IllegalInputException;
import exceptions.InputNotInOptions;
import exceptions.NegativeInputException;
import exceptions.NoAnswerException;
import exceptions.TooManyAnwersOption;
import validation.ValidateFunc;

public class ProgramExam {

	public static void main(String[] args) {
		begin();

	}

	public static void begin() {
		Scanner scanner = new Scanner(System.in);
		try {
			ExamGenerator exG = new ExamGenerator();
			boolean run = true;
			while (run) {
				System.out.println("welcome to the exam generator please choose an option: \n"
						+ "enter 1 to update the Data Base \n" + "enter 2 to manually create an exam \n"
						+ "enter 3 to randomly create an exam \n " + "enter 0 to exit the program");
				int choice = ValidateFunc.setSwitchCaseChoice(scanner.next());
				switch (choice) {

				case 1:
					System.out.println("please enter your choice \n" + "to add new question enter 1 \n"
							+ "to add an answer to a specific question enter 2 \n" + "to go back to main menu enter 0");
					int innerChoice = ValidateFunc.setSwitchCaseChoice2(scanner.next());
					switch (innerChoice) {
					case 1:
						scanner.nextLine();
						System.out.println(" please enter the question:");
						String theQuestion = scanner.nextLine();
						System.out.println("choose an option: \n" + "to add an american Question enter 1 \n"
								+ "to add an open question enter 2");
						int questionChoice = ValidateFunc.setSwitchCaseChoice(scanner.next());
						switch (questionChoice) {
						case 1:
							AmericanQuestion newQuestion = new AmericanQuestion(theQuestion);

							for (int i = 0; i < 4; i++) {
								scanner.nextLine();
								System.out.println("please enter answer number " + (i + 1) + ":");
								String answer = scanner.nextLine();
								Answer newAnswer = new Answer(answer);

								System.out
										.println("please choose an option: \n" + "enter 1 if this answer is correct \n"
												+ "enter 2 of this answer is incorrect");
								int isCorrect = ValidateFunc.setSwitchCaseChoice(scanner.next());
								if (isCorrect == 1) {
									newAnswer.setIsCorrect(true);
								} else if (isCorrect == 2) {
									newAnswer.setIsCorrect(false);
								}

								newQuestion.addAnswer(newAnswer);
							}
							exG.addQuestionToDB(newQuestion,exG.getNumOfallQuestionDB());

							break;
						case 2:
							scanner.nextLine();
							System.out.println("please enter the solution for the question");
							String solution = scanner.nextLine();
							OpenQuestion openQuestion = new OpenQuestion(theQuestion, new Answer(solution));
							exG.addQuestionToDB(openQuestion);
							break;
						default:
							System.out.println("back to the main session");
							break;
						}
						break;
					case 2:
						System.out.println("please choose the American Question you want to add an answer to - \n"
								+ "note that you cannot add answers to an Open Question");
						exG.showAmericanQuestion();
						int qToAddAnswer = scanner.nextInt();
						scanner.nextLine();
						int maxQ = exG.getNumberOfAmericanQuestion();
						if (ValidateFunc.validate(qToAddAnswer, maxQ)) {
							int numOfq = qToAddAnswer;
							System.out.println("the question you chose is: \n"
									+ exG.getAmericanQuestionByIndex(numOfq).toString());
							System.out.println("please enter the new Answer");
							String answer = scanner.nextLine();
							exG.addAnswerToSpecifiecQuestionToDB(numOfq, answer);
						} else {
							throw new InputNotInOptions();
						}
					case 0:
						break;

					}
					break;
				case 2:
					exG.showQuestion();
					ArrayList<Question> listOfQuestions = exG.chosenQuestion();
					exG.buildExamManually(listOfQuestions);

					break;
				case 3:
					System.out.println("how many questions would you like to have?");
					String numOfquestions = scanner.next();
					exG.buildAutoExam(numOfquestions);
					break;
				case 0:
					System.out.println("Bye-Bye!");
					run = false;
				}
			}
		} catch (InputNotInOptions | IllegalInputException | NoAnswerException | AnswerIsNotFoundException e1) {
			System.out.println(e1.getMessage());
			begin();
		} catch (TooManyAnwersOption | IOException | IndexOutOfBoundsException | InputMismatchException
				| NegativeInputException e2) {
			System.out.println(e2.getMessage());
			begin();
		}

	}

}