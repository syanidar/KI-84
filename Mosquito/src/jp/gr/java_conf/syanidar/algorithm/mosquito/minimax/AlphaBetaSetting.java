package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import java.util.function.Predicate;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Setting;

public class AlphaBetaSetting<P extends Position<?>, E extends Evaluation<E>> implements Setting {
	private final int minDepth;
	private final int maxDepth;
	private final Evaluator<?, E> evaluator;
	private final Predicate<P> predicate;
	private Bound<E> bound;

	public AlphaBetaSetting(int minDepth, Evaluator<?, E> evaluator){
		if(minDepth < 0)throw new IllegalArgumentException("depth = " + minDepth);
		this.minDepth = minDepth;
		this.maxDepth = Integer.MAX_VALUE;
		this.evaluator = evaluator;
		this.bound = new Bound<E>(evaluator.lowerBound(), evaluator.upperBound());
		this.predicate = p -> true;
	}
	public AlphaBetaSetting(int minDepth, int maxDepth, Evaluator<?, E> evaluator, Predicate<P> predicate){
		if(minDepth < 0)throw new IllegalArgumentException("depth = " + minDepth);
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
		this.evaluator = evaluator;
		this.bound = new Bound<E>(evaluator.lowerBound(), evaluator.upperBound());
		this.predicate = predicate;
	}
	Predicate<P> predicate(){
		return predicate;
	}
	Bound<E> bound(){
		return bound;
	}
	int minDepth(){
		return minDepth;
	}
	int maxDepth(){
		return maxDepth;
	}

	@Override
	public void initialize() {
		this.bound = new Bound<E>(evaluator.lowerBound(), evaluator.upperBound());
	}
}
