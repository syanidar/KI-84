package jp.gr.java_conf.syanidar.util.collection;

import java.util.Iterator;

public class Random implements Iterable<Long>{
	private final java.util.Random random;
	private final int size;
	private final int andTimes;
	private final boolean isInfinite;
	private Random(int size, int andTimes, boolean isInfinite){
		random = new java.util.Random();
		this.size = size;
		this.andTimes = andTimes;
		this.isInfinite = isInfinite;
	}
	public static final Random infinite(){
		return new Random(Integer.MAX_VALUE, 0, true);
	}
	public static final Random infiniteLowNumOfBits(int n){
		if(n < 1)throw new IllegalArgumentException("n should be positive.");
		return new Random(Integer.MAX_VALUE, n, true);
	}
	public static final Random times(int times){
		if(times <= 0)throw new IllegalArgumentException("times should be positive.");
		return new Random(times, 0, false);
	}
	public static final Random timesLowNumOfBits(int times, int n){
		if(n < 1)throw new IllegalArgumentException("n should be positive.");
		if(times <= 0)throw new IllegalArgumentException("times should be positive.");
		return new Random(times, n, false);
	}
	
	@Override
	public Iterator<Long> iterator() {
		return new Iterator<Long>(){
			private int current = 0;
			@Override
			public boolean hasNext() {
				if(isInfinite)return true;
				return current < size;
			}

			@Override
			public Long next() {
				current++;
				long result = random.nextLong();
				for(int i = 0; i < andTimes; i++)result &= random.nextLong();
				return result;
			}
		};
	}
	
}
