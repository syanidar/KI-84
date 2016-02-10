package jp.gr.java_conf.syanidar.reversi.learner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.concurrent.Task;
import jp.gr.java_conf.syanidar.reversi.Counter;
import jp.gr.java_conf.syanidar.reversi.DataBasePlayer;
import jp.gr.java_conf.syanidar.reversi.GameKeeper;
import jp.gr.java_conf.syanidar.reversi.PatternRecognitioner;
import jp.gr.java_conf.syanidar.reversi.Player;
import jp.gr.java_conf.syanidar.reversi.Teacher;
import jp.gr.java_conf.syanidar.reversi.Viewer;
public class JrevLearnerViewer extends Task<Void>{
	private final int input;
	private final File file;
	private final long startTime;
	
	JrevLearnerViewer(int times, File file, JrevLearnerController controller){
		this.input = times;
		this.file = file;
		this.startTime = System.nanoTime();
		progressProperty().addListener((observable, oldValue, newValue) -> {
			double progress = newValue.doubleValue();
			controller.progressBar.setProgress(progress);
			controller.progressIndicator.setText((int)(progress * 100) + "%, " + (System.nanoTime() - startTime) / 60000000000L + "min");
			if(oldValue.intValue() != -1)controller.textField.setText(String.valueOf(Integer.parseInt(controller.textField.getText()) - 1));
		});
		messageProperty().addListener((observable, oldValue, newValue) -> controller.message.setText(newValue));
		setOnSucceeded(e -> {
			controller.textField.setText(String.valueOf(Integer.parseInt(controller.textField.getText()) - 1));
			controller.progressIndicator.setText("Completed");
			controller.startButton.setText("Start");
			controller.complete();
		});
		setOnFailed(e -> {
			controller.progressIndicator.setText("Completed");
			controller.startButton.setText("Start");
			controller.complete();
		});
	}
	@Override
	protected Void call() throws Exception{
		updateMessage("Processing...");
		PatternRecognitioner student = PatternRecognitioner.INSTANCE;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			for (int i = 0; i < input; i++) {
				updateProgress(i, input);
				Counter[] squareDifferenceCounters = new Counter[61];
				for(int j = 0; j < 61; j++)squareDifferenceCounters[j] = new Counter();
				Counter[] counters = new Counter[61];
				for(int j = 0; j < 61; j++)counters[j] = new Counter();
				
				reader = new BufferedReader(new FileReader(file));
				ExecutorService games = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
				int gameId = 0;
				for (String line = DataBasePlayer.readNextValidLine(reader); line != null; line = DataBasePlayer
						.readNextValidLine(reader)) {
					if(Thread.interrupted()){
						games.shutdownNow();
						throw new InterruptedException();
					}
					final String game = line;
					games.submit(() -> {
						Viewer teacher = new Teacher(student, game, squareDifferenceCounters, counters);
						Player player = new DataBasePlayer(teacher, game);
						GameKeeper keeper = new GameKeeper(player, player);
						try {
							keeper.excute();
						} catch (InterruptedException e) {}
					});
					updateMessage("Loaded " + gameId + 1 + "th game.");
					gameId++;
				}
				if(gameId == 0){
					games.shutdownNow();
					throw new IOException();
				}
				updateMessage("Learning from " + gameId +" games. This would take a while.");
				games.shutdown();
				try {
					games.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
				} catch (InterruptedException e) {
					games.shutdownNow();
					throw e;
				}
				
				double[] standardDeviations = new double[61];
				for (int j = 0; j < 61; j++)
					standardDeviations[j] = Math.pow(squareDifferenceCounters[j].tally(), 0.5) / Math.pow(counters[j].tally(), 0.5);
				updateMessage("ƒÐ(turn = 61)= " + standardDeviations[60]);
				writer = new BufferedWriter(new FileWriter("tables" + File.separator + "log.txt"));
				writer.write("ƒÐ:" + Arrays.toString(standardDeviations));
				writer.write(System.lineSeparator());
				updateMessage("Updating evaluation function...");
				student.learn();
				updateMessage("Completed update.");
			}
		} catch (IOException e) {
			updateMessage("An error has occured while operating files. Terninating...");
			throw new RuntimeException();
		}catch(InterruptedException e){
			updateMessage("Saving the changes...");
			student.overwrite();
			updateMessage("Saved files successfully.");
			throw e;
		}finally{
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
		updateMessage("Saving the changes...");
		student.overwrite();
		updateMessage("Saved files successfully.");
		return null;
	}
}
