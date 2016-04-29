package jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer;

public interface Evaluation <E extends Evaluation<E>> extends Comparable<E>{
	public E reverse();
	public E reverseIf(boolean condition);
	public boolean isBetterThan(E evaluation);
	@SuppressWarnings("unchecked")
	public default E IfBetterThan(E evaluation, Runnable runnable){
		if(this.isBetterThan(evaluation)){
			runnable.run();
			return (E)this;
		}
		return evaluation;
	}
}
