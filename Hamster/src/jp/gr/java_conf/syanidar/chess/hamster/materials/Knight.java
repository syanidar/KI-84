package jp.gr.java_conf.syanidar.chess.hamster.materials;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum.WHITE;

public class Knight extends Piece {

	public Knight(ColorEnum c) {
		super(c);
	}

	@Override
	public String toString(){return color() == WHITE ? "N" : "n";}

	@Override
	public boolean isEqualTo(PieceEnum piece) {
		return piece == PieceEnum.KNIGHT;
	}

	@Override
	public boolean isEqualTo(ColorEnum color, PieceEnum piece) {
		return color() == color && piece == PieceEnum.KNIGHT;
	}
	@Override
	public String toAlgebraicNotation(){return "N";}
	@Override
	public PieceEnum toEnum(){
		return PieceEnum.KNIGHT;
	}
}
