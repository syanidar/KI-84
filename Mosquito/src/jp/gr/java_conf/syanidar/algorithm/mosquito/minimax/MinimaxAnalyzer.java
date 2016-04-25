package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.LookAheadAnalyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;

public class MinimaxAnalyzer<P extends Position<?>, E extends Evaluation<E>> implements LookAheadAnalyzer<P, MinimaxSetting, E> {
	private Evaluator<P, E> evaluator;
	public MinimaxAnalyzer(Evaluator<P, E> evaluator){
		if(evaluator == null)throw new IllegalArgumentException();
		this.evaluator = evaluator;
	}
	@Override
	public E evaluate(P position, MinimaxSetting settings) {
		if(position == null || settings == null)throw new IllegalArgumentException("null");
		int depth = settings.depth();
		
		return evaluate(position, depth, null).reverse();
	}
	@Override
	public E evaluate(P position, MinimaxSetting settings, List<String> sequence){
		if(position == null || settings == null || sequence == null)throw new IllegalArgumentException("null");

		int depth = settings.depth();
		E result = evaluate(position, depth, sequence).reverse();
		Collections.reverse(sequence);
		return result;
	}
	
	private E evaluate(P position, int depth, List<String> line){
		if(depth == 0)return evaluator.evaluate(position).reverseIf(!position.theFirstPlayerHasTheMove());
		
		List<? extends Move> moves = position.moves();
		if(moves.size() == 0)return evaluator.evaluateIfTerminated(position, depth).reverseIf(!position.theFirstPlayerHasTheMove());
	
		E best = evaluator.lowerBound();
		for(Move move : moves){	
			move.play();
			List<String> newLine = line == null ? null : new ArrayList<>();
			E evaluation = evaluate(position, depth - 1, newLine).reverse();
			move.undo();
			best = evaluation.IfBetterThan(best, new LineUpdater(line, newLine, depth, move));
		}
		return best;
	}
}
