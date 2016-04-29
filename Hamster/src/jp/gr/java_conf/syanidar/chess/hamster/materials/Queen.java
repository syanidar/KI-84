package jp.gr.java_conf.syanidar.chess.hamster.materials;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum.WHITE;


public class Queen extends Piece {

	public Queen(ColorEnum c) {
		super(c);
	}

	@Override
	public String toString(){return color() == WHITE ? "Q" : "q";}
	@Override
	public boolean isEqualTo(PieceEnum piece) {
		return piece == PieceEnum.QUEEN;
	}

	@Override
	public boolean isEqualTo(ColorEnum color, PieceEnum piece) {
		return color() == color && piece == PieceEnum.QUEEN;
	}
	@Override
	public String toAlgebraicNotation(){return "Q";}
	@Override
	public PieceEnum toEnum(){
		return PieceEnum.QUEEN;
	}
	@Override
	public String toIcon(){
		return color() == WHITE ? "♕" : "♛";
	}
}
