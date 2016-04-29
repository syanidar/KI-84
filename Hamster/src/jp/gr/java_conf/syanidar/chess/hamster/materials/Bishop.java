package jp.gr.java_conf.syanidar.chess.hamster.materials;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum.WHITE;


public class Bishop extends Piece {

	public Bishop(ColorEnum c) {
		super(c);
	}

	@Override
	public String toString(){return color() == WHITE ? "B" : "b";}

	@Override
	public boolean isEqualTo(PieceEnum piece) {
		return piece == PieceEnum.BISHIP;
	}

	@Override
	public boolean isEqualTo(ColorEnum color, PieceEnum piece) {
		return color() == color && piece == PieceEnum.BISHIP;
	}
	@Override
	public String toAlgebraicNotation(){return "B";}
	@Override
	public String toIcon(){
		return color() == WHITE ? "♗" : "♝";
	}
	@Override
	public PieceEnum toEnum(){
		return PieceEnum.BISHIP;
	}
}
