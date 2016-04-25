package jp.gr.java_conf.syanidar.chess.hamster.move;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

class BishopState implements PieceState {
	private static final Set<DirectionEnum> directions = EnumSet.of(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
	private final MoveRecorder recorder;
	private final Square square;
	
	BishopState(MoveRecorder r, Square s){
		assert r != null;
		assert s != null && s.isOccupied();
		recorder = r;
		square = s;
	}
	@Override
	public List<MoveGenerator> moveGenerators() {
		assert square.isOccupied();
		List<MoveGenerator> result = new ArrayList<>();
		directions.forEach(d -> result.add(new RayMoveGenerator(recorder, d, square)));
		return result;
	}

}
