package jp.gr.java_conf.syanidar.algorithm.mosquito.game;

import java.util.Map;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Result;

public interface Viewer <P extends Position<?>, R extends Result<R, ?>>{
	public void updateBoard(P position);
	public void updateResults(Map<String, R> results);
}
