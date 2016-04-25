package jp.gr.java_conf.syanidar.algorithm.mosquito.game;

import java.util.List;
import java.util.Optional;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;

public interface Viewer <P extends Position<?>>{
	public void drawBoard(P position);
	public void drawComputedLine(Optional<List<String>> line);
}
