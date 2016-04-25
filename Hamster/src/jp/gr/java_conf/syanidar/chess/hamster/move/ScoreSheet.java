package jp.gr.java_conf.syanidar.chess.hamster.move;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Piece;
import jp.gr.java_conf.syanidar.chess.hamster.materials.PieceEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public class ScoreSheet{
	private final List<MoveNotation> moves;
	private int halfMoveClock;
	private int fullMoveNum;
	
	public ScoreSheet(){
		moves = new ArrayList<>();
		halfMoveClock = 0;
		fullMoveNum = 1;
	}

	boolean pieceOnSquareHasMoved(Square square){
		if(!square.isOccupied())return true;
		for(MoveNotation m : moves){
			if(m.hasMoved(square.piece().get()))return true;
		}
		return false;
	}
	boolean hasMovedLast(Piece piece){
		return moves.get(moves.size() - 1).hasMoved(piece);
	}
	boolean InitialPawnPushHasBeenPlayedInvolving(Piece piece){
		int size = moves.size();
		if(size == 0)return false;
		
		MoveNotation last = moves.get(size - 1);
		if(!last.hasMoved(piece))return false;
		
		return last.isInitialPawnPush();
	}
	void add(Move move){
		MoveNotation notation = move.notation();
		moves.add(notation);	
		if(!notation.isTargetPresent() && !notation.hasMoved(PieceEnum.PAWN)){
			halfMoveClock++;
		}
		fullMoveNum++;
	}
	MoveNotation removeLast(){
		MoveNotation notation = moves.remove(moves.size() - 1);
		if(!notation.isTargetPresent() && !notation.hasMoved(PieceEnum.PAWN)){
			halfMoveClock--;
		}
		fullMoveNum--;
		return 	notation;
	}
	public int halfMoveClock(){return halfMoveClock;}
	public int fullMoveNumber(){return fullMoveNum;}
}
