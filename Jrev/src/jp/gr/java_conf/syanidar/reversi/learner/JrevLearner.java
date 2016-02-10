package jp.gr.java_conf.syanidar.reversi.learner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class JrevLearner extends Application{
	private JrevLearnerController controller;
	public static final void main(String[] args){
		launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Jrev Learner");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("JrevLearner.fxml"));
		stage.setScene(new Scene(loader.load()));
    	controller = (JrevLearnerController)loader.getController();
    	controller.setStage(stage);
		stage.sizeToScene();
		stage.setResizable(false);
		stage.show();
	}
	@Override
	public void stop(){
    	controller.shutdown();
	}
	
}
