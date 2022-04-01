package view;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JOptionPane;

import exceptions.IllegalInputException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import listeners.ExamUIEventsListener;

public class ExamSystemFX2 implements AbstractExamView {
	private Vector<ExamUIEventsListener> allListeners = new Vector<ExamUIEventsListener>();
	private Map<String, List<String>> allTextData = new LinkedHashMap<String, List<String>>();
	private Vector<HBox> allDBWindow = new Vector<HBox>();
	private Map<Integer, CheckBox> mappingQues = new LinkedHashMap<Integer, CheckBox>();
	private Map<Integer, Vector<Label>> mappingAnswers = new LinkedHashMap<Integer, Vector<Label>>();
	private FlowPane fAllWindow = new FlowPane();
	private VBox vbRoot;
	private GridPane gpRoot;
	private Stage secondStage;

	public ExamSystemFX2(Stage secondStage, Map<String, ArrayList<String>> allQuestionToUI)
			throws FileNotFoundException, IllegalInputException {
		this.secondStage = secondStage;
		for (Entry<String, ArrayList<String>> entry : allQuestionToUI.entrySet()) {
			allTextData.put(entry.getKey(), entry.getValue());
		}
		gpRoot = new GridPane();
		secondStage.setTitle("Questions repository");
		fAllWindow.setVgap(10);
		fAllWindow.setHgap(10);
		gpRoot.setPadding(new Insets(10));
		gpRoot.setHgap(10);
		gpRoot.setVgap(10);
		vbRoot = new VBox();
		vbRoot.setAlignment(Pos.CENTER_LEFT);
		vbRoot.setSpacing(40);
		vbRoot.setPadding(new Insets(10));
		setAllQuestionsHorizonly();
		Button btnDone = new Button("Done");
		Button btnReturnMain = new Button("Return to main Session");
		btnDone.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ArrayList<Integer> allQuestionChosen = new ArrayList<Integer>();
				allQuestionChosen.addAll(getChosenMappingQues());
				ArrayList<ArrayList<Integer>> syncAnsr = new ArrayList<ArrayList<Integer>>();
				for (ExamUIEventsListener l : allListeners) {
					if (allQuestionChosen.isEmpty()) {
						l.faildCreatingExam();
					} else if (!checkerIfAllsetUP(allQuestionChosen)) {
						l.faildCreatingExam();
					} else {
						syncAnsr.addAll(getSyncAnsr(allQuestionChosen));
						l.buildExamManually(allQuestionChosen, syncAnsr);
						for (Entry<Integer, Vector<Label>> entry : mappingAnswers.entrySet()) {
							int indexRow = entry.getKey();
							Vector<Label> allAnselected = new Vector<Label>();
							allAnselected.addAll(entry.getValue());
							for (int i = 0; i < allAnselected.size(); i++) {
								String value = allAnselected.get(i).getText();
								int idLbl = getlblID(value);
								removeAnswerFromUI(value, idLbl, indexRow);

							}

						}
						mappingAnswers.clear();
					}
				}
			}

		});
		btnReturnMain.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				for (ExamUIEventsListener listen : allListeners) {
					listen.showWindow(0);
					listen.closeWindow(1);
				}

			}

		});
		btnDone.setBorder(Border.EMPTY);
		btnDone.setMaxWidth(100);
		ScrollPane scroll = new ScrollPane();
		scroll.fitToHeightProperty().set(true);
		scroll.fitToWidthProperty().set(true);
		vbRoot.getChildren().addAll(allDBWindow);
		gpRoot.add(vbRoot, 0, 1);
		gpRoot.add(btnDone, 0, 2);
		gpRoot.add(btnReturnMain, 0, 3);
		scroll.setContent(gpRoot);
		secondStage.setScene(new Scene(scroll, 800, 600));
		secondStage.hide();

	}

	public ArrayList<ArrayList<Integer>> getSyncAnsr(ArrayList<Integer> allQuestionChosen) {
		ArrayList<ArrayList<Integer>> getSyncAnsr = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < allQuestionChosen.size(); i++) {
			Vector<Label> allChosenAns = new Vector<Label>();
			if (mappingAnswers.get(allQuestionChosen.get(i)) == null) {
				if (allDBWindow.get(i).getChildren().size() == 1) {
					continue;

				}

			} else {

				allChosenAns.addAll(mappingAnswers.get(allQuestionChosen.get(i)));
				ArrayList<Integer> idAns = new ArrayList<Integer>();
				for (int j = 0; j < allChosenAns.size(); j++) {
					Label lbl = allChosenAns.get(j);
					int idLbl = getlblID(lbl.getText());
					idAns.add(idLbl);
				}
				getSyncAnsr.add(idAns);
			}
		}
		return getSyncAnsr;
	}

	public boolean checkerIfAllsetUP(ArrayList<Integer> intOfQues) {
		for (int i = 0; i < intOfQues.size(); i++) {
			int numOQ = intOfQues.get(i);
			if (mappingAnswers.containsKey(numOQ)) {
				String ques = mappingQues.get(numOQ).getText();
				if (!(allTextData.get(ques).isEmpty())) {
					if (mappingAnswers.get(numOQ) == null) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private ArrayList<Integer> getChosenMappingQues() {
		ArrayList<Integer> chosenQues = new ArrayList<Integer>();
		ArrayList<Integer> leftOvers = new ArrayList<Integer>();
		for (Entry<Integer, CheckBox> entry : mappingQues.entrySet()) {
			int key = entry.getKey();
			CheckBox ck = entry.getValue();
			if (ck.isSelected()) {
				for (int i = 0; i < allDBWindow.size(); i++) {
					HBox hbCurrent = allDBWindow.get(i);
					if (hbCurrent.getChildren().contains(ck)) {
						if (hbCurrent.getChildren().size() > 1) {
							chosenQues.add(key);
						} else {
							leftOvers.add(key);
						}
					}
				}

			}
		}
		chosenQues.addAll(leftOvers);
		return chosenQues;
	}

	@Override
	public void autoExamFailedMessage(String message) {
		System.out.println("someTing is not allright");

	}

	@Override
	public void registerListener(ExamUIEventsListener listener) {
		allListeners.add(listener);

	}

	@Override
	public void manualExamFailedMessage(String message) {
		JOptionPane.showMessageDialog(null, message);

	}

	public void setAllQuestionsHorizonly() {
		for (Entry<String, List<String>> entry : allTextData.entrySet()) {
			String key = entry.getKey();
			ComboBox<String> allAnswers = new ComboBox<String>();
			allAnswers.setDisable(true);
			CheckBox cb = new CheckBox(key);
			Button btnAddOption = new Button("Add");
			btnAddOption.setDisable(true);
			Label text = new Label("collected options:");
			text.setVisible(false);
			List<String> value = entry.getValue();
			for (int i = 0; i < value.size(); i++) {
				allAnswers.getItems().add((i + 1) + ")" + value.get(i));
			}

			cb.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					for (ExamUIEventsListener l : allListeners) {
						int indla = l.getIndexOfQuestionToUI(cb.getText());
						if (cb.isSelected()) {
							mappingQues.put(indla, cb);
							allAnswers.setDisable(false);
							btnAddOption.setDisable(false);
							text.setVisible(true);
						}
						if (!cb.isSelected()) {
							mappingQues.remove(indla);
							allAnswers.setDisable(true);
							btnAddOption.setDisable(false);
							text.setVisible(false);
						}

					}

				}
			});
			btnAddOption.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					for (ExamUIEventsListener l : allListeners) {
						if (allAnswers.getSelectionModel().isEmpty()) {
							l.faildMessageAddingAnswer();
						} else {
							l.addAnswerToToModelEvent(allAnswers.getValue(), l.getIndexOfQuestionToUI(cb.getText()),
									allAnswers);

						}

					}

				}

			});
			HBox hbRoot = new HBox();
			hbRoot.setSpacing(10);

			if (!value.isEmpty()) {
				hbRoot.getChildren().addAll(cb, allAnswers, btnAddOption, text);
			} else {
				hbRoot.getChildren().add(cb);
			}
			allDBWindow.add(hbRoot);
		}
	}

	public int getlblID(String value) {
		String[] lbltxt = value.split("\\)");
		if (lbltxt[0] == null) {
			return -1;
		}
		int lblId = Integer.parseInt(lbltxt[0]);
		return lblId;
	}

	@Override
	public void addAnswerToUI(String value, int idLbl, int indexRow, ComboBox<String> all) {
		String lbl = value;
		Label lblNewAnswer = new Label(lbl);
		replaceValueInMap(indexRow, lblNewAnswer, 1);
		lblNewAnswer.setBorder(
				new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
		lblNewAnswer.setPadding(new Insets(5));
		allDBWindow.get(indexRow).getChildren().add(lblNewAnswer);
		all.getItems().remove(all.getValue());
		lblNewAnswer.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				for (ExamUIEventsListener l : allListeners) {
					l.removeAnswerFromModelEvent(lblNewAnswer.getText(), idLbl, indexRow);
					replaceValueInMap(indexRow, lblNewAnswer, 0);
					all.getItems().add((lblNewAnswer.getText()));
					lblNewAnswer.setVisible(true);
				}
			}
		});

	}

	public void replaceValueInMap(int indexRow, Label lbl, int num) {
		Vector<Label> replacement = new Vector<Label>();
		if (num == 1) {
			if (mappingAnswers.get(indexRow) == null) {
				replacement.add(lbl);
				mappingAnswers.put(indexRow, replacement);
			} else {
				replacement.addAll(mappingAnswers.get(indexRow));
				replacement.add(lbl);
				mappingAnswers.put(indexRow, replacement);

			}
		} else if (num == 0) {
			replacement.addAll(mappingAnswers.get(indexRow));
			replacement.remove(lbl);
			mappingAnswers.put(indexRow, replacement);
		}

	}

	public String parseTocorrect(int idLbl, String value) {
		int lblCorrect = idLbl;
		StringBuilder sb = new StringBuilder();
		String[] getTowork = value.split("[)]");
		sb.append(lblCorrect + ")" + getTowork[1]);
		return sb.toString();
	}

	public String getTextLbl(String value) {
		String[] sblbl = value.split("[)]");
		return sblbl[1];
	}

	@Override
	public void removeAnswerFromUI(String value, int idLbl, int indexRow) {
		Label lbl = getLabelByIndex(idLbl, indexRow, value);
		if (allDBWindow.get(indexRow).getChildren().remove(lbl)) {
			System.out.println("removed lbl from window");
		}

	}

	public int getPlaceOflbl(Vector<Label> values, int idLbl, int indexRow, String value) {
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i).equals(getLabelByIndex(idLbl, indexRow, value))) {
				return i;
			}
		}
		return -1;
	}

	public Label getLabelByIndex(int idLbl, int indexRow, String value) {
		Label lbEl = null;
		Vector<Label> lbElement = mappingAnswers.get(indexRow);
		for (int i = 0; i < lbElement.size(); i++) {
			int idForCheck = getlblID(lbElement.get(i).getText());
			if (idForCheck == idLbl) {
				lbEl = lbElement.remove(i);
				return lbEl;
			}
		}
		return lbEl;
	}

	public boolean isallReadyExistAnswer(String value, int idLbl, int indexRow) {
		HBox hb = new HBox();
		hb = allDBWindow.get(indexRow);
		if (!mappingAnswers.containsKey(indexRow)) {
			return false;
		}
		if (!hb.getChildren().contains(getLabelByIndex(idLbl, indexRow, value))) {
			return false;
		}
		if (getLabelByIndex(idLbl, indexRow, value) != null) {
			return true;
		}
		System.out.println("false");
		return false;
	}

	@Override
	public void addOpenQuestionToUI(String string, int i) {
		allTextData.put(string, null);
		CheckBox newOne = new CheckBox(string);
		mappingQues.put(i, newOne);
		HBox newOne1 = new HBox(newOne);
		allDBWindow.add(newOne1);
		vbRoot.getChildren().add(newOne1);

	}

	@Override
	public void addAmericanQuestionToUI(int id, String theQuestion, ArrayList<String> allAnswers) {
		allTextData.put(theQuestion, allAnswers);
		CheckBox newOne = new CheckBox(theQuestion);
		mappingQues.put(id, newOne);
		ComboBox<String> addition = new ComboBox<String>();
		Button add = new Button("Add");
		for (int i = 0; i < allAnswers.size(); i++) {
			String str = "" + (i + 1) + ")" + allAnswers.get(i);
			addition.getItems().add(str);
		}
		HBox newOne1 = new HBox(newOne, addition, add);
		newOne1.setSpacing(10);

		add.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				buttoAddHandle(addition, newOne);
			}
		});
		allDBWindow.add(newOne1);
		vbRoot.getChildren().add(newOne1);

	}

	public void buttoAddHandle(ComboBox<String> addition, CheckBox newOne) {
		for (ExamUIEventsListener l : allListeners) {
			if (addition.getSelectionModel().isEmpty()) {
				l.faildMessageAddingAnswer();
			} else {

				l.addAnswerToToModelEvent(addition.getValue(), l.getIndexOfQuestionToUI(newOne.getText()), addition);

			}

		}

	}

	@Override
	public void showWindow(int i) {
		if (i == 1) {
			secondStage.show();
		}

	}

	@Override
	public void closeWindow(int i) {
		if (i == 1) {
			secondStage.hide();
		}

	}

	@Override
	public void addSAnswerToUI(int numOfQuestion, String theOption, int idAns, int numOfAnswersBefor) {

		ArrayList<String> allNewOptions = new ArrayList<String>();
		ComboBox<String> newOne = new ComboBox<String>();
		String theQues = null;
		Button btnAdd = new Button("Add");
		for (ExamUIEventsListener l : allListeners) {
			allNewOptions.addAll(l.getAnswersToQuestion(numOfQuestion));
			theQues = l.getQuestionToUIByIndex(numOfQuestion);

		}
		for (int i = 0; i < allNewOptions.size(); i++) {
			String str = "" + (i + 1) + ")" + allNewOptions.get(i);
			newOne.getItems().add(str);
		}
		CheckBox oldOne = new CheckBox(theQues);
		allTextData.get(theQues).add(theOption);
		HBox newOne1 = new HBox(oldOne, newOne, btnAdd);
		newOne1.setSpacing(10);
		changeWindow(numOfQuestion, newOne1, oldOne);

		btnAdd.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				buttoAddHandle(newOne, oldOne);
			}
		});

	}

	public void changeWindow(int numOfQuestion, HBox newOne1, CheckBox oldOne) {
		allDBWindow.remove(numOfQuestion);
		allDBWindow.add(numOfQuestion, newOne1);
		vbRoot.getChildren().remove(numOfQuestion);
		vbRoot.getChildren().add(numOfQuestion, newOne1);
		mappingQues.remove(numOfQuestion);
		mappingQues.put(numOfQuestion, oldOne);
		newOne1.setSpacing(10);

	}

	@Override
	public void removeSpecificAnswerFromUI(int numOfAns, int numOfQ, String theOption, String theQues) {
		allTextData.get(theQues).remove(numOfAns);
		ComboBox<String> newOne = new ComboBox<String>();
		Button btnAdd = new Button("Add");
		for (int i = 0; i < allTextData.get(theQues).size(); i++) {
			String str = "" + (i + 1) + ")" + allTextData.get(theQues).get(i);
			newOne.getItems().add(str);
		}
		CheckBox oldOne = new CheckBox(theQues);
		HBox newOne1 = new HBox(oldOne, newOne, btnAdd);
		changeWindow(numOfQ, newOne1, oldOne);

		btnAdd.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				buttoAddHandle(newOne, oldOne);
			}
		});

	}

	@Override
	public void removeQuestionFromUI(int numOfQ, String theQuestion) {
		allTextData.remove(theQuestion);
		mappingQues.remove(numOfQ);
		allDBWindow.remove(numOfQ);
		vbRoot.getChildren().remove(numOfQ);
	}

}
