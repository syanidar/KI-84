package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Result;

public class MinimaxResult<E extends Evaluation<E>> implements Result<MinimaxResult<E>, E> {
	private final E evalution;
	private final List<String> line;
	public MinimaxResult(E evaluation, List<String> line){
		this.evalution = evaluation;
		this.line = line;
	}
	@Override
	public E evaluation() {
		return evalution;
	}
	public List<String> expectedLine(){
		return new ArrayList<>(line);
	}
}
