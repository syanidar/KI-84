package jp.gr.java_conf.syanidar.games.chess.rat;

public enum File {
	A, B, C, D, E, F, G, H;
	
	
	
	@Override
	public String toString(){
		return name().toLowerCase();
	}
}
