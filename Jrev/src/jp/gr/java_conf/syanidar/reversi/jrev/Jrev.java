package jp.gr.java_conf.syanidar.reversi.jrev;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Jrev extends Application{
	
	private JrevController controller;
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Jrev.fxml"));
		Scene scene = new Scene(loader.load());
		controller = (JrevController)loader.getController();
		controller.setStage(stage);
		stage.setTitle("Jrev");
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setResizable(false);
		stage.show();
	}
	
	@Override
	public void stop() throws Exception {
		controller.shutdown();
	}

	public static final void main(String args[]){
		launch(args);
	}
}
