package jp.gr.java_conf.syanidar.chess.hamster.move;

import java.util.List;

import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;

public interface LegalMoveGenerator extends MoveGenerator{
	public List<Move> generateLegalMoves(ColorEnum c);
}
