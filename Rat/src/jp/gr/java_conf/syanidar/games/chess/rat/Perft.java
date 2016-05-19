package jp.gr.java_conf.syanidar.games.chess.rat;

import java.util.concurrent.TimeUnit;

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
		counter = perft(position, depth);
	}
	public final long numOfLeafNodes(){
		return counter;
	}
	
	
	
	public static final long perft(Position p, int depth){
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
		Position position = new Position();
		Perft perft = new Perft(position, 6);
		Timer timer = new Timer(perft);
		timer.start();
		System.out.printf("The number of leaf nodes was %d.\n", perft.numOfLeafNodes());
		System.out.printf("It took %d seconds to complete.\n", timer.estimatedTime(TimeUnit.SECONDS));
	}
}
