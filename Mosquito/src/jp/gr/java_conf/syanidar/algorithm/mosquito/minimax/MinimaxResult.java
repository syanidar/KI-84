package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Result;

public class MinimaxResult<E extends Evaluation<E>> implements Result<MinimaxResult<E>, E> {
	private final E evaluation;
	private final List<String> line;
	private final long leafNodes;
	private final long totalNodes;
	private final long perftNodes;
	
	
	
	public MinimaxResult(E evaluation, List<String> line, long leafNodes, long totalNodes, long perftNodes){
		this.evaluation = evaluation;
		this.line = line;
		this.leafNodes = leafNodes;
		this.totalNodes = totalNodes;
		this.perftNodes = perftNodes;
	}
	@Override
	public E evaluation() {
		return evaluation;
	}
	public long leafNodes(){
		return leafNodes;
	}
	public long totalNodes(){
		return totalNodes;
	}
	public long perftNodes(){
		return perftNodes;
	}
	public List<String> expectedLine(){
		return new ArrayList<>(line);
	}
	@Override
	public int compareTo(MinimaxResult<E> arg0) {
		return evaluation().compareTo(arg0.evaluation);
	}
	@Override
	public String toString(){
		return String.join("", "{", evaluation.toString(), ", ", line.toString(), "}");
	}
}
