package jp.gr.java_conf.syanidar.reversi.jrev;
import static jp.gr.java_conf.syanidar.reversi.Board.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import jp.gr.java_conf.syanidar.reversi.*;
import jp.gr.java_conf.syanidar.reversi.jrev.JrevViewer.Players;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class JrevController implements Initializable{
	static final Board SENTINEL = new Board(BLACK, 0, 0);

	
	@FXML Pane boardPane;
	@FXML TextArea log;
	@FXML TextArea gameRecord;
	@FXML ProgressBar barBlack;
	@FXML ProgressBar barWhite;
	@FXML Label labelBlack;
	@FXML Label labelWhite;
	@FXML Label indicator;
	
	@FXML private ComboBox<String> depthEndField;
	@FXML private ComboBox<String> strengthField;
	@FXML private ComboBox<String> depthMiddleField;
	@FXML private TextField timeField;
	@FXML private RadioMenuItem timeDisableItem;
	@FXML private RadioMenuItem humanBlack;
	@FXML private RadioMenuItem pvsBlack;
	@FXML private RadioMenuItem mtdfBlack;
	@FXML private RadioMenuItem negaAlphaBlack;
	@FXML private RadioMenuItem monteCarloBlack;
	@FXML private RadioMenuItem humanWhite;
	@FXML private RadioMenuItem pvsWhite;
	@FXML private RadioMenuItem mtdfWhite;
	@FXML private RadioMenuItem negaAlphaWhite;
	@FXML private RadioMenuItem monteCarloWhite;
	
	private Board[] boards;
	private Board currentBoard;
	private ExecutorService service;
	private JrevViewer game;
	private Stage stage;
	
	private Players black;
	private Players white;
	
	
    @FXML
    void onNewGameClicked(ActionEvent event){
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("New Game");
		alert.setHeaderText("Are you sure you want to start a new game?");
		alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
			currentBoard = INITIAL_POSITION;
			gameRecord.clear();
			shutdown();
			start();
		});
    }
    @FXML
    void onResumeClicked(ActionEvent event) {
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Resume");
		alert.setHeaderText("Are you sure you want to resume?");
		alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
			shutdown();
			start();
		});
    }
    @FXML
    void onToTheFirstClicked(ActionEvent event){
    	if(1 < currentBoard.turn){
    		shutdown();
    		while (1 < currentBoard.turn && currentBoard != SENTINEL) {
    			Board tmp = currentBoard;
				currentBoard = boards[currentBoard.turn - 2];
				if (currentBoard != SENTINEL) {
					Board previousBoard = currentBoard.turn == 1 ? SENTINEL : boards[currentBoard.turn - 2];
					game.updateBoard(previousBoard, currentBoard);
					game.updateIndications(currentBoard);
					int length = gameRecord.getLength();
					gameRecord.deleteText(length - 2, length);
				}else{
					currentBoard = tmp;
					break;
				}
			}
    	}
    }
    @FXML
    void onUndoClicked(ActionEvent event){
    	if(1 < currentBoard.turn){
    		shutdown();
    		currentBoard = boards[currentBoard.turn - 2];
    		Board previousBoard = currentBoard.turn == 1 ? SENTINEL : boards[currentBoard.turn - 2];
    		game.updateBoard(previousBoard, currentBoard);
    		game.updateIndications(currentBoard);
    		int length = gameRecord.getLength();
    		gameRecord.deleteText(length - 2, length);
    	}
    }
    @FXML
    void onStopClicked(ActionEvent event){
    	shutdown();
    }
    @FXML
    void onRedoClicked(ActionEvent event){
    	if (currentBoard.turn != 61) {
    		shutdown();
			Board nextBoard = boards[currentBoard.turn];
			if(nextBoard != SENTINEL){
				game.updateBoard(currentBoard, nextBoard);
	    		game.updateGameRecord(currentBoard, nextBoard);
	    		game.updateIndications(nextBoard);
				currentBoard = nextBoard;
			}
		}
    }
    @FXML
    void onToTheLastClicked(ActionEvent event){
    	if (currentBoard.turn != 61) {
    		shutdown();
    		while (true) {
				Board nextBoard = boards[currentBoard.turn];
				if (nextBoard != SENTINEL) {
					game.updateBoard(currentBoard, nextBoard);
					game.updateGameRecord(currentBoard, nextBoard);
					game.updateIndications(nextBoard);
					currentBoard = nextBoard;
					if(currentBoard.turn == 61)break;
				} else break;
			}
		}
    }
    @FXML
    void onClearLogClicked(ActionEvent event){
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	alert.setTitle("Clear Log");
    	alert.setHeaderText("Are you sure you want to clear the log?");
    	alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
    		log.clear();
    	});
    }
    @FXML
    void onSavePositionClicked(ActionEvent event){
    	FileChooser chooser = new FileChooser();
    	chooser.setTitle("Locate the directory where you want to save the position.");
    	chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
    	chooser.setInitialFileName(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".ser");
    	chooser.getExtensionFilters().add(new ExtensionFilter("Position file","ser"));
    	File file = chooser.showSaveDialog(stage);
    	if(file != null){
    		ObjectOutputStream out = null;
    		try {
				out = new ObjectOutputStream(new FileOutputStream(file));
				out.writeObject(currentBoard);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    @FXML
    void onLoadPositionClicked(ActionEvent event){
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
					currentBoard = (Board) in.readObject();
					Arrays.fill(boards, SENTINEL);
					boards[currentBoard.turn - 1] = currentBoard;
					gameRecord.clear();
					shutdown();
					start();
				} catch (ClassNotFoundException e) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("WARNING");
					alert.setHeaderText("An exception has occured.");
					alert.setContentText("Invalid file.");
					alert.show();
				}
			} catch (FileNotFoundException e) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("WARNING");
				alert.setHeaderText("An exception has occured.");
				alert.setContentText("File not found.");
				alert.show();
			} catch (IOException e) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("WARNING");
				alert.setHeaderText("An exception has occured.");
				alert.setContentText("Invalid file.");
				alert.show();
			} 
		}
    }
    @FXML
    void onSaveLogClicked(ActionEvent event){
    	FileChooser chooser = new FileChooser();
    	chooser.setTitle("Locate the directory where you want to save the log.");
    	chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
    	chooser.setInitialFileName(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".txt");
    	chooser.getExtensionFilters().add(new ExtensionFilter("Text file","txt"));
    	File file = chooser.showSaveDialog(stage);
    	try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(log.getText());
			writer.close();
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText("An exception has occured.");
			alert.setContentText("Invalid file.");
			alert.show();
		}
    }
	@FXML
	private void onCopyButtonClicked(ActionEvent e){
		String record = gameRecord.getText();
		HashMap<DataFormat, Object> data = new HashMap<>();
		data.put(DataFormat.PLAIN_TEXT, record);
		Clipboard.getSystemClipboard().setContent(data);
	}
	@FXML
	private void onLaunchLearnerClicked(ActionEvent e){
		ProcessBuilder builder = new ProcessBuilder("java", "-jar", "Jrev Learner.jar");
		try {
			builder.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	@FXML
	private void onLaunchTesterClicked(ActionEvent e){
		ProcessBuilder builder = new ProcessBuilder("java", "-jar", "FFO Tester.jar");
		try {
			builder.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private final void start(){
		if(service == null)service = Executors.newSingleThreadExecutor();
		else {
			shutdown();
			service = Executors.newSingleThreadExecutor();
		}
		
		if(humanBlack.isSelected())black = Players.HUMAN;
		else if(pvsBlack.isSelected())black = Players.PVS;
		else if(mtdfBlack.isSelected())black = Players.MTDF;
		else if(negaAlphaBlack.isSelected())black = Players.NEGA_ALPHA;
		else black = Players.MONTE_CARLO;
		
		if(humanWhite.isSelected())white = Players.HUMAN;
		else if(pvsWhite.isSelected())white = Players.PVS;
		else if(mtdfWhite.isSelected())white = Players.MTDF;
		else if(negaAlphaWhite.isSelected())white = Players.NEGA_ALPHA;
		else white = Players.MONTE_CARLO;
		
		
		game = new JrevViewer(black, white, this, currentBoard);
		service.submit(game);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		depthMiddleField.getItems().addAll("1", "2", "3", "4", "6", "8", "10", "12", "14", "16");
		depthEndField.getItems().addAll("8", "10", "12", "14", "16", "18", "20", "22", "24", "26");
		strengthField.getItems().addAll("1000", "5000", "10000", "50000", "100000", "500000", "1000000", "5000000", "10000000", "50000000");
		currentBoard = INITIAL_POSITION;
		boards = new Board[61];
		Arrays.fill(boards, SENTINEL);
		start();
	}	
	int getDepthMiddle() {
		int result = 0;
		try {
			result = Integer.valueOf(depthMiddleField.getValue());
		} catch (NumberFormatException e) {
			return Integer.parseInt(depthMiddleField.getPromptText());		}
		return result;
	}
	int getDepthEnd() {
		int result = 0;
		try {
			result = Integer.valueOf(depthEndField.getValue());
		} catch (NumberFormatException e) {
			return Integer.parseInt(depthEndField.getPromptText());		}
		return result;
	}
	int getStrength() {
		int result = 0;
		try {
			result = Integer.valueOf(strengthField.getValue());
		} catch (NumberFormatException e) {
			return Integer.parseInt(strengthField.getPromptText());		}
		return result;
	}	
	long getTime() {
		int time = 0;
		try {
			time = Integer.parseInt(timeField.getText()) * 1000000;
		} catch (NumberFormatException e) {
			return timeDisableItem.isSelected() ? Long.MAX_VALUE : Integer.parseInt(timeField.getPromptText()) * 1000000;
		}
		return timeDisableItem.isSelected() ? Long.MAX_VALUE : time;
	}
	
	Stage getStage(){return stage;}
	void setStage(Stage stage){this.stage = stage;}
	void shutdown(){if(service != null)service.shutdownNow();}
	void setCurrentBoard(Board board){this.currentBoard = board;}
	void put(int index, Board board){boards[index] = board;}
	void fill(int index, Board board){for(int i = index; i < 61; i++)boards[i] = board;}
	Board getCurrentBoard(){return this.currentBoard;}
}
