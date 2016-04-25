package jp.gr.java_conf.syanidar.algorithm.mosquito.game;

import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Position;

public interface NoMoveHandler<P extends Position<?>> {
	public void handle(P position);
}
