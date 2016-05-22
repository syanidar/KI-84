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

public class AlphaBetaAnalyzer<P extends Position, E extends Evaluation<E>> implements Analyzer<P, AlphaBetaSetting<P, E>, AlphaBetaResult<E>> {
	private static final class Params<P extends Position, E extends Evaluation<E>>{
		private final P position;
		private final int depth;
		private final int maxDepthMoveOrderingApplied;
		private final Bound<E> bound;
		
		
		
		Params(P position, int depth, int maxDepthMoveOrderingApplied, Bound<E> bound){
			this.position = position;
			this.depth = depth;
			this.maxDepthMoveOrderingApplied = maxDepthMoveOrderingApplied;
			this.bound = bound;
		}
		
		
		
		Params<P, E> next(){
			return new Params<>(position, depth - 1, maxDepthMoveOrderingApplied - 1, bound.reverse());
		}
	}
	
	
	
	
	
	
	
	
	
	
	private final Evaluator<P, E> evaluator;
	private final Counter leafNodeCounter;
	private final Counter nodeCounter;
	
	
	
	public AlphaBetaAnalyzer(Evaluator<P, E> evaluator){
		if(evaluator == null)throw new IllegalArgumentException();
		this.evaluator = evaluator;
		this.leafNodeCounter = new Counter();
		this.nodeCounter = new Counter();
	}
	@Override
	public AlphaBetaResult<E> evaluate(P position, AlphaBetaSetting<P, E> settings) {
		if(position == null || settings == null)throw new IllegalArgumentException("null");
		List<String> line = new ArrayList<>();
		initializeCounters();
		Params<P, E> params = new Params<>(position, settings.depth(), settings.maxDepthMoveOrderingApplied(), settings.bound().reverse());
		E evaluation = evaluate(params, line).reverse();
		Collections.reverse(line);
		return new AlphaBetaResult<>(evaluation, line, leafNodeCounter.value(), nodeCounter.value());
	}
	private E evaluate(Params<P, E> params, List<String> line){
		P position = params.position;
		int depth = params.depth;
		nodeCounter.tick();
		if(depth == 0){	
			leafNodeCounter.tick();
			return evaluator.evaluate(position, depth).reverseIf(!position.theFirstPlayerHasTheMove());
		}
		Iterator<? extends Move> iterator = position.moveIterator(params.maxDepthMoveOrderingApplied >= 0);
		if(!iterator.hasNext())return evaluator.evaluate(position, depth).reverseIf(!position.theFirstPlayerHasTheMove());
		E best = evaluator.lowerBound();

		while(iterator.hasNext()){	
			Move move = iterator.next();
			move.play();
			List<String> newLine = new ArrayList<>();
			E evaluation = evaluate(params.next(), newLine).reverse();
			move.undo();
			
			if(params.bound.isEqualToOrWorseThan(evaluation))return evaluation;
			params.bound.updateLower(evaluation);
			best = evaluation.IfBetterThan(best, new LineUpdater(line, newLine, move));
	
		}
		
		if(evaluator.claimsDraw(best) && position.playerHasRightToDraw()){
			return evaluator.drawEvaluation();
		}
		return best;
	}
	private void initializeCounters(){
		leafNodeCounter.clear();
		nodeCounter.clear();
	}
}
