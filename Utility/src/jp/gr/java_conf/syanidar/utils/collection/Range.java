package jp.gr.java_conf.syanidar.utils.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Range implements Iterable<Integer> {
	private final int max;
	
	public Range(int max){
		this.max = max;
	}
	public static final Range range(int max){
		return new Range(max);
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>(){
			private int current = 0;
			
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
