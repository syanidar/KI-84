package jp.gr.java_conf.syanidar.algorithm.mosquito.game;

import java.util.List;
import java.util.Optional;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;

public interface Player <P extends Position<?>>{
	public Optional<List<String>> play(P position, NoMoveHandler<P> handler);
}
