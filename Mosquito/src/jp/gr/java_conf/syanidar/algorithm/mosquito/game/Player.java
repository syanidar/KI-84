package jp.gr.java_conf.syanidar.algorithm.mosquito.game;


import java.util.Map;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Result;

public interface Player <P extends Position<?>, R extends Result<R, ?>>{
	public Map<String, R> play(P position, NoMoveHandler<P> handler);
}
