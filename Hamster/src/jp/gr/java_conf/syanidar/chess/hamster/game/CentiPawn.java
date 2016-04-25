package jp.gr.java_conf.syanidar.chess.hamster.game;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;

public final class CentiPawn implements Evaluation<CentiPawn>{
	private final int value;
	
	public CentiPawn(int value){
		this.value = value;
	}
	@Override
	public CentiPawn reverse(){
		return new CentiPawn(-value);
	}
	@Override
	public CentiPawn reverseIf(boolean condition){
		return condition ? reverse() : this;
	}
	@Override
	public boolean isBetterThan(CentiPawn evaluation){
		return value - evaluation.value > 0;
	}
	@Override
	public String toString(){
		return "Evaluation:" + String.valueOf(value);
	}
}
