package jp.gr.java_conf.syanidar.games.chess.rat;
import java.util.Comparator;


public class DefaultMoveOrderer implements Comparator<Position.RawMove> {
	private static final Comparator<Position.RawMove> INSTANCE = new DefaultMoveOrderer();
	
	private DefaultMoveOrderer(){}
	
	static final Comparator<Position.RawMove> getInstance(){
		return INSTANCE;
	}
	@Override
	public int compare(Position.RawMove m0, Position.RawMove m1) {
		int result = 0;
		if(m1.captures()){result += 1000;}
		if(m1.materialGain() - m1.materialOfThePiece() > 0){result += 100;}
		if(m0.captures()){result -= 1000;}
		if(m0.materialGain() - m0.materialOfThePiece() > 0){result -= 100;}
		return result;
	}
}
