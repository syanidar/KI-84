package jp.gr.java_conf.syanidar.games.chess.rat;

public enum Piece {
	PAWN("P"), KNIGHT("N"), BISHOP("B"), ROOK("R"), QUEEN("Q"), KING("K");
	
	
	
	private String symbol;
	
	
	
	Piece(String symbol){this.symbol = symbol;}
	
	
	
	@Override
	public String toString(){
		return symbol;
	}

}
