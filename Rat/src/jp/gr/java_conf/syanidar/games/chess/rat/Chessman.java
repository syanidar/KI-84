package jp.gr.java_conf.syanidar.games.chess.rat;

public final class Chessman {
	private final Color color;
	private final Piece piece;
	
	
	
	Chessman(Color color, Piece piece){
		this.color = color;
		this.piece = piece;
	}
	
	
	
	public Color color(){return color;}
	public Piece piece(){return piece;}
	@Override
	public String toString(){
		return color == Color.WHITE? piece.toString() : piece.toString().toLowerCase();
	}
}
