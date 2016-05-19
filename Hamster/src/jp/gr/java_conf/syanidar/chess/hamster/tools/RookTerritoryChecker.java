package jp.gr.java_conf.syanidar.chess.hamster.tools;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.*;

import java.util.EnumSet;
import java.util.Set;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Coordinates;
import jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public class RookTerritoryChecker extends BishopTerritoryChecker {
	private static final Set<DirectionEnum> directions = EnumSet.of(NORTH, EAST, SOUTH, WEST);

	public RookTerritoryChecker(Square s) {
		super(s);
	}
	@Override
	public boolean pieceControls(Coordinates c) {
		return pieceControls(c, directions);
	}
}
