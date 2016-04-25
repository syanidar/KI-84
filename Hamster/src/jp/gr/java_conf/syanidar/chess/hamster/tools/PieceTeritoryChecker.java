package jp.gr.java_conf.syanidar.chess.hamster.tools;

import jp.gr.java_conf.syanidar.chess.hamster.materials.Coordinates;

public interface PieceTeritoryChecker {
	public boolean pieceControls(Coordinates c);
}
