package jp.gr.java_conf.syanidar.chess.hamster.move;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Piece;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

class Elimination implements Event {
	private final Square location;
	private final Piece piece;
	
	Elimination(Square s){
		assert s != null;
		location = s; piece = s.piece().get();
	}	
	@Override
	public void play() {
		assert location.isOccupied();
		location.remove();
	}
	@Override
	public void undo() {
		assert !location.isOccupied();
		location.put(piece);
	}
	@Override
	public int priority(){return 1;}
	
	Piece target(){return piece;}
}
