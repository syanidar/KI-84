package jp.gr.java_conf.syanidar.chess.hamster.move;

import jp.gr.java_conf.syanidar.chess.hamster.materials.ColorEnum;
import jp.gr.java_conf.syanidar.chess.hamster.tools.AttackDetector;

public class DefaultLegalityTester implements LegalityTester {
	private final AttackDetector ad;
	public DefaultLegalityTester(AttackDetector ad){
		this.ad = ad;
	}
	@Override
	public boolean isLegal(Move move, ColorEnum color) {
		move.play();
		boolean isLegal = !ad.piecesCheckTheKingOf(color);
		move.undo();
		return isLegal;
	}

}
