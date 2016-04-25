package jp.gr.java_conf.syanidar.chess.hamster.move;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.EAST;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.NORTH;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.NORTH_EAST;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.NORTH_WEST;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.SOUTH;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.SOUTH_EAST;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.SOUTH_WEST;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.WEST;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

class QueenState implements PieceState {
	private static final Set<DirectionEnum> directions = EnumSet.of(NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST);
	private final MoveRecorder recorder;
	private final Square square;
	QueenState(MoveRecorder r, Square s){
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
