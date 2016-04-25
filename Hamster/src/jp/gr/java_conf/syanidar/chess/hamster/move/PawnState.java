package jp.gr.java_conf.syanidar.chess.hamster.move;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

class PawnState implements PieceState {
	private static final Set<DirectionEnum> directionsForWhite = EnumSet.of(NORTH_EAST, NORTH_WEST);
	private static final Set<DirectionEnum> directionsForBlack = EnumSet.of(SOUTH_EAST, SOUTH_WEST);
	
	private final MoveRecorder recorder;
	private final Square square;
	private final ScoreSheet ss;
	PawnState(MoveRecorder m, Square s,ScoreSheet c){
		assert m != null;
		assert s != null && s.isOccupied();
		assert c != null;
		recorder = m;
		square = s;
		ss = c;
	}
	@Override
	public List<MoveGenerator> moveGenerators() {
		assert square.isOccupied();
		ColorEnum color = square.piece().get().color();
		List<MoveGenerator> result = new ArrayList<>();
		for(DirectionEnum direction : color == ColorEnum.WHITE ? directionsForWhite : directionsForBlack){
			result.add(new PawnCaptureMoveGenerator(recorder, direction, square));
		}
		result.add(new PawnPushMoveGenerator(recorder, square));
		result.add(new EnPassantMoveGenerator(recorder, square, ss));
		return result;
	}

}
