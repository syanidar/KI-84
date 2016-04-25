package jp.gr.java_conf.syanidar.chess.hamster.tools;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Board;
import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public final class AttackDetector {
	private final Board board;
	public AttackDetector(Board b){
		board = b;
	}
	
	public boolean piecesCheckTheKingOf(ColorEnum color){
		KingSearcher ks = new KingSearcher(board);
		Square king = ks.squareOccupiedByTheKingOf(color);
		return piecesAttackTheSquareOf(color, king);
	}
	public boolean piecesAttackTheSquareOf(ColorEnum color, Square square){
		for(Square enemy : board.squaresMatch(s -> s.isOccupiedBy(color.opposite()))){
			if(TerritoryCheckerFactory.create(enemy).pieceControls(square))return true;
		}
		return false;
	}
}
