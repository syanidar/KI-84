package jp.gr.java_conf.syanidar.algorithm.mosquito.game;

import jp.gr.java_conf.syanidar.algorithm.mosquito.analyzer.Position;

public class Game <P extends Position<?>>{
	private final P position;
	private final Player<P> first;
	private final Player<P> second;
	private final Viewer<P> viewer;
	
	private Player<P> current;
	
	public Game(P position, Player<P> first, Player<P> second, Viewer<P> viewer){
		this.position = position;
		this.first = first;
		this.second = second;
		this.viewer = viewer;
		current = first;
	}
	public void play(NoMoveHandler<P> handler){
		viewer.drawBoard(position);
		viewer.drawComputedLine(current.play(position, handler));
		changeTheSide();
	}
	public void changeTheSide(){
		current = current == first ? second : first;
	}
}
