package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import java.util.List;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move;

public class LineUpdater implements Runnable {
	private final List<String> line;
	private final List<String> newLine;
	private final Move move;
	
	LineUpdater(List<String> line, List<String> newLine, Move move){
		assert line != null && newLine != null && move != null;
		this.line = line;
		this.newLine = newLine;
		this.move = move;
	}
	@Override
	public void run() {		
		line.clear();
		line.addAll(newLine);
		line.add(move.toString());
	}

}
