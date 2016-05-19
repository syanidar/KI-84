package jp.gr.java_conf.syanidar.util.test;

import java.util.concurrent.TimeUnit;

public class Timer {
	private long time;
	private Runnable task;
	
	
	
	public Timer(Runnable task){
		this.task = task;
	}
	
	
	
	public void start(){
		long startTime = System.nanoTime();
		task.run();
		time = System.nanoTime() - startTime;
	}
	public long estimatedTime(TimeUnit unit){
		
		return unit.convert(time, TimeUnit.NANOSECONDS);
	}
}
