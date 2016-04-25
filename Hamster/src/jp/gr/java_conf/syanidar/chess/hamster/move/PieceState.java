package jp.gr.java_conf.syanidar.chess.hamster.move;

import java.util.List;

public interface PieceState {
	List<MoveGenerator> moveGenerators();
}
