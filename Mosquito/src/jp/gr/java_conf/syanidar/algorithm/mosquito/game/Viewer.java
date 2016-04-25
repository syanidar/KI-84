package jp.gr.java_conf.syanidar.algorithm.mosquito.game;

import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Position;
import java.util.List;
import java.util.Optional;

public interface Viewer <P extends Position<?>>{
	public void drawBoard(P position);
	public void drawComputedLine(Optional<List<String>> line);
}
