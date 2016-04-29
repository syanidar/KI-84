package jp.gr.java_conf.syanidar.algorithm.mosquito.game;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Result;
import jp.gr.java_conf.syanidar.utils.collection.ListUtility;

public class HumanPlayer<P extends Position<?>, R extends Result<R, ?>> implements Player<P, R> {
	private final MoveSelector selector;
	public HumanPlayer(MoveSelector selector){
		this.selector = selector;
	}
	@Override
	public Map<String, R> play(P position, NoMoveHandler<P> handler) {
		Map<String, R> map = new TreeMap<>();
		List<? extends Move> moves = position.moves();
		int size = moves.size();
		if(size == 0){
			handler.handle(position);
			return map;
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
		return map;
	}
}
