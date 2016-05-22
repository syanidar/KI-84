package jp.gr.java_conf.syanidar.chess.hamster.game;

import java.util.concurrent.TimeUnit;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Move;
import jp.gr.java_conf.syanidar.util.test.Timer;

public class Perft implements Runnable {
	private long counter;
	private final ChessPosition position;
	private final int depth;
	
	
	
	public Perft(ChessPosition position, int depth){
		this.position = position;
		this.depth = depth;
	}
	
	
	
	@Override
	public void run() {
		counter = perft(position, depth);
	}
	public final long numOfLeafNodes(){
		return counter;
	}
	
	
	
	public static final long perft(ChessPosition p, int depth){
		if(depth == 0)return 1;
		int result = 0;
		for(Move move : p.moves()){
			move.play();
			result += perft(p, depth - 1);
			move.undo();
		}
		
		return result;
	}
	public static final void main(String[] args){
		ChessPosition position = new ChessPosition();
		int depth = 5;
		Perft perft = new Perft(position, depth);
		Timer timer = new Timer(perft);
		timer.start();
		System.out.printf("The number of leaf nodes at the depth of %d was %d.\n", depth, perft.numOfLeafNodes());
		System.out.printf("It took %d seconds to complete.\n", timer.estimatedTime(TimeUnit.SECONDS));
	}
}
