package jp.gr.java_conf.syanidar.reversi.ffotester;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;


public class FFOTester extends Application{
	FFOTesterController controller;
	public static final void main(String args[]){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FFOTester.fxml"));
		primaryStage.setScene(new Scene(loader.load()));
		controller = (FFOTesterController)loader.getController();
		controller.setStage(primaryStage);
		primaryStage.sizeToScene();
		primaryStage.show();
	}
	@Override
	public void stop(){
		controller.shutdown();
	}
}
