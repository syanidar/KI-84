package jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer;

public interface Evaluator<P extends Position, E extends Evaluation<E>>{		
	E evaluate(P position, int depth);
	E lowerBound();
	E upperBound();

	default E drawEvaluation(){
		throw new UnsupportedOperationException();
	}
	default boolean claimsDraw(E evaluation) {
		return false;
	}
}
