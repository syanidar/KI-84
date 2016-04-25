package jp.gr.java_conf.syanidar.chess.hamster.move;

interface Event {
	public void play();
	public void undo();
	public int priority();
}
