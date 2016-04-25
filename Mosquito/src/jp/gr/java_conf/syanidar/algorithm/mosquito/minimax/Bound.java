package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Evaluation;

public class Bound <E extends Evaluation<E>>{
	private E lower;
	private final E upper;
	
	Bound(E lower, E upper){
		this.lower = lower;
		this.upper = upper;
	}
	void updateLower(E evaluation){		
		if(evaluation.isBetterThan(lower)){
			lower = evaluation;
		}
	}
	boolean isEqualToOrWorseThan(E evaluation){
		return !upper.isBetterThan(evaluation);
	}
	Bound<E> reverse(){
		return new Bound<E>(upper.reverse(), lower.reverse());
	}
}
