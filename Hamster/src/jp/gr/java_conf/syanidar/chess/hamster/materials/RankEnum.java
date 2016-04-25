package jp.gr.java_conf.syanidar.chess.hamster.materials;

public enum RankEnum {
	FIRST(0), SECOND(1), THIRD(2), FORTH(3), FIFTH(4), SIXTH(5), SEVENTH(6), EIGHTH(7);
	private int row;
	private RankEnum(int row){
		this.row = row;
	}
	boolean involves(Coordinates c){
		return c.row() == row;
	}
}
