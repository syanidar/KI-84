package jp.gr.java_conf.syanidar.chess.hamster.game;

import jp.gr.java_conf.syanidar.chess.hamster.move.Move;

public class ChessMove implements jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move {

	private final Move move;
	private final ChessPosition cp;
	private boolean hasBeenPlayed;
	ChessMove(ChessPosition cp, Move move){
		this.cp = cp;
		this.move = move;
	}
	@Override
	public void play() {
		cp.update();
		move.play();
		hasBeenPlayed = true;
	}

	@Override
	public void undo() {
		cp.undo();
		move.undo();
		hasBeenPlayed = false;
	}
	@Override
	public String toString(){
		if(hasBeenPlayed)throw new IllegalStateException("This move has already been played.");
		
		play();
		boolean isACheck = cp.isInCheck();
		boolean isACheckmate = isACheck && cp.moves().size() == 0;
		undo();
		
		return move.toAlgebraicNotation(cp.board(), isACheck, isACheckmate);
	}
}
