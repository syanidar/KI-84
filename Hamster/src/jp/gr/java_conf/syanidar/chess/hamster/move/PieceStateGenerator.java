package jp.gr.java_conf.syanidar.chess.hamster.move;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Board;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

class PieceStateGenerator {
	private PieceStateGenerator(){}
	
	static final PieceState generate(MoveRecorder recorder, Board board, Square square, Square kr, Square qr, ScoreSheet ss){
		assert recorder != null;
		assert board != null;
		assert square != null && square.isOccupied();
		assert kr != null;
		assert qr != null;
		
		switch(square.piece().get().toEnum()){
		case KING:
			return new KingState(recorder, board, square, kr, qr,ss);
		case QUEEN:
			return new QueenState(recorder, square);
		case ROOK:
			return new RookState(recorder, square);
		case BISHIP:
			return new BishopState(recorder, square);
		case KNIGHT:
			return new KnightState(recorder, square);
		case PAWN:
			return new PawnState(recorder, square, ss);
		default:
			throw new AssertionError();
		}
	}
}
