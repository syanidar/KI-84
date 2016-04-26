package jp.gr.java_conf.syanidar.chess.hamster.materials;

import java.util.Optional;

public enum DirectionEnum {
	NORTH(0, 1),
	NORTH_NORTH_EAST(1, 2),
	NORTH_EAST(1, 1),
	EAST_NORTH_EAST(2, 1),
	EAST(1, 0),
	EAST_SOUTH_EAST(2, -1),
	SOUTH_EAST(1, -1),
	SOUTH_SOUTH_EAST(1, -2),
	SOUTH(0, -1),
	SOUTH_SOUTH_WEST(-1, -2),
	SOUTH_WEST(-1, -1),
	WEST_SOUTH_WEST(-2, -1),
	WEST(-1, 0),
	WEST_NORTH_WEST(-2, 1),
	NORTH_WEST(-1, 1),
	NORTH_NORTH_WEST(-1, 2);
	private final int columnIncrement;
	private final int rowIncrement;
	
	private DirectionEnum(int c, int r){
		columnIncrement = c;
		rowIncrement = r;
	}
	Optional<Coordinates> increment(Coordinates c){
		int column = c.column() + columnIncrement;
		if(column < 0 || 8 <= column)return Optional.empty();
		int row = c.row() + rowIncrement;
		if(row < 0 || 8 <= row)return Optional.empty();
		return Optional.of(new Coordinates(column, row));
	}
}
