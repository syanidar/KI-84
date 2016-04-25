package jp.gr.java_conf.syanidar.chess.hamster.game;

import jp.gr.java_conf.syanidar.chess.hamster.move.Move;

public class ChessMove implements jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move {

	private final Move move;
	private final ChessPosition cp;
	ChessMove(ChessPosition cp, Move move){
		this.cp = cp;
		this.move = move;
	}
	@Override
	public void play() {
		cp.update();
		move.play();
	}

	@Override
	public void undo() {
		cp.undo();
		move.undo();
	}
	@Override
	public String toString(){
		return move.notation().toPureCoordinateNotation();
	}
}
