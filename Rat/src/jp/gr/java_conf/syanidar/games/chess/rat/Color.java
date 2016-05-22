package jp.gr.java_conf.syanidar.games.chess.rat;

public enum Color {
	WHITE, BLACK;
	@Override
	public String toString() {
		return String.format("%s%s", name().substring(0, 1), name().substring(1).toLowerCase());
	}
	public Color opposite(){
		return this == WHITE? BLACK : WHITE;
	}
}
