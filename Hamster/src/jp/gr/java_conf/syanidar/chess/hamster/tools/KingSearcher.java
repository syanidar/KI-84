package jp.gr.java_conf.syanidar.chess.hamster.tools;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Board;
import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.PieceEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public final class KingSearcher {
	private final Board board;
	public KingSearcher(Board b){board = b;}
	public Square squareOccupiedByTheKingOf(ColorEnum color){
		return board.squareMatchs(s -> {
			return s.isOccupied() && s.piece().get().isEqualTo(color, PieceEnum.KING);
		}).get();
	}
}
