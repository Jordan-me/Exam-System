//ID : 208730523 
//NAME: Yarden Dahan

package Main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import controller.ExamController;
import exam_system.ExamGenerator;
import javafx.application.Application;
import javafx.stage.Stage;
import view.AbstractExamView;
import view.ExamSystemFX;
import view.ExamSystemFX2;
import view.ExamSystemFX3;

public class ExamSystemMain extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage proStage) throws Exception {
		System.out.println("Here we go...");
		ExamGenerator theModel = new ExamGenerator();
		AbstractExamView theMainView = new ExamSystemFX(new Stage());
		ExamController controller1 = new ExamController(theModel, theMainView);

		Stage thirdStage = new Stage();
		AbstractExamView theMainView3;
		theMainView3 = new ExamSystemFX3(thirdStage);
		ExamController controller3 = new ExamController(theModel, theMainView3);

		Map<String, ArrayList<String>> allQuestionToUI = new LinkedHashMap<String, ArrayList<String>>();
		allQuestionToUI.putAll(controller1.getAllQuestionToUi());
		AbstractExamView theMainView2;

		Stage secondStage = new Stage();
		theMainView2 = new ExamSystemFX2(secondStage, allQuestionToUI);
		ExamController controller2 = new ExamController(theModel, theMainView2);
	}

}
