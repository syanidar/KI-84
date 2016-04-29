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
		E evaluation = evaluate(position, settings.depth(), settings.bound().reverse(), settings.predicate(), line).reverse();
		Collections.reverse(line);
		return new MinimaxResult<>(evaluation, line);
	}
	private E evaluate(P position, int depth, Bound<E> bound, Predicate<P> predicate,  List<String> line){
		if(predicate.test(position) || depth == 0){
			return evaluator.evaluate(position).reverseIf(!position.theFirstPlayerHasTheMove());
		}
		
		List<? extends Move> moves = position.moves();
		if(moves.size() == 0)return evaluator.evaluateIfTerminated(position, depth).reverseIf(!position.theFirstPlayerHasTheMove());
	
		E best = evaluator.lowerBound();
		for(Move move : moves){	
			move.play();
			List<String> newLine = new ArrayList<>();
			E evaluation = evaluate(position, depth - 1, bound.reverse(), predicate, newLine).reverse();
			move.undo();
			
			if(bound.isEqualToOrWorseThan(evaluation))return evaluation;
			bound.updateLower(evaluation);
			best = evaluation.IfBetterThan(best, new LineUpdater(line, newLine, depth, move));
	
		}
		return best;
	}
}
