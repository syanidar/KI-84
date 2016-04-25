package jp.gr.java_conf.syanidar.chess.hamster.move;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Board;
import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.*;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;
import jp.gr.java_conf.syanidar.chess.hamster.tools.AttackDetector;

class CastlingMoveGenerator implements MoveGenerator {
	private final MoveRecorder recorder;
	private final Board board;
	private final Square king;
	private final Square kingsRook;
	private final Square queensRook;
	private final ScoreSheet ss;
	
	CastlingMoveGenerator(MoveRecorder m, Board b, Square k, Square kr, Square q, ScoreSheet s){
		assert m != null;
		assert  b != null;
		assert k != null;
		assert kr != null;
		assert q != null;
		assert s != null;
		recorder = m;
		board = b;
		king = k;
		kingsRook = kr;
		queensRook = q;
		ss = s;
	}
	@Override
	public List<Move> generate(ColorEnum color) {
		if(color == null)throw new IllegalArgumentException();
		List<Move> result = new ArrayList<>();
		AttackDetector ad = new AttackDetector(board);
		if(ss.pieceOnSquareHasMoved(king))return result;
		if(ad.piecesCheckTheKingOf(color))return result;
		
		Optional.of(kingsRook).filter(n -> !ss.pieceOnSquareHasMoved(n))
		.flatMap(n -> n.next(WEST)).filter(n -> !n.isOccupied())
		.flatMap(n -> n.next(WEST)).filter(n -> !n.isOccupied() && !ad.piecesAttackTheSquareOf(color, n))
		.ifPresent(n -> result.add(new Move(recorder, new Walk(king, n.next(EAST).get()), new Walk(kingsRook, n))));
		
		Optional.of(queensRook).filter(n -> !ss.pieceOnSquareHasMoved(n))
		.flatMap(n -> n.next(EAST)).filter(n -> !n.isOccupied())
		.flatMap(n -> n.next(EAST)).filter(n -> !n.isOccupied())
		.flatMap(n -> n.next(EAST)).filter(n -> !n.isOccupied() && !ad.piecesAttackTheSquareOf(color, n))
		.ifPresent(n -> result.add(new Move(recorder, new Walk(king, n.next(WEST).get()), new Walk(queensRook, n))));
		
		return result;
	}
}
