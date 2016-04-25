package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Setting;

public class AlphaBetaSetting<E extends Evaluation<E>> implements Setting {
	private final int depth;
	private final Evaluator<?, E> evaluator;
	private Bound<E> bound;

	public AlphaBetaSetting(int depth, Evaluator<?, E> evaluator){
		if(depth < 0)throw new IllegalArgumentException("depth = " + depth);
		this.depth = depth;
		this.evaluator = evaluator;
		this.bound = new Bound<E>(evaluator.lowerBound(), evaluator.upperBound());
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
