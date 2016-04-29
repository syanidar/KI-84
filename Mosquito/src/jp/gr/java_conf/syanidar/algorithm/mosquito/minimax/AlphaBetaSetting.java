package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import java.util.function.Predicate;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Setting;

public class AlphaBetaSetting<P extends Position<?>, E extends Evaluation<E>> implements Setting {
	private final int depth;
	private final Evaluator<?, E> evaluator;
	private final Predicate<P> predicate;
	private Bound<E> bound;

	public AlphaBetaSetting(int depth, Evaluator<?, E> evaluator){
		if(depth < 0)throw new IllegalArgumentException("depth = " + depth);
		this.depth = depth;
		this.evaluator = evaluator;
		this.bound = new Bound<E>(evaluator.lowerBound(), evaluator.upperBound());
		this.predicate = p -> false;
	}
	public AlphaBetaSetting(int depth, Evaluator<?, E> evaluator, Predicate<P> predicate){
		if(depth < 0)throw new IllegalArgumentException("depth = " + depth);
		this.depth = depth;
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
	int depth(){
		return depth;
	}

	@Override
	public void initialize() {
		this.bound = new Bound<E>(evaluator.lowerBound(), evaluator.upperBound());
	}
}
