package jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer;

public interface Evaluator<P extends Position<?>, E extends Evaluation<?>>{
		
	E evaluate(P position);
	E lowerBound();
	E upperBound();
	default E evaluateIfTerminated(P position, int depth){
		return evaluate(position);
	}
}
