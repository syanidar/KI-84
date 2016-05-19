package jp.gr.java_conf.syanidar.chess.hamster.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Board;
import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Coordinates;
import jp.gr.java_conf.syanidar.chess.hamster.materials.PieceEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public final class AttackDetector {
	private static final Map<Board, AttackDetector> map = new HashMap<>();
	private final Board board;
	
	private AttackDetector(Board b){
		board = b;
	}
	
	public static final AttackDetector getInstance(Board board){
		if(map.containsKey(board))return map.get(board);
		else{
			AttackDetector result = new AttackDetector(board);
			map.put(board, result);
			return result;
		}
	}
	public boolean piecesCheckTheKingOf(ColorEnum color){
		Square king = board.squareMatchs(s -> s.isOccupied() && s.piece().get().isEqualTo(color, PieceEnum.KING)).get();
		return piecesAttackTheSquareOf(color, king.coordinates());
	}
	public boolean piecesAttackTheSquareOf(ColorEnum color, Coordinates square){
		for(Square enemy : board.squaresMatch(s -> s.isOccupiedBy(color.opposite()))){
			if(PieceTerritoryChecker.create(enemy).pieceControls(square))return true;
		}
		return false;
	}
	public List<Square> piecesWhichAttackTheSquareOf(ColorEnum color, Coordinates square){
		List<Square> result = new ArrayList<>();
		for(Square enemy : board.squaresMatch(s -> s.isOccupiedBy(color.opposite()))){
			if(PieceTerritoryChecker.create(enemy).pieceControls(square)){
				result.add(enemy);
			}
		}
		return result;
	}
}
