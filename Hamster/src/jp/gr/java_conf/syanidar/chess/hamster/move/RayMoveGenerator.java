package jp.gr.java_conf.syanidar.chess.hamster.move;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.DirectionEnum;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

class RayMoveGenerator implements MoveGenerator {
	private final MoveRecorder recorder;
	private final DirectionEnum direction;
	private final Square square;
	
	RayMoveGenerator(MoveRecorder r, DirectionEnum d, Square s){
		recorder = r;
		direction = d;
		square = s;
	}
	@Override
	public List<Move> generate(ColorEnum color) {
		if(color == null)throw new IllegalArgumentException();

		List<Move> result = new ArrayList<>();
		Optional<Square> next = square.next(direction);
		
		while(next.isPresent()){
			Optional<Square> current = next.filter(n -> !n.isOccupiedBy(color));
			Optional<List<Event>> events = Optional
					.ofNullable(current.isPresent() ? new ArrayList<>() : null);
			
			events.ifPresent(e -> {
				current.filter(c -> c.isOccupied())
				.ifPresent(n -> e.add(new Elimination(n)));
				
				e.add(new Walk(square, current.get()));
				result.add(new Move(recorder, e));
			});
			
			next = current.filter(n -> !n.isOccupied()).flatMap(n -> n.next(direction));
		}
		
		return result;
	}
}
