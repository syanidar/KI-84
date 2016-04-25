package jp.gr.java_conf.syanidar.chess.hamster.tools;

import static jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum.*;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Coordinates;
import jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public class BishopTeritoryChecker implements PieceTeritoryChecker {
	private static final Set<DirectionEnum> directions = EnumSet.of(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
	private final Square square;
	
	public BishopTeritoryChecker(Square s){square = s;}
	
	@Override
	public boolean pieceControls(Coordinates c) {
		return pieceControls(c, directions);
	}
	final boolean pieceControls(Coordinates c, Set<DirectionEnum> d){
		assert c != null;
		assert d != null;
		
		for(DirectionEnum direction : d){
			Optional<Square> next = square.next(direction);
			
			while(next.isPresent()){
				next = next.filter(n -> !n.equals(c));
				if(!next.isPresent())return true;
				next = next.filter(n -> !n.isOccupied());
				next = next.flatMap(n -> n.next(direction));
			}
		}
		return false;
	}
}
