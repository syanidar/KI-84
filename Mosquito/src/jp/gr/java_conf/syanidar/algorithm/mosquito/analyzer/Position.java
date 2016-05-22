package jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer;

import java.util.Iterator;
import java.util.List;

public interface Position{
	public List<? extends Move> moves();
	
	public boolean theFirstPlayerHasTheMove();
	
	default public Iterator<? extends Move> moveIterator(){
		return moves().iterator();
	}
	default public Iterator<? extends Move> moveIterator(boolean isMoveOrderingRequired){
		return moveIterator();
	}
	default public boolean isDrawForced(){
		return false;
	}
	default public boolean playerHasRightToDraw(){
		return false;
	}
}
