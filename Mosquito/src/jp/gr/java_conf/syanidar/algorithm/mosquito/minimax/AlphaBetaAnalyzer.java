package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.LookAheadAnalyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Move;
import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Position;

public class AlphaBetaAnalyzer<P extends Position<?>, E extends Evaluation<E>> implements LookAheadAnalyzer<P, AlphaBetaSetting, E> {
	private Evaluator<P, E> evaluator;
	
	public AlphaBetaAnalyzer(Evaluator<P, E> evaluator){
		if(evaluator == null)throw new IllegalArgumentException();
		this.evaluator = evaluator;
	}
	@Override
	public E evaluate(P position, AlphaBetaSetting settings) {
		if(position == null || settings == null)throw new IllegalArgumentException("null");
		int depth = settings.depth();
		
		return evaluate(position, depth, new Bound<E>(evaluator.lowerBound(), evaluator.upperBound()), null).reverse();
	}
	@Override
	public E evaluate(P position, AlphaBetaSetting settings, List<String> line){
		if(position == null || settings == null || line == null)throw new IllegalArgumentException("null");

		int depth = settings.depth();
		E result = evaluate(position, depth, new Bound<E>(evaluator.lowerBound(), evaluator.upperBound()), line).reverse();
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
