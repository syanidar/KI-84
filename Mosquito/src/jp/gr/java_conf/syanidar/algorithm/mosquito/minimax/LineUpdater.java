package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import java.util.List;

import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Move;

public class LineUpdater implements Runnable {
	private final List<String> line;
	private final List<String> newLine;
	private final int depth;
	private final Move move;
	
	public LineUpdater(List<String> line, List<String> newLine, int depth, Move move){
		this.line = line;
		this.newLine = newLine;
		this.depth = depth;
		this.move = move;
	}
	@Override
	public void run() {
		if(line == null || newLine == null)return;
		
		if(depth == 1){
			line.clear();
			line.add(move.toString());
		}else{
			line.clear();
			line.addAll(newLine);
			line.add(move.toString());
		}
	}

}
