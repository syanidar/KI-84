package jp.gr.java_conf.syanidar.util.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Range implements Iterable<Integer> {
	private final int min;
	private final int max;
	
	private Range(int min, int max){
		this.min = min;
		this.max = max;
	}
	public static final Range of(int n){
		if(n <= 0)throw new IllegalArgumentException("n should be positive: " + n);
		return new Range(0, n);
	}
	public static final Range of(int min, int max){
		if(min > max)throw new IllegalArgumentException(min + ">" + max);
		return new Range(min, max);
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>(){
			private int current = min;
			
			@Override
			public boolean hasNext() {return current < max;}

			@Override
			public Integer next() {
				if (hasNext())return current++;
				else throw new NoSuchElementException();
			}
		};
	}
}
