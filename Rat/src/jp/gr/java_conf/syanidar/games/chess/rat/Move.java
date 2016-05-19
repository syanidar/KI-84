package jp.gr.java_conf.syanidar.games.chess.rat;
import jp.gr.java_conf.syanidar.util.function.Undoable;

class Move implements jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move {	
	private final Undoable events;

	Move(Undoable e){
		events = e;
	}

	@Override
	public void play() {
		events.apply();
	}

	@Override
	public void undo() {
		events.undo();
	}
	@Override
	public String toString(){
		return events.toString();
	}
}
