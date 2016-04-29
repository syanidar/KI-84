package jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer;

import java.util.function.BiFunction;

public interface Analyzer<P extends Position<?>, S extends Setting, R extends Result<R, ?>> extends BiFunction<P, S, R>{
	default R apply(P position, S setting){
		return evaluate(position, setting);
	}
	public R evaluate(P position, S setting);
}
