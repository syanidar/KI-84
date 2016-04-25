package jp.gr.java_conf.syanidar.algorithm.mosquito.framework;

import java.util.function.BiFunction;

public interface Analyzer<P extends Position<?>, S extends Setting, E extends Evaluation<?>> extends BiFunction<P, S, E>{
	default E apply(P position, S setting){
		return evaluate(position, setting);
	}
	public E evaluate(P position, S setting);
}
