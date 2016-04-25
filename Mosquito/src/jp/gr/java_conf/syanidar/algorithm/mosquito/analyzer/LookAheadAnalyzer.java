package jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer;

import java.util.List;

public interface LookAheadAnalyzer<P extends Position<?>, S extends Setting, E extends Evaluation<?>> extends Analyzer<P, S, E> {
	public E evaluate(P position, S setting, List<String> line);
}
