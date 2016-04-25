package jp.gr.java_conf.syanidar.chess.hamster.move;

import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;

public interface LegalityTester {
	public boolean isLegal(Move move, ColorEnum color);
}
