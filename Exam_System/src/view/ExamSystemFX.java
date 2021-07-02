package view;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;

import exam_system.ExamGenerator;
import exceptions.IllegalInputException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import listeners.ExamEventsListener;
import listeners.ExamUIEventsListener;

public class ExamSystemFX implements AbstractExamView {
	private Vector<ExamUIEventsListener> allListeners = new Vector<ExamUIEventsListener>();
	private Stage theStage;
	private Label lbNumQuestion = new Label();

	public ExamSystemFX(Stage theStage) throws FileNotFoundException, IllegalInputException {
		this.theStage = theStage;
		theStage.setTitle("welcome!");
		ToggleGroup tglOption = new ToggleGroup();
		RadioButton rdbAutoOption = new RadioButton("Create auto exam");
		RadioButton rdbManualOption = new RadioButton("Create manually exam");
		rdbAutoOption.setToggleGroup(tglOption);
		rdbManualOption.setToggleGroup(tglOption);
		VBox vbRoot = new VBox();
		vbRoot.setSpacing(10);
		vbRoot.setPadding(new Insets(10));
		vbRoot.setAlignment(Pos.CENTER_LEFT);

		TextField fieldForInt = new TextField();
		fieldForInt.setVisible(false);
		fieldForInt.setMaxWidth(40);
		HBox hbRoot = new HBox();
		hbRoot.setPadding(new Insets(10));
		hbRoot.setAlignment(Pos.CENTER_LEFT);
		hbRoot.setSpacing(10);
		hbRoot.getChildren().addAll(lbNumQuestion, fieldForInt);
		Button btnExecutive = new Button("Start create Exam");
		btnExecutive.setVisible(false);
		fieldForInt.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					fieldForInt.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
		rdbAutoOption.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				lbNumQuestion.setText("Number of questions (1-" + getNumberOfAQuestionsToUI() + ") :");
				fieldForInt.setVisible(true);
				btnExecutive.setVisible(true);
				btnExecutive.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						int numFromField = Integer.parseInt(fieldForInt.getText());
						for (ExamUIEventsListener listen : allListeners) {
							if (fieldForInt.getText().isEmpty() || numFromField < 1
									|| numFromField > getNumberOfAQuestionsToUI()) {
								listen.faildCreatingExam();
								lbNumQuestion.setTextFill(Color.RED);
							} else {
								lbNumQuestion.setTextFill(Color.BLACK);

								listen.creatAutoExamToUI(fieldForInt.getText());
								fieldForInt.setText("");
							}
						}
					}

				});

			}

		});
		rdbManualOption.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				fieldForInt.setVisible(false);
				lbNumQuestion.setText("To get started click on the Start button");
				lbNumQuestion.setTextFill(Color.BLACK);
				btnExecutive.setVisible(true);
				btnExecutive.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						for (ExamUIEventsListener listen : allListeners) {
							listen.showWindow(1);
							listen.closeWindow(0);
						}
						fieldForInt.setText("");
						btnExecutive.setVisible(false);

					}
				});
			}
		});

		Button btnCreativeUI = new Button("Add question/answer of your own");

		btnCreativeUI.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent action) {
				for (ExamUIEventsListener listen : allListeners) {
					listen.closeWindow(0);
					listen.showWindow(2);
				}
			}

		});

		vbRoot.getChildren().addAll(rdbAutoOption, rdbManualOption, hbRoot, btnCreativeUI, btnExecutive);
		theStage.setScene(new Scene(vbRoot, 450, 350));
		theStage.show();
	}

	public int getNumberOfQuestionsToUI() {
		int num = 0;
		for (ExamUIEventsListener l : allListeners) {
			num = l.getNumOfQuestions();
		}
		return num;
	}

	@Override
	public void autoExamFailedMessage(String message) {
		JOptionPane.showMessageDialog(null, message);

	}

	@Override
	public void registerListener(ExamUIEventsListener listener) {
		allListeners.add(listener);

	}

	public int getNumberOfAQuestionsToUI() {
		int num = 0;
		for (ExamUIEventsListener l : allListeners) {
			num = l.getNumOfAmericanQuestions();
		}
		return num;
	}

	@Override
	public void manualExamFailedMessage(String message) {
		JOptionPane.showMessageDialog(null, message);

	}

	@Override
	public void removeAnswerFromUI(String value, int index, int indexRow) {
		System.out.println("succeded");

	}

	@Override
	public void addAnswerToUI(String value, int index, int indexRow, ComboBox<String> all) {
		System.out.println("succeded");

	}

	@Override
	public void addOpenQuestionToUI(String string, int i) {
		System.out.println("succeded");

	}

	@Override
	public void addAmericanQuestionToUI(int id, String theQuestion, ArrayList<String> allAnswers) {
		if (allAnswers.size() >= 4) {
			int num = getNumberOfQuestionsToUI();
			lbNumQuestion.setText("Number of questions (1-" + num + ") :");
		}

	}

	@Override
	public void showWindow(int i) {
		if (i == 0) {
			theStage.show();
		}
	}

	@Override
	public void closeWindow(int i) {
		if (i == 0) {
			theStage.hide();
		}

	}

	@Override
	public void addSAnswerToUI(int numOfQuestion, String theOption, int idAns, int numOfAnswersBefor) {
		if (numOfAnswersBefor < 4 && idAns >= 4) {
			int num = getNumberOfAQuestionsToUI();
			lbNumQuestion.setText("Number of questions (1-" + num + ") :");
		}

	}

	@Override
	public void removeSpecificAnswerFromUI(int numOfAns, int numOfQ, String theOption, String theQues) {
		lbNumQuestion.setText("Number of questions (1-" + getNumberOfAQuestionsToUI() + ") :");
	}

	@Override
	public void removeQuestionFromUI(int numOfQ, String theQuestion) {
		lbNumQuestion.setText("Number of questions (1-" + getNumberOfAQuestionsToUI() + ") :");

	}

}
