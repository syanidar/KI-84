package jp.gr.java_conf.syanidar.chess.hamster.move;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

class KnightState implements PieceState {
	private static final Set<DirectionEnum> directions = EnumSet.of(NORTH_NORTH_EAST, EAST_NORTH_EAST, EAST_SOUTH_EAST, SOUTH_SOUTH_EAST, SOUTH_SOUTH_WEST, WEST_SOUTH_WEST, WEST_NORTH_WEST, NORTH_NORTH_WEST);
	private final MoveRecorder recorder;
	private final Square square;
	
	KnightState(MoveRecorder r, Square s){
		assert r != null;
		assert s != null && s.isOccupied();
		recorder = r;
		square = s;
	}
	@Override
	public List<MoveGenerator> moveGenerators() {
		assert square.isOccupied();
		List<MoveGenerator> result = new ArrayList<>();
		directions.forEach(d -> result.add(new StepMoveGenerator(recorder, d, square)));
		return result;
	}

}
