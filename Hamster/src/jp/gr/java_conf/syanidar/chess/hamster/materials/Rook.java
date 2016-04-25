package jp.gr.java_conf.syanidar.chess.hamster.materials;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum.WHITE;


public class Rook extends Piece {

	public Rook(ColorEnum c) {
		super(c);
	}

	@Override
	public String toString(){return color() == WHITE ? "♖" : "♜";}
	@Override
	public boolean isEqualTo(PieceEnum piece) {
		return piece == PieceEnum.ROOK;
	}

	@Override
	public boolean isEqualTo(ColorEnum color, PieceEnum piece) {
		return color() == color && piece == PieceEnum.ROOK;
	}
	@Override
	public String toAlgebraicNotation(){return "R";}
	@Override
	public PieceEnum toEnum(){
		return PieceEnum.ROOK;
	}
}
