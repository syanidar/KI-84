package jp.gr.java_conf.syanidar.algorithm.mosquito.game;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;
import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Result;

public class Game <P extends Position, R extends Result<R, ?>>{
	private final P position;
	private final Player<P, R> first;
	private final Player<P, R> second;
	private final Viewer<P, R> viewer;
	
	private Player<P, R> current;
	
	public Game(P position, Player<P, R> first, Player<P, R> second, Viewer<P, R> viewer){
		this.position = position;
		this.first = first;
		this.second = second;
		this.viewer = viewer;
		current = first;
	}
	public void play(NoMoveHandler<P> handler){
		viewer.updateBoard(position);
		viewer.updateResults(current.play(position, handler));
		changeTheSide();
	}
	private void changeTheSide(){
		current = current == first ? second : first;
	}
}
