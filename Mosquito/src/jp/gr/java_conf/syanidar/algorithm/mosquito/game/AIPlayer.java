package jp.gr.java_conf.syanidar.algorithm.mosquito.game;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Analyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Result;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Setting;

public class AIPlayer<P extends Position, S extends Setting, E extends Evaluation<E>, R extends Result<R, E>> implements Player<P, R> {
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
		Iterator<? extends Move> moveIterator = position.moveIterator(true);
		if(!moveIterator.hasNext() || position.isDrawForced()){
			handler.handle(position);
			return map;
		}
		
		E best = evaluator.lowerBound();
		Move bestMove = null;
		while(moveIterator.hasNext()){
			Move move = moveIterator.next();
			move.play();
			R result = analyzer.evaluate(position, setting);
			move.undo();
			map.put(move.toString(), result);
			
			if(result.evaluation().isBetterThan(best)){
				best = result.evaluation();
				bestMove = move;
			}
		}
		if(evaluator.claimsDraw(best) && position.playerHasRightToDraw()){
			handler.handle(position);
			return map;
		}
		bestMove.play();
		
		
		return map;
	}
}
