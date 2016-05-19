package jp.gr.java_conf.syanidar.games.chess.rat;

public enum Rank {
	FIRST, SECOND, THIRD, FORTH, FIFTH, SIXTH, SEVENTH, EIGHTH;
	
	
	
	@Override
	public String toString(){
		return String.valueOf(ordinal() + 1);
	}
}
