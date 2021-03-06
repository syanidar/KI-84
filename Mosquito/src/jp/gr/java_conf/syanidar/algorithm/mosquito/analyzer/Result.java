package jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer;

public interface Result <R extends Result<R, E>, E extends Evaluation<E>> extends Comparable<R>{
	public E evaluation();
}
