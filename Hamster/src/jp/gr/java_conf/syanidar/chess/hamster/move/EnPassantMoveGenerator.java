package jp.gr.java_conf.syanidar.chess.hamster.move;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

class EnPassantMoveGenerator implements MoveGenerator {
	private final MoveRecorder recorder;
	private final Square square;
	private final ScoreSheet ss;
	
	EnPassantMoveGenerator(MoveRecorder m, Square s, ScoreSheet ss){
		recorder = m;
		square = s;
		this.ss = ss;
	}
	
	@Override
	public List<Move> generate(ColorEnum color) {
		if(color == null)throw new IllegalArgumentException();
		DirectionEnum direction = color == ColorEnum.WHITE ? DirectionEnum.NORTH : DirectionEnum.SOUTH; 
		List<Move> result = new ArrayList<>();
		
		DirectionEnum dirOfTheEnemy = DirectionEnum.EAST;
		for(int i = 0; i < 2; i++){
			square.next(dirOfTheEnemy)
			.filter(n -> n.isOccupiedBy(color.opposite()) && square.coordinates().row() == (color == ColorEnum.WHITE ? 4 : 3))
			.filter(n -> ss.InitialPawnPushHasBeenPlayedInvolving(n.piece().get()))
			.ifPresent(n -> result.add(new Move(recorder, new Elimination(n), new Walk(square, n.next(direction).get()))));

			dirOfTheEnemy = DirectionEnum.WEST;
		}
		return result;
	}
}
