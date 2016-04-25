package jp.gr.java_conf.syanidar.chess.hamster.materials;


public abstract class Piece {
	private final ColorEnum color;
	
	Piece(ColorEnum c){
		assert c != null;
		color = c;
	}
	
	public ColorEnum color(){return color;}
	public abstract boolean isEqualTo(PieceEnum piece);
	public abstract boolean isEqualTo(ColorEnum color, PieceEnum piece);
	public abstract String toAlgebraicNotation();
	public abstract PieceEnum toEnum();
}
