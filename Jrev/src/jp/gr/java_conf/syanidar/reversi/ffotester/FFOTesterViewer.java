package jp.gr.java_conf.syanidar.reversi.ffotester;

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

import jp.gr.java_conf.syanidar.reversi.*;

public class FFOTesterViewer extends Task<Board> implements Viewer{

	private final FFOTesterController controller;
	private final Board board;
	private final Player com;
	
    private volatile boolean completedUpdate;

	
	FFOTesterViewer(FFOTesterController controller, Board board){
		this.controller = controller;
		this.board = board;
		this.com = new PVS(this, PatternRecognitioner.INSTANCE, 1, 40, 1000000000);
		TextArea log = controller.log;
		valueProperty().addListener((observable, oldValue, newValue) -> {
			log.appendText(com.toString());
			log.appendText(System.lineSeparator());
			log.appendText(newValue.toString());
			log.appendText(System.lineSeparator());
			completedUpdate = true;
		});
	}
	@Override
	public void update(Board board) throws InterruptedException {
		if(Thread.interrupted())throw new InterruptedException();
		completedUpdate = false;
		updateValue(board);
		while(!completedUpdate){
			if(Thread.interrupted())throw new InterruptedException();
		}
	}

	@Override
	public void end(Board board) throws InterruptedException {
		if(Thread.interrupted())throw new InterruptedException();
		updateValue(board);
		while(!completedUpdate){
			if(Thread.interrupted())throw new InterruptedException();
		}
	}

	@Override
	public Point getPoint(Board board) throws InterruptedException {
		throw new AssertionError();
	}

	@Override
	public void initialize(Board board) {
	}
	
	@Override
	public Board call() {
		GameKeeper keeper = new GameKeeper(com, com, board);
		try {
			keeper.excute();
		} catch (InterruptedException e) {
			return getValue();
		}
		controller.getStage().setTitle("Finished.");
		return getValue();
	}

}
