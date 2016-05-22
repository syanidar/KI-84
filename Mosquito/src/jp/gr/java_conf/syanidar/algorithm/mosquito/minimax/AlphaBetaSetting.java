package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import java.util.function.Predicate;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Setting;

public class AlphaBetaSetting<P extends Position, E extends Evaluation<E>> implements Setting {
	public static final class Builder<P extends Position, E extends Evaluation<E>>{
		private final int depth;
		private final Evaluator<? super P, ? extends E> evaluator;
		private int maxDepthMoveOrderingApplied;
		
		public Builder(int depth, Evaluator<? super P, ? extends E> evaluator){
			this.depth = depth;
			this.evaluator = evaluator;
			
			this.maxDepthMoveOrderingApplied = depth;
		}
		public Builder<P, E> maxDepthMoveOrderingApplied(int maxDepthMoveOrderingApplied){
			this.maxDepthMoveOrderingApplied = maxDepthMoveOrderingApplied;
			return this;
		}
		public AlphaBetaSetting<P, E> build(){
			return new AlphaBetaSetting<>(this);
		}
	}
	private final int depth;
	private final int maxDepthMoveOrderingApplied;
	private final Evaluator<? super P, ? extends E> evaluator;
	private Bound<E> bound;

	
	
	private AlphaBetaSetting(Builder<P, E> b){
		this.depth = b.depth;
		this.maxDepthMoveOrderingApplied = b.maxDepthMoveOrderingApplied;
		this.evaluator = b.evaluator;
		this.bound = new Bound<E>(evaluator.lowerBound(), evaluator.upperBound());
	}
	public AlphaBetaSetting(int depth, Evaluator<? super P, ? extends E> evaluator){
		if(depth < 0)throw new IllegalArgumentException("depth = " + depth);
		if(evaluator == null)throw new IllegalArgumentException("evaluator = null");
		
		this.depth = depth;
		this.maxDepthMoveOrderingApplied = depth;
		this.evaluator = evaluator;
		this.bound = new Bound<E>(evaluator.lowerBound(), evaluator.upperBound());
	}
	public AlphaBetaSetting(int minDepth, int maxDepth, Evaluator<? super P, ? extends E> evaluator, Predicate<? super P> predicate){
		if(minDepth < 0)throw new IllegalArgumentException("min depth = " + minDepth);
		if(minDepth > maxDepth)throw new IllegalArgumentException("min depth = " + minDepth + ", max depth = " + maxDepth);
		this.depth = minDepth;
		this.maxDepthMoveOrderingApplied = maxDepth;
		this.evaluator = evaluator;
		this.bound = new Bound<E>(evaluator.lowerBound(), evaluator.upperBound());
	}

	
	
	Bound<E> bound(){
		return bound;
	}
	int depth(){
		return depth;
	}
	int maxDepthMoveOrderingApplied(){
		return maxDepthMoveOrderingApplied;
	}


	@Override
	public void initialize() {
		this.bound = new Bound<E>(evaluator.lowerBound(), evaluator.upperBound());
	}
}
