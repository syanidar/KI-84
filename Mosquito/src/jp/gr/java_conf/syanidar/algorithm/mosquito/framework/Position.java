package jp.gr.java_conf.syanidar.algorithm.mosquito.framework;

import java.util.List;

public interface Position <M extends Move>{
	public List<M> moves();
	public void changeTheSide();
	public boolean theFirstPlayerHasTheMove();
}