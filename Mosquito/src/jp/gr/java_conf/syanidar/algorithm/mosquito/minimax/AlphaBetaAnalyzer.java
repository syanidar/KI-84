package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Analyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;

public class AlphaBetaAnalyzer<P extends Position<?>, E extends Evaluation<E>> implements Analyzer<P, AlphaBetaSetting<P, E>, MinimaxResult<E>> {
	private Evaluator<P, E> evaluator;
	
	public AlphaBetaAnalyzer(Evaluator<P, E> evaluator){
		if(evaluator == null)throw new IllegalArgumentException();
		this.evaluator = evaluator;
	}
	@Override
	public MinimaxResult<E> evaluate(P position, AlphaBetaSetting<P, E> settings) {
		if(position == null || settings == null)throw new IllegalArgumentException("null");
		List<String> line = new ArrayList<>();
		E evaluation = evaluate(position, settings.minDepth(), settings.maxDepth(), settings.bound().reverse(), settings.predicate(), line).reverse();
		Collections.reverse(line);
		return new MinimaxResult<>(evaluation, line);
	}
	private E evaluate(P position, int minDepth, int maxDepth, Bound<E> bound, Predicate<P> predicate,  List<String> line){
		if(maxDepth == 0 || (minDepth <= 0 && predicate.test(position))){
			return evaluator.evaluate(position).reverseIf(!position.theFirstPlayerHasTheMove());
		}
		
		List<? extends Move> moves = position.moves();
		if(moves.size() == 0)return evaluator.evaluateIfTerminated(position, minDepth).reverseIf(!position.theFirstPlayerHasTheMove());
	
		E best = evaluator.lowerBound();
		for(Move move : moves){	
			move.play();
			List<String> newLine = new ArrayList<>();
			E evaluation = evaluate(position, minDepth - 1, maxDepth - 1, bound.reverse(), predicate, newLine).reverse();
			move.undo();
			
			if(bound.isEqualToOrWorseThan(evaluation))return evaluation;
			bound.updateLower(evaluation);
			best = evaluation.IfBetterThan(best, new LineUpdater(line, newLine, move));
	
		}
		return best;
	}
}
