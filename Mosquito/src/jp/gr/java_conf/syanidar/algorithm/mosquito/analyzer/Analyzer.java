package jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer;

public interface Analyzer<P extends Position<?>, S extends Setting, R extends Result<R, ?>>{
	default R apply(P position, S setting){
		return evaluate(position, setting);
	}
	public R evaluate(P position, S setting);
}
