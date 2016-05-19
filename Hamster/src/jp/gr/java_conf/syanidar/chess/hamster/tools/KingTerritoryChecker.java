package jp.gr.java_conf.syanidar.chess.hamster.tools;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.EAST;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.NORTH;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.NORTH_EAST;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.NORTH_WEST;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.SOUTH;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.SOUTH_EAST;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.SOUTH_WEST;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.WEST;

import java.util.EnumSet;
import java.util.Set;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Coordinates;
import jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public class KingTerritoryChecker extends KnightTerritoryChecker {
	private static final Set<DirectionEnum> directions = EnumSet.of(NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST);

	public KingTerritoryChecker(Square s) {
		super(s);
	}
	@Override
	public boolean pieceControls(Coordinates c) {
		return pieceControls(c, directions);
	}

}
