package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Analyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;

public class MinimaxAnalyzer<P extends Position<?>, E extends Evaluation<E>> implements Analyzer<P, MinimaxSetting, MinimaxResult<E>> {
	private Evaluator<P, E> evaluator;
	public MinimaxAnalyzer(Evaluator<P, E> evaluator){
		if(evaluator == null)throw new IllegalArgumentException();
		this.evaluator = evaluator;
	}
	@Override
	public MinimaxResult<E> evaluate(P position, MinimaxSetting settings) {
		if(position == null || settings == null)throw new IllegalArgumentException("null");
		int depth = settings.depth();
		
		List<String> line = new ArrayList<>();
		E evaluation = evaluate(position, depth, line).reverse();
		Collections.reverse(line);
		return new MinimaxResult<>(evaluation, line);
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
