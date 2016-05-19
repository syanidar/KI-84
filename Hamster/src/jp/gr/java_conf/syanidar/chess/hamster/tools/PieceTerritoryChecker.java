package jp.gr.java_conf.syanidar.chess.hamster.tools;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Coordinates;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Piece;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public abstract class PieceTerritoryChecker {
	PieceTerritoryChecker(){}
	public abstract boolean pieceControls(Coordinates c);
	static final PieceTerritoryChecker create(Square s){
		Piece piece = s.piece().get();
		switch(piece.toEnum()){
			case KING:
				return new KingTerritoryChecker(s);
			case QUEEN:
				return new QueenTerritoryChecker(s);
			case ROOK:
				return new RookTerritoryChecker(s);
			case BISHIP:
				return new BishopTerritoryChecker(s);
			case KNIGHT:
				return new KnightTerritoryChecker(s);
			case PAWN:
				return new PawnTerritoryChecker(s, piece.color());
			default:
				throw new AssertionError();
		}
	}
}
