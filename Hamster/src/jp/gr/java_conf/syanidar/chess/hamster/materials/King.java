package jp.gr.java_conf.syanidar.chess.hamster.materials;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum.WHITE;


public class King extends Piece {

	public King(ColorEnum c) {
		super(c);
	}

	@Override
	public String toString(){return color() == WHITE ? "K" : "k";}

	@Override
	public boolean isEqualTo(PieceEnum piece) {
		return piece == PieceEnum.KING;
	}

	@Override
	public boolean isEqualTo(ColorEnum color, PieceEnum piece) {
		return color() == color && piece == PieceEnum.KING;
	}
	@Override
	public String toAlgebraicNotation(){return "K";}
	@Override
	public PieceEnum toEnum(){
		return PieceEnum.KING;
	}
	@Override
	public String toIcon(){
		return color() == WHITE ? "♔" : "♚";
	}
}
