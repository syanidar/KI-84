package jp.gr.java_conf.syanidar.chess.hamster.tools;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.*;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Coordinates;
import jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public class KnightTerritoryChecker extends PieceTerritoryChecker {
	private static final Set<DirectionEnum> directions = EnumSet.of(NORTH_NORTH_EAST, EAST_NORTH_EAST, EAST_SOUTH_EAST, SOUTH_SOUTH_EAST, SOUTH_SOUTH_WEST, WEST_SOUTH_WEST, WEST_NORTH_WEST, NORTH_NORTH_WEST);
	
	private final Square square;
	
	public KnightTerritoryChecker(Square s){square = s;}
	
	@Override
	public boolean pieceControls(Coordinates c) {
		return pieceControls(c, directions);
	}
	final boolean pieceControls(Coordinates c, Set<DirectionEnum> d){
		for(DirectionEnum direction : d){
			Optional<Square> next = square.next(direction);
			if(next.filter(n -> n.isAt(c)).isPresent())return true;
		}
		return false;
	}
}
