package jp.gr.java_conf.syanidar.algorithm.mosquito.game;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Analyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Result;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Setting;

public class AIPlayer<P extends Position<?>, S extends Setting, E extends Evaluation<E>, R extends Result<R, E>> implements Player<P, R> {
	private final Analyzer<P, S, R> analyzer;
	private final S setting;
	private final Evaluator<P, E> evaluator;
	
	
	public AIPlayer(Analyzer<P, S, R> analyzer, S setting, Evaluator<P, E> evaluator){
		this.analyzer = analyzer;
		this.setting = setting;
		this.evaluator = evaluator;
	}
	@Override
	public Map<String, R> play(P position, NoMoveHandler<P> handler) {
		Map<String, R> map = new TreeMap<>();
		setting.initialize();
		List<? extends Move> moves = position.moves();
		if(moves.size() == 0){
			handler.handle(position);
			return map;
		}
		
		E best = evaluator.lowerBound();
		Move bestMove = null;
		for(Move move : moves){
			move.play();
			R result = analyzer.evaluate(position, setting);
			move.undo();
			map.put(move.toString(), result);
			
			if(result.evaluation().isBetterThan(best)){
				best = result.evaluation();
				bestMove = move;
			}
		}
		bestMove.play();
		
		
		return map;
	}
}
