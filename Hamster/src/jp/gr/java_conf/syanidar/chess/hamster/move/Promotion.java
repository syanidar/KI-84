package jp.gr.java_conf.syanidar.chess.hamster.move;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Piece;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

class Promotion implements Event {
	private final Square location;
	private final Piece piece;
	private final Piece pawn;
	
	Promotion(Square l, Piece pawn, Piece p){
		assert l != null;
		assert p != null;
		location = l; piece = p; this.pawn = pawn;
	}

	@Override
	public void play() {
		assert location.isOccupied() && location.piece().get() == pawn;
		location.remove();
		location.put(piece);
	}
	@Override
	public void undo() {
		assert location.isOccupied() && location.piece().get() == piece;
		location.remove();
		location.put(pawn);
	}
	@Override
	public int priority(){
		return 3;
	}
	Piece promotee(){return piece;}
}
