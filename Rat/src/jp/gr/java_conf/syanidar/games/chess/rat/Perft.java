package jp.gr.java_conf.syanidar.games.chess.rat;

import java.util.concurrent.TimeUnit;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Evaluator;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.MinimaxAnalyzer;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.MinimaxResult;
import jp.gr.java_conf.syanidar.algorithm.mosquito.minimax.MinimaxSetting;
import jp.gr.java_conf.syanidar.chess.hamster.game.CentiPawn;
import jp.gr.java_conf.syanidar.util.test.Timer;

public class Perft implements Runnable {
	private long counter;
	private final Position position;
	private final int depth;
	
	
	
	public Perft(Position position, int depth){
		this.position = position;
		this.depth = depth;
	}
	
	
	
	@Override
	public void run() {
		MinimaxAnalyzer<Position, CentiPawn> analyzer = 
				new MinimaxAnalyzer<Position, CentiPawn>(new Evaluator<Position, CentiPawn>(){
			@Override
			public CentiPawn evaluate(Position position, int depth) {return CentiPawn.ZERO;}
			@Override
			public CentiPawn lowerBound(){return CentiPawn.MIN_VALUE;}
			@Override
			public CentiPawn upperBound(){return CentiPawn.MAX_VALUE;}
		});
		MinimaxResult<CentiPawn> result = analyzer.evaluate(position, new MinimaxSetting(depth));
		counter = result.perftNodes();
	}
	public final long numOfLeafNodes(){
		return counter;
	}

	
	
	public static final void main(String[] args){
		int depth = 4;
		Position position = new Position();
		System.out.println(position);
		Perft perft = new Perft(position, depth);
		Timer timer = new Timer(perft);
		timer.start();
		System.out.printf("The number of leaf nodes at the depth of %d was %d.\n", depth, perft.numOfLeafNodes());
		System.out.printf("It took %d seconds to complete.\n", timer.estimatedTime(TimeUnit.SECONDS));
	}
}
