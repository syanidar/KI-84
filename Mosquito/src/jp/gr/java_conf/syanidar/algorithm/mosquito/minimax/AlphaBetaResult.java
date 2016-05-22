package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import java.util.List;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Result;

public class AlphaBetaResult<E extends Evaluation<E>> implements Result<AlphaBetaResult<E>, E> {
	private final MinimaxResult<E> result;
	
	
	
	AlphaBetaResult(E evaluation, List<String> line, long leafNodes, long totalNodes){
		result = new MinimaxResult<>(evaluation, line, leafNodes, totalNodes, 0);
	}
	
	
	
	@Override
	public int compareTo(AlphaBetaResult<E> arg0) {
		return result.compareTo(arg0.result);
	}
	@Override
	public E evaluation() {
		return result.evaluation();
	}
	@Override
	public String toString() {
		return result.toString();
	}
	public List<String> expectedLine(){
		return result.expectedLine();
	}
	public long leafNodes(){
		return result.leafNodes();
	}
	public long totalNodes(){
		return totalNodes();
	}
}
