package jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer;

import java.util.function.Function;

public interface Evaluator<P extends Position<?>, E extends Evaluation<?>> extends Function<P, E>{
	@Override
	public default E apply(P position){return evaluate(position);}
	
	E evaluate(P position);
	E lowerBound();
	E upperBound();
	default E evaluateIfTerminated(P position, int depth){
		return evaluate(position);
	}
}
