package jp.gr.java_conf.syanidar.chess.hamster.game;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluation;

public final class CentiPawn implements Evaluation<CentiPawn>{
	public static final CentiPawn ZERO = new CentiPawn(0);
	public static final CentiPawn MAX_VALUE = new CentiPawn(Integer.MAX_VALUE / 2);
	public static final CentiPawn MIN_VALUE = new CentiPawn(-Integer.MAX_VALUE / 2);
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
	@Override
	public int compareTo(CentiPawn arg0) {
		return arg0.value - value;
	}
}
