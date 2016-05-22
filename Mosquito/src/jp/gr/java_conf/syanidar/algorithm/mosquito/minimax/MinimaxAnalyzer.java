package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Analyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;
import jp.gr.java_conf.syanidar.util.Counter;

public class MinimaxAnalyzer<P extends Position, E extends Evaluation<E>> implements Analyzer<P, MinimaxSetting, MinimaxResult<E>> {
	private final Evaluator<P, E> evaluator;
	private final Counter leafNodeCounter;
	private final Counter nodeCounter;
	private final Counter perftCounter;

	
	
	public MinimaxAnalyzer(Evaluator<P, E> evaluator){
		if(evaluator == null)throw new IllegalArgumentException();
		this.evaluator = evaluator;
		this.leafNodeCounter = new Counter();
		this.nodeCounter = new Counter();
		this.perftCounter = new Counter();
	}
	@Override
	public MinimaxResult<E> evaluate(P position, MinimaxSetting settings) {
		if(position == null || settings == null)throw new IllegalArgumentException("null");
		int depth = settings.depth();
		
		List<String> line = new ArrayList<>();
		initializeCounters();
		E evaluation = evaluate(position, depth, line).reverse();
		Collections.reverse(line);
		return new MinimaxResult<>(evaluation, line, leafNodeCounter.value(), nodeCounter.value(), perftCounter.value());
	}
	
	private E evaluate(P position, int depth, List<String> line){
		nodeCounter.tick();
		if(depth == 0){
			leafNodeCounter.tick();
			perftCounter.tick();
			return evaluator.evaluate(position, depth).reverseIf(!position.theFirstPlayerHasTheMove());
		}
		
		Iterator<? extends Move> iterator = position.moveIterator();
		if(!iterator.hasNext()){
			leafNodeCounter.tick();
			return evaluator.evaluate(position, depth).reverseIf(!position.theFirstPlayerHasTheMove());
		}
	
		E best = evaluator.lowerBound();
		while(iterator.hasNext()){
			Move move = iterator.next();
			move.play();
			List<String> newLine = line == null ? null : new ArrayList<>();
			E evaluation = evaluate(position, depth - 1, newLine).reverse();
			move.undo();
			best = evaluation.IfBetterThan(best, new LineUpdater(line, newLine, move));
		}
		return best;
	}
	private void initializeCounters(){
		leafNodeCounter.clear();
		nodeCounter.clear();
		perftCounter.clear();
	}
}
