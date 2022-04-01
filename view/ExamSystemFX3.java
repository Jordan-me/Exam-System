package view;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import listeners.ExamUIEventsListener;

public class ExamSystemFX3 implements AbstractExamView {
	private Vector<ExamUIEventsListener> allListeners = new Vector<ExamUIEventsListener>();
	private StackPane spRoot;
	private VBox vbMainSession;
	private VBox vbSecondSession;
	private VBox vbThirdSession;
	private VBox vbFourSession;
	private HBox firstRow;
	private HBox secondRow;
	private HBox thirdRow;
	private Stage thirStage;

	public ExamSystemFX3(Stage thirdStage) {
		this.thirStage = thirdStage;
		thirdStage.setTitle("Creat repository of your own");
		spRoot = new StackPane();
		vbMainSession = new VBox();
		spRoot.setPadding(new Insets(10));
		spRoot.setAlignment(Pos.CENTER);
		vbMainSession.setPadding(new Insets(10));
		vbMainSession.setAlignment(Pos.CENTER);
		vbMainSession.setSpacing(20);
		Button btaddOp = new Button("Add Open question");
		Button btaddAQbtn = new Button("Add american question");
		Button bteditQbtn = new Button("Edit question");
		Button btReturnMain = new Button("Return to main session");
		vbMainSession.getChildren().addAll(btaddOp, btaddAQbtn, bteditQbtn, btReturnMain);
		spRoot.getChildren().add(vbMainSession);
		btaddOp.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				vbSecondSession = new VBox();
				vbSecondSession.setPadding(new Insets(20));
				vbSecondSession.setAlignment(Pos.CENTER);
				vbSecondSession.setSpacing(10);
				CheckBox showTxt = new CheckBox("Start");
				Label lblQ = new Label("Enter your question: ");
				TextField txtQues = new TextField();
				txtQues.setDisable(true);
				Label lblS = new Label("The solution: ");
				TextField txtSol = new TextField();
				txtSol.setDisable(true);
				CheckBox blockTxt = new CheckBox("Finish");
				blockTxt.setVisible(false);
				Button btAdd = new Button("Done");
				btAdd.setVisible(false);
				Button btRturn = new Button("return");

				showTxt.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						lblQ.setTextFill(Color.BLACK);
						lblS.setTextFill(Color.BLACK);
						txtQues.setDisable(!showTxt.isSelected());
						txtSol.setDisable(!showTxt.isSelected());
						blockTxt.setVisible(showTxt.isSelected());
						blockTxt.setSelected(!showTxt.isSelected());
						btAdd.setVisible(false);
					}

				});
				blockTxt.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						txtQues.setDisable(blockTxt.isSelected());
						txtSol.setDisable(blockTxt.isSelected());
						showTxt.setSelected(!blockTxt.isSelected());
						if (!(txtQues.getText().isEmpty() || txtSol.getText().isEmpty())) {
							if (blockTxt.isSelected()) {
								btAdd.setVisible(true);
							}
						} else {
							if (txtQues.getText().isEmpty()) {
								lblQ.setTextFill(Color.RED);
							} else if (txtSol.getText().isEmpty()) {
								lblS.setTextFill(Color.RED);
							}
						}

					}

				});
				btAdd.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						String tQues = txtQues.getText();
						String sQues = txtSol.getText();
						for (ExamUIEventsListener l : allListeners) {
							int id = l.getNumOfQuestions();
							l.addOpenQuestionToModelExam(tQues, sQues, id);
							blockTxt.setSelected(false);
							txtQues.setText("");
							txtSol.setText("");
							txtQues.setDisable(true);
							txtSol.setDisable(true);
						}
					}

				});
				btRturn.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						vbSecondSession.setVisible(false);
						vbMainSession.setVisible(true);

					}

				});

				vbSecondSession.getChildren().addAll(showTxt, lblQ, txtQues, lblS, txtSol, blockTxt, btAdd, btRturn);
				vbMainSession.setVisible(false);
				spRoot.getChildren().add(vbSecondSession);

			}

		});
		btaddAQbtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				vbThirdSession = new VBox();
				TextField txtQues = new TextField();
				ArrayList<String> allOptionsAQ = new ArrayList<String>();
				ArrayList<Integer> solution = new ArrayList<Integer>();
				vbThirdSession.setPadding(new Insets(20));
				vbThirdSession.setAlignment(Pos.CENTER);
				vbThirdSession.setSpacing(10);

				Label lb1 = new Label("Collect options:");
				Label lb2 = new Label("Check the correct option:");
				HBox hbOptions = new HBox(lb1);
				HBox hbSolution = new HBox(lb2);
				hbOptions.setSpacing(10);
				ArrayList<Label> allLBLop = new ArrayList<Label>();
				ArrayList<CheckBox> allCKBsolution = new ArrayList<CheckBox>();
				CheckBox showTxt = new CheckBox("Start");
				txtQues.setText("Enter your question:");
				txtQues.setDisable(true);
				TextField txtOption = new TextField();
				txtOption.setText("Enter option:");
				txtOption.setDisable(true);
				txtOption.setMaxWidth(2000);
				Button btAddOption = new Button("Add");
				HBox hbAdd = new HBox(txtOption, btAddOption);
				hbAdd.setSpacing(20);
				CheckBox blockTxt = new CheckBox("Finish");
				blockTxt.setVisible(false);
				Button btExcutive = new Button("Done");
				btExcutive.setVisible(false);
				Button btRturn = new Button("return");
				btRturn.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						vbThirdSession.setVisible(false);
						vbMainSession.setVisible(true);
					}

				});
				showTxt.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						txtQues.setText("");
						txtQues.setDisable(!showTxt.isSelected());
						txtOption.setDisable(!showTxt.isSelected());
						txtOption.setText("");
						blockTxt.setVisible(showTxt.isSelected());
						blockTxt.setSelected(!showTxt.isSelected());
						txtQues.setBorder(new Border(new BorderStroke(Color.AQUAMARINE, BorderStrokeStyle.SOLID, null,
								new BorderWidths(1))));
						txtOption.setBorder(new Border(new BorderStroke(Color.AQUAMARINE, BorderStrokeStyle.SOLID, null,
								new BorderWidths(1))));
						if (showTxt.isSelected()) {
							txtQues.setText("");
							txtOption.setText("");
							btExcutive.setVisible(false);
							btAddOption.setVisible(true);
						}

					}

				});
				blockTxt.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						txtQues.setDisable(blockTxt.isSelected());
						txtOption.setDisable(blockTxt.isSelected());
						showTxt.setSelected(!blockTxt.isSelected());
						btAddOption.setVisible(!blockTxt.isSelected());
						if (blockTxt.isSelected()) {
							if (!(txtQues.getText().isEmpty() || txtQues.getText().equals("Enter your question:")
									|| allLBLop.isEmpty() || allCKBsolution.isEmpty())) {
								if (blockTxt.isSelected()) {
									btExcutive.setVisible(true);

								}
							} else {
								if (txtQues.getText().isEmpty() || (txtQues.getText().equals("Enter your question:"))) {
									txtQues.setText("Enter your question:");
									txtQues.setBorder(new Border(new BorderStroke(Color.INDIANRED,
											BorderStrokeStyle.SOLID, null, new BorderWidths(1))));

								}
								if (txtOption.getText().isEmpty()) {
									txtOption.setText("Enter your option:");
									txtOption.setBorder(new Border(new BorderStroke(Color.INDIANRED,
											BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
								}
							}
						}

					}

				});
				btAddOption.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						if (txtOption.getText().isEmpty()
								|| txtOption.getText().equalsIgnoreCase("Enter your option:")) {
							txtOption.setBorder(new Border(new BorderStroke(Color.INDIANRED, BorderStrokeStyle.SOLID,
									null, new BorderWidths(1))));
						} else {
							String option = txtOption.getText();
							txtOption.setText("");
							Label theOp = new Label(option);

							theOp.setBorder(new Border(
									new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
							theOp.setPadding(new Insets(5));
							if (allLBLop.size() < 10) {
								CheckBox ckSol = new CheckBox(option);
								allLBLop.add(theOp);
								hbOptions.getChildren().add(theOp);
								allCKBsolution.add(ckSol);
								hbSolution.getChildren().add(ckSol);
								allOptionsAQ.add(option);
								ckSol.setOnMouseClicked(new EventHandler<MouseEvent>() {

									@Override
									public void handle(MouseEvent arg0) {
										Integer sol = allCKBsolution.indexOf(ckSol);
										if (ckSol.isSelected()) {
											solution.add(sol);

										} else if (!ckSol.isSelected()) {
											solution.remove(sol);
										}
						
									}

								});
							}
							if (allLBLop.size() == 10) {
								JOptionPane.showMessageDialog(null, "NO MORE ANSWERS! ");
							}
							theOp.setOnMouseClicked(new EventHandler<MouseEvent>() {

								@Override
								public void handle(MouseEvent arg0) {

									hbOptions.getChildren().remove(theOp);
									int id = allLBLop.indexOf(theOp);
									allLBLop.remove(theOp);
									hbSolution.getChildren().remove(allCKBsolution.get(id));
									if (allCKBsolution.get(id).isSelected()) {
										for (int i = 0; i < solution.size(); i++) {
											if (id == solution.get(i)) {
												solution.remove(i);
											}
										}

									}
									allCKBsolution.remove(id);
									allOptionsAQ.remove(theOp.getText());
								}
							});
						}

					}

				});
				btExcutive.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						if (allLBLop.size() < 4) {
							JOptionPane.showMessageDialog(null,
									"NOTE that for the question to be in a random lottery it must contain at least 4 answers");
						}
						for (ExamUIEventsListener l : allListeners) {
							int id = l.getNumOfQuestions();
							String theQ = txtQues.getText();
							l.addAmericanQuestionToUI(id, theQ, allOptionsAQ, solution);

						}

					}

				});

				vbMainSession.setVisible(false);
				vbThirdSession.getChildren().addAll(showTxt, txtQues, hbAdd, hbOptions, hbSolution, blockTxt,
						btExcutive, btRturn);
				spRoot.getChildren().add(vbThirdSession);
			}

		});
		bteditQbtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				vbFourSession = new VBox();
				vbMainSession.setVisible(false);
				firstRow = new HBox();
				secondRow = new HBox();
				thirdRow = new HBox();
				firstRow.setSpacing(20);
				secondRow.setSpacing(20);
				thirdRow.setSpacing(20);
				GridPane gpRoot = new GridPane();
				gpRoot.setPadding(new Insets(8));
				gpRoot.setHgap(10);
				gpRoot.setVgap(10);
				Label chQ = new Label("choose:");
				ComboBox<String> allQ = new ComboBox<String>();
				allQ.setPrefWidth(300);
				ComboBox<String> allOp = new ComboBox<String>();
				allOp.setPrefWidth(300);
				allOp.setVisible(false);
				TextField txtOption = new TextField();
				Button btnAddSop = new Button("Add option:");
				Button btnRemoveSOp = new Button("Remove option:");
				Button btnRemoveQ = new Button("Remove question");
				Button btnReturn = new Button("Return To Main");
				CheckBox ckTrue = new CheckBox("Correct");
				CheckBox ckFalse = new CheckBox("Uncorrect");
				txtOption.setPrefWidth(300);
				firstRow.getChildren().addAll(chQ, allQ, allOp);
				secondRow.getChildren().addAll(btnRemoveSOp, btnRemoveQ);
				secondRow.setAlignment(Pos.CENTER);
				thirdRow.getChildren().addAll(txtOption, ckTrue, ckFalse, btnAddSop);
				thirdRow.setAlignment(Pos.CENTER);
				for (ExamUIEventsListener l : allListeners) {
					int num = l.getNumOfQuestions();
					for (int i = 0; i < num; i++) {
						String theQues = l.getQuestionToUIByIndex(i);
						allQ.getItems().add(theQues);
					}
				}
				ArrayList<String> allTheQues = new ArrayList<String>();
				ArrayList<String> allOfOp = new ArrayList<String>();
				allTheQues.addAll(allQ.getItems());
				allQ.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						if (allQ.getSelectionModel().isEmpty()) {
							allOp.setVisible(false);
							btnRemoveSOp.setVisible(false);
							btnAddSop.setVisible(false);
						} else {
							for (ExamUIEventsListener l : allListeners) {
								int numOfQues = l.getIndexOfQuestionToUI(allQ.getSelectionModel().getSelectedItem());
								allOp.getItems().removeAll(allOfOp);
								allOfOp.removeAll(allOfOp);
								allOfOp.addAll(l.getAnswersToQuestion(numOfQues));
								if (!allOfOp.isEmpty()) {
									allOp.getItems().addAll(allOfOp);
									allOp.setVisible(true);
									btnRemoveSOp.setVisible(true);
									btnAddSop.setVisible(true);

								} else {
									allOp.setVisible(false);
									btnRemoveSOp.setVisible(false);
									btnAddSop.setVisible(false);
								}

							}
						}

					}

				});
				btnAddSop.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						if (txtOption.getText().isEmpty()) {
							txtOption.setBorder(new Border(
									new BorderStroke(Color.CORAL, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
						} else if (allQ.getSelectionModel().isEmpty()) {
							allQ.setBorder(new Border(new BorderStroke(Color.CORAL, BorderStrokeStyle.DASHED, null,
									new BorderWidths(1))));
						} else if ((!(ckFalse.isSelected() || ckTrue.isSelected()))
								|| (ckFalse.isSelected() && ckTrue.isSelected())) {
							ckFalse.setTextFill(Color.RED);
							ckTrue.setTextFill(Color.RED);

						} else {
							ckFalse.setTextFill(Color.BLACK);
							ckTrue.setTextFill(Color.BLACK);
							txtOption.setBorder(new Border(new BorderStroke(Color.AQUAMARINE, BorderStrokeStyle.SOLID,
									null, new BorderWidths(1))));
							allQ.setBorder(new Border(new BorderStroke(Color.AQUAMARINE, BorderStrokeStyle.DASHED, null,
									new BorderWidths(1))));
							String theOption = txtOption.getText();
							Boolean isCorrect;
							for (ExamUIEventsListener l : allListeners) {
								int numOfQuestion = l
										.getIndexOfQuestionToUI(allQ.getSelectionModel().getSelectedItem());
								if (ckFalse.isSelected()) {
									isCorrect = false;
								} else {
									isCorrect = true;
								}
								// l.showWindow(1);
								l.addSpecificAnswerToUI(numOfQuestion, theOption, isCorrect);
								allOfOp.add(theOption);
								allOp.getItems().add(theOption);
							}
						}

					}

				});
				btnRemoveSOp.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						if (!allOfOp.isEmpty()&& !allOp.getSelectionModel().isEmpty()) {
							allOp.setBorder(new Border(new BorderStroke(Color.AQUAMARINE, BorderStrokeStyle.SOLID,
									null, new BorderWidths(1))));
							String theOption = allOp.getSelectionModel().getSelectedItem();
							String theQuestion = allQ.getSelectionModel().getSelectedItem();
							for (ExamUIEventsListener l :allListeners) {
							int numOfQ = l.getIndexOfQuestionToUI(theQuestion);
							l.removeAnswerFromUI(theOption, numOfQ);
							}
							allOp.getItems().remove(theOption);
						}else {
							JOptionPane.showMessageDialog(null, "Select an option first");
						}
					}

				});
				btnRemoveQ.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						String theQuestion = allQ.getSelectionModel().getSelectedItem();
						for (ExamUIEventsListener l :allListeners) {
							int numOfQ = l.getIndexOfQuestionToUI(theQuestion);
							l.removeQuestionFromUI(theQuestion, numOfQ);
						}
						allQ.getItems().remove(theQuestion);
					}
					
				});

				btnReturn.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						vbFourSession.setVisible(false);
						vbMainSession.setVisible(true);
					}

				});

				vbFourSession.setSpacing(30);
				vbFourSession.setAlignment(Pos.CENTER);
				vbFourSession.getChildren().addAll(firstRow, secondRow, thirdRow, btnReturn);
				spRoot.getChildren().add(vbFourSession);
			}

		});
		btReturnMain.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				for (ExamUIEventsListener listen : allListeners) {
					listen.showWindow(0);
					listen.closeWindow(2);
				}

			}

		});
		thirdStage.setScene(new Scene(spRoot, 800, 500));
		thirdStage.hide();

	}

	@Override
	public void registerListener(ExamUIEventsListener listener) {
		allListeners.add(listener);

	}

	@Override
	public void addOpenQuestionToUI(String string, int i) {
		JOptionPane.showMessageDialog(null, "Your question has been successfully added");

	}

	@Override
	public void addAmericanQuestionToUI(int id, String theQuestion, ArrayList<String> allAnswers) {
		JOptionPane.showMessageDialog(null, "Your question has been successfully added");

	}

	@Override
	public void showWindow(int i) {
		if (i == 2) {
			thirStage.show();
		}

	}

	@Override
	public void addAnswerToUI(String value, int index, int indexRow, ComboBox<String> all) {
		System.out.println("added");
	}

	@Override
	public void removeAnswerFromUI(String value, int index, int indexRow) {
		System.out.println("removed");
	}

	@Override
	public void autoExamFailedMessage(String message) {
		System.out.println("failed");
	}

	@Override
	public void closeWindow(int i) {
		if (i == 2) {
			thirStage.hide();
		}

	}

	@Override
	public void addSAnswerToUI(int numOfQuestion, String theOption, int idAns, int numOfAnswersBefor) {
		JOptionPane.showMessageDialog(null, "The answer " + theOption + " added succesfully");

	}

	@Override
	public void manualExamFailedMessage(String message) {
		System.out.println("failed");

	}

	@Override
	public void removeSpecificAnswerFromUI(int numOfAns, int numOfQ, String theOption, String theQues) {
		JOptionPane.showMessageDialog(null, "Your selection has been successfully removed");
		
	}

	@Override
	public void removeQuestionFromUI(int numOfQ, String theQuestion) {
		JOptionPane.showMessageDialog(null, "Your selection has been successfully removed");
		
	}

}
