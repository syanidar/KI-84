package jp.gr.java_conf.syanidar.reversi.learner;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JrevLearnerController {
	
	@FXML Label message;
	@FXML TextField textField;
    @FXML Label progressIndicator;
    @FXML ProgressBar progressBar;
    @FXML Button startButton;
    
    private File file;
    private Stage stage;
    private ExecutorService service;
    private boolean started;

    @FXML
    private void onStartButtonClicked(ActionEvent event){

    	
    	if (!started) {
			String input = textField.getText();
			if (isDecimal(input) && file != null) {
				started = true;
				startButton.setText("Stop");
				JrevLearnerViewer task = new JrevLearnerViewer(Integer.parseInt(input), file, this);
				service = Executors.newSingleThreadExecutor();
				service.submit(task);
				service.shutdown();
			} else
				return;
		}else{
			shutdown();
		}
    }
    @FXML
    private void onLoadButtonClicked(ActionEvent e){
    	shutdown();
    	FileChooser chooser = new FileChooser();
    	chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
    	chooser.setTitle("Locate the record file.");
    	chooser.getExtensionFilters().add(new ExtensionFilter("Record file","*.rec"));
    	file = chooser.showOpenDialog(stage);
    	if(file != null)message.setText("Loaded the record file");
    }
    private static final boolean isDecimal(String input){
    	return input.chars().allMatch(c -> Character.isDigit(c));
    }
    void setStage(Stage stage){this.stage = stage;}
    void complete(){started = false;}
    void shutdown(){
    	if(service != null)service.shutdownNow();
    }    
}