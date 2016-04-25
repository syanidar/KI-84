package jp.gr.java_conf.syanidar.chess.hamster.tools;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Piece;
class TerritoryCheckerFactory {
	private TerritoryCheckerFactory(){}
	
	static final PieceTeritoryChecker create(Square s){
		Piece piece = s.piece().get();
		switch(piece.toEnum()){
			case KING:
				return new KingTeritoryChecker(s);
			case QUEEN:
				return new QueenTeritoryChecker(s);
			case ROOK:
				return new RookTeritoryChecker(s);
			case BISHIP:
				return new BishopTeritoryChecker(s);
			case KNIGHT:
				return new KnightTeritoryChecker(s);
			case PAWN:
				return new PawnTeritoryChecker(s, piece.color());
			default:
				throw new AssertionError();
		}
	}
}
