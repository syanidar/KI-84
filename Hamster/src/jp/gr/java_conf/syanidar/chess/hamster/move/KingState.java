package jp.gr.java_conf.syanidar.chess.hamster.move;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Board;
import jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.*;

class KingState implements PieceState {
	private static final Set<DirectionEnum> directions = EnumSet.of(NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST);
	private final MoveRecorder recorder;
	private final Board board;
	private final Square square;
	private final Square kingsRook;
	private final Square queensRook;
	private final ScoreSheet ss;
	KingState(MoveRecorder m, Board b, Square s, Square k, Square q, ScoreSheet c){
		assert m != null;
		assert b != null;
		assert s != null && s.isOccupied();
		assert k != null;
		assert q != null;
		assert c != null;
		recorder = m;
		board = b;
		square = s;
		kingsRook = k;
		queensRook = q;
		ss = c;
	}
	@Override
	public List<MoveGenerator> moveGenerators() {
		assert square.isOccupied();
		List<MoveGenerator> result = new ArrayList<>();
		directions.forEach(d -> result.add(new StepMoveGenerator(recorder, d, square)));
		result.add(new CastlingMoveGenerator(recorder, board, square, kingsRook, queensRook, ss));
		return result;
	}

}
