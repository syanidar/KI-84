package jp.gr.java_conf.syanidar.chess.hamster.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.syanidar.chess.hamster.move.*;
import jp.gr.java_conf.syanidar.chess.hamster.tools.AttackDetector;
import jp.gr.java_conf.syanidar.util.collection.ListUtility;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;
import jp.gr.java_conf.syanidar.chess.hamster.materials.*;

public class ChessPosition implements Position<ChessMove> {
	private final Board board;
	private final ScoreSheet ss;
	private final LegalityTester lt;
	private final LegalMoveGenerator lmg;
	private final AttackDetector ad;
	private final List<BoardSnapshot> snapshots;
	
	private ColorEnum colorToPlay;
	
	public ChessPosition(){
		board = new Board();
		ss = new ScoreSheet();
		ad = AttackDetector.getInstance(board);
		lt = new DefaultLegalityTester(ad);
		lmg = new DefaultMoveGenerator(board, ss, lt);
		snapshots = new ArrayList<>();
		colorToPlay = ColorEnum.WHITE;
	}
	@Override
	public List<ChessMove> moves() {
		if(!hasEnoughMaterials())return new ArrayList<ChessMove>();
		
		List<Move> moves = lmg.generateLegalMoves(colorToPlay);
		return ListUtility.map(moves, m -> new ChessMove(this, m));
	}
	public void changeTheSide(){
		colorToPlay = colorToPlay.opposite();
	}
	@Override
	public boolean theFirstPlayerHasTheMove(){
		return colorToPlay == ColorEnum.WHITE;
	}
	public Board board(){
		return board;
	}
	public ColorEnum colorToPlay(){
		return colorToPlay;
	}
	public boolean isInCheck(){
		return ad.piecesCheckTheKingOf(colorToPlay);
	}
	public boolean fiftyMoveRuleApplied(){
		return ss.halfMoveClock() >= 100;
	}
	public boolean threeMoveRepetitionHasHappened(){
		Map<BoardSnapshot, Integer> map = new HashMap<>();
		
		for(BoardSnapshot bs : snapshots){
			if(!map.containsKey(bs)){
				map.put(bs, 1);
			}else{
				map.put(bs, map.get(bs) + 1);
			}
		}
		return map.values().retainAll(java.util.Arrays.asList(1, 2));
	}
	public boolean hasEnoughMaterials(){
		List<Square> wPieces = board.squaresMatch(s -> s.isOccupiedBy(ColorEnum.WHITE));
		if(wPieces.size() > 2)return true;
		List<Square> bPieces = board.squaresMatch(s -> s.isOccupiedBy(ColorEnum.BLACK));
		if(bPieces.size() > 2)return true;
		
		if(wPieces.size() == 1 && bPieces.size() == 1)return false;
		
		List<Square> superior = wPieces.size() == 0 ? bPieces : wPieces;
		for(Square s : superior){
			Piece piece = s.piece().get();
			if(piece.isEqualTo(PieceEnum.BISHIP))return false;
			if(piece.isEqualTo(PieceEnum.KNIGHT))return false;
		}
		return true;
	}
	public boolean isQuiet(){
		ColorEnum enemy = colorToPlay.opposite();
		for(Square s : board.squaresMatch(s -> s.isOccupiedBy(enemy))){
			if(ad.piecesAttackTheSquareOf(enemy, s.coordinates()))return false;
		}
		return true;
	}
	void update(){
		changeTheSide();
		snapshots.add(board.snapshot());
	}
	void undo(){
		changeTheSide();
		snapshots.remove(snapshots.size() - 1);
	}
	@Override
	public String toString(){
		return board.toString();
	}
}
