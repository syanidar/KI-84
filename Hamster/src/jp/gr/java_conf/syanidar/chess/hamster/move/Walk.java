package jp.gr.java_conf.syanidar.chess.hamster.move;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Piece;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

class Walk implements Event {
	private final Piece piece;
	private final Square origin;
	private final Square destination;
	Walk(Square o, Square d){
		assert o != null;
		assert d != null;
		piece = o.piece().get(); origin = o; destination = d;} 
	@Override
	public void play() {
		assert origin.isOccupied();
		assert !destination.isOccupied():origin.toAlgebraicNotation() + "->" + destination.toAlgebraicNotation() + System.lineSeparator() + origin.board().toString();
		destination.put(origin.remove());
	}
	@Override
	public void undo() {
		assert destination.isOccupied();
		assert !origin.isOccupied();
		origin.put(destination.remove());
	}
	@Override
	public int priority(){
		return 2;
	}
	
	Piece piece(){return piece;}
	Square origin(){return origin;}
	Square destination(){return destination;}
}
