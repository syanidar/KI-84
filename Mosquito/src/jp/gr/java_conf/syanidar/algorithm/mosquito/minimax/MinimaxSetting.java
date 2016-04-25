package jp.gr.java_conf.syanidar.algorithm.mosquito.minimax;

import jp.gr.java_conf.syanidar.algorithm.mosquito.framework.Setting;

public class MinimaxSetting implements Setting {
	private final int depth;
	public MinimaxSetting(int depth){
		if(depth < 0)throw new IllegalArgumentException("depth = " + depth);
		this.depth = depth;
	}
	
	int depth(){
		return depth;
	}

	@Override
	public void initialize() {		
	}
}
