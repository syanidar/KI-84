package jp.gr.java_conf.syanidar.chess.hamster.tools;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum.*;
import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.*;

import java.util.EnumSet;
import java.util.Set;

import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Coordinates;
import jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public class PawnTerritoryChecker extends KnightTerritoryChecker {
	private static final Set<DirectionEnum> directionsWhite = EnumSet.of(NORTH_EAST, NORTH_WEST);
	private static final Set<DirectionEnum> directionsBlack = EnumSet.of(SOUTH_EAST, SOUTH_WEST);
	
	private final ColorEnum color;
	public PawnTerritoryChecker(Square s, ColorEnum c) {
		super(s);
		assert c != null;
		color = c;
	}
	@Override
	public boolean pieceControls(Coordinates c) {
		return pieceControls(c, color == WHITE ? directionsWhite : directionsBlack);
	}
}
