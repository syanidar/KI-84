package jp.gr.java_conf.syanidar.util;

public class Counter {
	private long count = 0;
	
	
	public Counter(){}
	public Counter(long initValue){
		count = initValue;
	}
	public void tick(){
		if(count == Long.MAX_VALUE)throw new IllegalStateException("Please don't tick any more. That's illegal. ;)");
		count++;
	}
	public void tickIf(boolean condition){
		if(condition){
			if(count == Long.MAX_VALUE)throw new IllegalStateException("Please don't tick any more. That's illegal. ;)");
			count++;
		}
	}
	public long value(){
		return count;
	}
	public void clear(){
		count = 0;
	}
}
