package jp.gr.java_conf.syanidar.chess.hamster.materials;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;

public enum DirectionEnum {
	NORTH{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r + 1;}
	},
	NORTH_NORTH_EAST{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c + 1;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r + 2;}
	},
	NORTH_EAST{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c + 1;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r + 1;}
	},
	EAST_NORTH_EAST{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c + 2;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r + 1;}
	},
	EAST{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c + 1;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r;}
	},
	EAST_SOUTH_EAST{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c + 2;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r - 1;}
	},
	SOUTH_EAST{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c + 1;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r - 1;}
	},
	SOUTH_SOUTH_EAST{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c + 1;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r - 2;}
	},
	SOUTH{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r - 1;}
	},
	SOUTH_SOUTH_WEST{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c - 1;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r - 2;}
	},
	SOUTH_WEST{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c - 1;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r - 1;}
	},
	WEST_SOUTH_WEST{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c - 2;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r - 1;}
	},
	WEST{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c - 1;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r;}
	},
	WEST_NORTH_WEST{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c - 2;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r + 1;}
	},
	NORTH_WEST{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c - 1;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r + 1;}
	},
	NORTH_NORTH_WEST{
		@Override
		IntUnaryOperator columnIncrement() {return c -> c - 1;}
		@Override
		IntUnaryOperator rowIncrement() {return r -> r + 2;}
	};
	abstract IntUnaryOperator columnIncrement();
	abstract IntUnaryOperator rowIncrement();
	Function<Coordinates, Optional<Coordinates>> increment(){
		return c -> {
			Coordinates result = null;
			int column = columnIncrement().applyAsInt(c.column());
			int row = rowIncrement().applyAsInt(c.row());
			if(0 <= column && column < 8 && 0 <= row && row < 8){
				result = new Coordinates(column, row);
			}
			return Optional.ofNullable(result);
		};
	}
}
