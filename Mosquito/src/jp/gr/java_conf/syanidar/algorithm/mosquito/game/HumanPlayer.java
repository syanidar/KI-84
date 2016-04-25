package jp.gr.java_conf.syanidar.algorithm.mosquito.game;

import java.util.List;
import java.util.Optional;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;
import jp.gr.java_conf.syanidar.utils.collection.ListUtility;

public class HumanPlayer<P extends Position<?>> implements Player<P> {
	private final MoveSelector selector;
	public HumanPlayer(MoveSelector selector){
		this.selector = selector;
	}
	@Override
	public Optional<List<String>> play(P position, NoMoveHandler<P> handler) {
		List<? extends Move> moves = position.moves();
		int size = moves.size();
		if(size == 0){
			handler.handle(position);
			return Optional.empty();
		}
		List<String> moveStrings = ListUtility.map(moves, m -> m.toString());
		String input = null;
		while(!moveStrings.contains(input)){
			input = selector.select(moveStrings);
		}
		
		for(int i = 0; i < size; i++){
			if(moves.get(i).toString().equals(input)){
				moves.get(i).play();
				break;
			}
		}
		return Optional.empty();
	}
}
