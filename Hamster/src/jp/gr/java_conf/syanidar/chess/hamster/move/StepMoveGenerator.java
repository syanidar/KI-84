package jp.gr.java_conf.syanidar.chess.hamster.move;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

class StepMoveGenerator implements MoveGenerator {
	private final MoveRecorder recorder;
	private final DirectionEnum direction;
	private final Square square;
	
	StepMoveGenerator(MoveRecorder r, DirectionEnum d, Square s){
		recorder = r;
		direction = d;
		square = s;
	}
	@Override
	public List<Move> generate(ColorEnum color) {
		if(color == null)throw new IllegalArgumentException();

		Optional<Square> next = square
				.next(direction)
				.filter(n -> !n.isOccupiedBy(color));
		
		Optional<List<Event>> events = Optional
				.ofNullable(next.isPresent() ? new ArrayList<Event>() : null);

		List<Move> result = new ArrayList<>();
		events.ifPresent(e -> {
			next.filter(n -> n.isOccupied())
			.ifPresent(n -> e.add(new Elimination(n)));
			next.ifPresent(n -> e.add(new Walk(square, n)));
			
			result.add(new Move(recorder, e));
		});
				
		return result;
	}
}
