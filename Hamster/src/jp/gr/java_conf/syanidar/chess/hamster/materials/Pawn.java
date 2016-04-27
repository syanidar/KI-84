package jp.gr.java_conf.syanidar.chess.hamster.materials;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum.WHITE;


public class Pawn extends Piece {

	public Pawn(ColorEnum c) {
		super(c);
	}


	@Override
	public String toString(){return color() == WHITE ? "P" : "p";}

	@Override
	public boolean isEqualTo(PieceEnum piece) {
		return piece == PieceEnum.PAWN;
	}

	@Override
	public boolean isEqualTo(ColorEnum color, PieceEnum piece) {
		return color() == color && piece == PieceEnum.PAWN;
	}
	@Override
	public String toAlgebraicNotation(){return "";}
	@Override
	public PieceEnum toEnum(){
		return PieceEnum.PAWN;
	}
}
