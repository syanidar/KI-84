package jp.gr.java_conf.syanidar.util.bitwise;
import java.util.Iterator;

public class LongBits implements Iterable<Long> {
	private final long bits;
	
	
	
	private LongBits(long bits){
		this.bits = bits;
	}
	public static final LongBits of(long bits){
		return new LongBits(bits);
	}
	
	
	
	@Override
	public Iterator<Long> iterator() {
		return new Iterator<Long>(){
			private long current = bits;
			@Override
			public boolean hasNext() {
				return current != 0;
			}

			@Override
			public Long next() {
				long result = Long.lowestOneBit(current);
				current -= result;
				
				return result;
			}
			
		};
	}
	public Iterator<Long> highestToLowest(){
		return new Iterator<Long>(){
			private long current = bits;
			@Override
			public boolean hasNext() {
				return current != 0;
			}

			@Override
			public Long next() {
				long result = Long.highestOneBit(current);
				current -= result;
				
				return result;
			}
			
		};
	}
	
	
	
	public static final boolean intersects(long l1, long l2){
		return (l1 & l2) != 0;
	}
}
