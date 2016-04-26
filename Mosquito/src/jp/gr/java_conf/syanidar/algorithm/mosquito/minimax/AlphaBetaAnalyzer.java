package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.LookAheadAnalyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;

public class AlphaBetaAnalyzer<P extends Position<?>, E extends Evaluation<E>> implements LookAheadAnalyzer<P, AlphaBetaSetting<E>, E> {
	private Evaluator<P, E> evaluator;
	
	public AlphaBetaAnalyzer(Evaluator<P, E> evaluator){
		if(evaluator == null)throw new IllegalArgumentException();
		this.evaluator = evaluator;
	}
	@Override
	public E evaluate(P position, AlphaBetaSetting<E> settings) {
		if(position == null || settings == null)throw new IllegalArgumentException("null");
		
		return evaluate(position, settings.depth(), settings.bound(), null).reverse();
	}
	@Override
	public E evaluate(P position, AlphaBetaSetting<E> settings, List<String> line){
		if(position == null || settings == null || line == null)throw new IllegalArgumentException("null");
		
		E result = evaluate(position, settings.depth(), settings.bound().reverse(), line).reverse();
		Collections.reverse(line);
		return result;
	}
	
	private E evaluate(P position, int depth, Bound<E> bound, List<String> line){
		if(depth == 0)return evaluator.evaluate(position).reverseIf(!position.theFirstPlayerHasTheMove());
		
		List<? extends Move> moves = position.moves();
		if(moves.size() == 0)return evaluator.evaluateIfTerminated(position, depth).reverseIf(!position.theFirstPlayerHasTheMove());
	
		E best = evaluator.lowerBound();
		for(Move move : moves){	
			move.play();
			List<String> newLine = line == null ? null : new ArrayList<>();
			E evaluation = evaluate(position, depth - 1, bound.reverse(), newLine).reverse();
			move.undo();
			
			if(bound.isEqualToOrWorseThan(evaluation))return evaluation;
			bound.updateLower(evaluation);
			best = evaluation.IfBetterThan(best, new LineUpdater(line, newLine, depth, move));
	
		}
		return best;
	}
}
