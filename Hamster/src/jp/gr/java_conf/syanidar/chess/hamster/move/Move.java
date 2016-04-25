package jp.gr.java_conf.syanidar.chess.hamster.move;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Piece;
import jp.gr.java_conf.syanidar.chess.hamster.materials.Square;

public class Move{
	private final List<Event> events;
	private final MoveRecorder recorder;

	Move(MoveRecorder m, List<Event> e){
		e.sort((e0, e1) -> e0.priority() - e1.priority());
		events = e;
		recorder = m;
	}
	Move(MoveRecorder m, Event first, Event... events){
		this.events = new ArrayList<>();
		this.events.add(first);
		for(Event e : events){
			this.events.add(e);
		}
		this.events.sort((e0, e1) -> e0.priority() - e1.priority());
		recorder = m;
	}
	public void play(){
		recorder.record(this);

		for(int i = 0, n = events.size(); i < n; i++){
			events.get(i).play();
		}
	}
	public void undo(){
		recorder.undo();

		for(int i = events.size() - 1; 0 <= i; i--){
			events.get(i).undo();
		}
	}
	public MoveNotation notation(){
		Square origin = null, destination = null;
		Piece piece = null, target = null, promotee = null, rook = null;
		boolean pieceHasMoved = false;
		for(Event e : events){
			if(e instanceof Walk){
				if (!pieceHasMoved) {
					piece = ((Walk) e).piece();
					origin = ((Walk) e).origin();
					destination = ((Walk) e).destination();
					pieceHasMoved = true;
				}else{
					rook = ((Walk)e).piece();
				}
			}else if(e instanceof Elimination){
				target = ((Elimination)e).target();
			}else if(e instanceof Promotion){
				promotee = ((Promotion)e).promotee();
			}
		}
		return new MoveNotation.MoveNotationBuilder(piece, origin, destination).target(target).promotee(promotee).rook(rook).build();
	}
	@Override
	public String toString(){
		return notation().toPureCoordinateNotation();
	}
}
