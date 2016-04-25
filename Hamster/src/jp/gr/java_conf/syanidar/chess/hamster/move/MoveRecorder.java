package jp.gr.java_conf.syanidar.chess.hamster.move;

interface MoveRecorder {
	public void record(Move move);
	public void undo();
}
