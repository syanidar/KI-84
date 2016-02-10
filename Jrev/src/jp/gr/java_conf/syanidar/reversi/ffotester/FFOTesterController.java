package jp.gr.java_conf.syanidar.reversi.ffotester;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import jp.gr.java_conf.syanidar.reversi.Board;
import javafx.stage.FileChooser;
import javafx.fxml.Initializable;

public class FFOTesterController implements Initializable{
    @FXML TextArea log;
    
    private Board board;
    private Stage stage;
    private ExecutorService service;

    @FXML
    void onLoadClicked(ActionEvent event) {
		shutdown();
    	FileChooser chooser = new FileChooser();
    	chooser.setTitle("Locate the position file.");
    	chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
    	chooser.getExtensionFilters().add(new ExtensionFilter("Position file","*.ser"));
    	File file = chooser.showOpenDialog(stage);
    	if (file != null) {
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				try {
					board = (Board) in.readObject();
					stage.setTitle("Loaded the position");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					stage.setTitle("Invalid file.");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				stage.setTitle("File not found.");
			} catch (IOException e) {
				e.printStackTrace();
				stage.setTitle("Invalid file.");
			} 
		}
    }

    @FXML
    void onSaveClicked(ActionEvent event) {
    	try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt"));
			writer.write(log.getText());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void onStartClicked(ActionEvent event) {
    	if (board != Board.INITIAL_POSITION) {
			shutdown();
			service = Executors.newSingleThreadExecutor();
			FFOTesterViewer game = new FFOTesterViewer(this, board);
			service.submit(game);
		}
    }
    
    @FXML
    void onClearClicked(ActionEvent event) {
    	log.clear();
    }
    void setStage(Stage stage){this.stage = stage;}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		board = Board.INITIAL_POSITION;
	}
	void shutdown(){
		if(service != null)service.shutdownNow();
	}
	Stage getStage(){return  stage;}
}
