package jp.gr.java_conf.syanidar.reversi.jrev;

import static jp.gr.java_conf.syanidar.reversi.Board.*;
import static jp.gr.java_conf.syanidar.reversi.jrev.JrevController.SENTINEL;

import java.util.ArrayList;

import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import jp.gr.java_conf.syanidar.reversi.*;

public class JrevViewer extends Task<Board> implements Viewer{
	private final JrevController controller;
	private final Player black;
	private final Player white;
	private final GameKeeper keeper;
	
	private Point inputMove;
	private volatile boolean inputRequested;
	private volatile boolean completedUpdate;
	
	static enum Players{
		HUMAN,
		PVS,
		MTDF,
		NEGA_ALPHA,
		MONTE_CARLO;
	}
	JrevViewer(Players black, Players white, JrevController controller, Board board){
		switch(black){
		case HUMAN:
			this.black = new Human(this);
			break;
		case PVS:
			this.black = new PVS(this, PatternRecognitioner.INSTANCE, controller.getDepthMiddle(), controller.getDepthEnd(), controller.getTime());
			break;
		case MTDF:
			this.black = new MTDF(this, PatternRecognitioner.INSTANCE, controller.getDepthMiddle(), controller.getDepthEnd(), controller.getTime());
			break;
		case NEGA_ALPHA:
			this.black = new NegaAlpha(this, PatternRecognitioner.INSTANCE, controller.getDepthMiddle(), controller.getDepthEnd(), controller.getTime());
			break;
		case MONTE_CARLO:
			this.black = new MonteCarlo(this, controller.getStrength(), controller.getTime());
			break;
		default:
			throw new AssertionError();	
		}
		switch(white){
		case HUMAN:
			this.white = new Human(this);
			break;
		case PVS:
			this.white = new PVS(this, PatternRecognitioner.INSTANCE, controller.getDepthMiddle(), controller.getDepthEnd(), controller.getTime());
			break;
		case MTDF:
			this.white = new MTDF(this, PatternRecognitioner.INSTANCE, controller.getDepthMiddle(), controller.getDepthEnd(), controller.getTime());
			break;
		case NEGA_ALPHA:
			this.white = new NegaAlpha(this, PatternRecognitioner.INSTANCE, controller.getDepthMiddle(), controller.getDepthEnd(), controller.getTime());
			break;
		case MONTE_CARLO:
			this.white = new MonteCarlo(this, controller.getStrength(), controller.getTime());
			break;
		default:
			throw new AssertionError();	
		}
		
		this.controller = controller;
		keeper = new GameKeeper(this.black, this.white, board);
		
		valueProperty().addListener((observable, oldValue, newValue) -> {
			controller.getStage().getScene().setCursor(Cursor.DEFAULT);
			oldValue = oldValue == null ? SENTINEL : oldValue;
			update(oldValue, newValue);
			Players currentPlayer = newValue.colorToPlay == BLACK ? black : white;
			if(currentPlayer != Players.HUMAN)controller.getStage().getScene().setCursor(Cursor.WAIT);
		});
		
		controller.boardPane.setOnMouseClicked(e -> {
			if(inputRequested){
				int column = (int)e.getX() / 40;
				int row = (int)e.getY() / 40;
				Point move = new Point(column, row);
				if(controller.getCurrentBoard().hasMove(new Point(column, row))){
					inputMove = move;
					inputRequested = false;
				}
			}
		});
	}
	@Override
	protected void succeeded(){
		controller.getStage().getScene().setCursor(Cursor.DEFAULT);
	}
	@Override
	protected void failed(){
		controller.getStage().getScene().setCursor(Cursor.DEFAULT);
	}

	@Override
	public void update(Board board) throws InterruptedException {
		if(Thread.interrupted())throw new InterruptedException();
		
		completedUpdate = false;
		updateValue(board);
		while(!completedUpdate){
			if(Thread.interrupted())throw new InterruptedException();
		}
		System.out.println(board);
	}

	@Override
	public void end(Board board) throws InterruptedException {
		if(Thread.interrupted())throw new InterruptedException();
		System.out.println(board);
	}

	@Override
	public Point getPoint(Board board) throws InterruptedException {
		inputRequested = true;
		while(inputRequested){
			if(Thread.interrupted())throw new InterruptedException();
		}
		return inputMove;
	}
	@Override
	public void initialize(Board board) {
	}
	
	
	
