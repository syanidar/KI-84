package jp.gr.java_conf.syanidar.util.bitwise;
import java.util.Iterator;

public class Bits implements Iterable<Long> {
	final long bits;
	private Bits(long bits){
		this.bits = bits;
	}
	public static final Bits of(long bits){
		return new Bits(bits);
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
				current &= current - 1;
				
				return result;
			}
			
		};
	}
	
	public static final boolean intersects(long l1, long l2){
		return (l1 & l2) != 0;
	}
}