	void update(Board previousBoard, Board newBoard){
		controller.setCurrentBoard(newBoard);
		controller.put(newBoard.turn - 1, newBoard);
		controller.fill(newBoard.turn, SENTINEL);
		updateBoard(previousBoard, newBoard);
		updateGameRecord(previousBoard, newBoard);
		updateLog(previousBoard, newBoard);
		updateIndications(newBoard);
		completedUpdate = true;
	}
	void updateBoard(Board previousBoard, Board newBoard) {
		ArrayList<ImageView> discs = new ArrayList<>();
		for(int row = 0; row < 8; row++){
			for(int column = 0; column < 8; column++){
				switch(newBoard.squareAt(column, row)){
				case BLACK:
					discs.add(newDisc(getClass(), column, row, BLACK));
					break;
				case WHITE:
					discs.add(newDisc(getClass(), column, row, WHITE));
					break;
				case EMPTY:
					if(newBoard.hasMove(column, row))discs.add(newDisc(getClass(), column, row, EMPTY));
					break;
				}
			}
		}
		controller.boardPane.getChildren().clear();
		controller.boardPane.getChildren().addAll(discs);
	}
	final void updateGameRecord(Board previousBoard, Board newBoard){
		if(previousBoard != SENTINEL)controller.gameRecord.appendText(computeMove(previousBoard, newBoard));
	}
	final void updateLog(Board previousBoard, Board newBoard){
		TextArea log = controller.log;
		if (previousBoard != SENTINEL) {
			log.appendText(previousBoard.colorToPlay == BLACK ? black.toString() : white.toString());
			log.appendText(System.lineSeparator());
		}
		log.appendText(newBoard.toString());
		log.appendText(System.lineSeparator());
		log.end();
	}
	final void updateIndications(Board newBoard){
		int numOfBlackDiscs = Long.bitCount(newBoard.bitBoardBlack);
		int numOfWhiteDiscs = (newBoard.turn + 3) - numOfBlackDiscs;
		
		Label indicator = controller.indicator;
		if (newBoard.hasMoves()) {
			if (newBoard.colorToPlay == BLACK)
				indicator.setText("Black's turn to play");
			else
				indicator.setText("White's turn to play");
		}else{
			if(newBoard.isFinal()){
				if(numOfBlackDiscs < numOfWhiteDiscs)indicator.setText("White wins!");
				else if(numOfBlackDiscs == numOfWhiteDiscs)indicator.setText("Draw game");
				else indicator.setText("Black wins!");
			}else indicator.setText("No valid moves");
		}
		
		controller.labelBlack.setText(String.valueOf(numOfBlackDiscs));
		controller.labelWhite.setText(String.valueOf(numOfWhiteDiscs));
		
		controller.barBlack.setProgress((double)numOfBlackDiscs / (newBoard.turn + 3));
		controller.barWhite.setProgress((double)numOfWhiteDiscs / (newBoard.turn + 3));	
	}
	private static final ImageView newDisc(Class<?> resourceClass, int column, int row, int type){
		ImageView result = null;
		switch(type){
		case BLACK:
			result =  new ImageView(resourceClass.getResource("disc_black.png").toString());
			break;
		case WHITE:
			result = new ImageView(resourceClass.getResource("disc_white.png").toString());
			break;
		case EMPTY:
			result = new ImageView(resourceClass.getResource("light.png").toString());
			break;
		default:
			throw new AssertionError();
		}
		result.setX(40 * column);
		result.setY(40 * row);
		result.	setFitHeight(40);
		result.setFitWidth(40);
		return result;
	}
	private static final String computeMove(Board before, Board after){
		long move = (after.bitBoardBlack | after.bitBoardWhite) - (before.bitBoardBlack | before.bitBoardWhite);
		int index = Long.numberOfLeadingZeros(move);
		char column = (char)('A' + (index & 7));
		char row = (char)('1' + (index >>> 3));
		char[] tmp = new char[2];
		tmp[0] = column;tmp[1] = row;
		return new String(tmp);
	}

	@Override
	public Board call() throws Exception {
		try {
			keeper.excute();
		} catch (InterruptedException e) {
			return getValue();
		}
		return keeper.getBoard();
	}
}
