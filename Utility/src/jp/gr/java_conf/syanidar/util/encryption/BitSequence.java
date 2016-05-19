package jp.gr.java_conf.syanidar.util.encryption;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import jp.gr.java_conf.syanidar.util.bitwise.Bits;
import jp.gr.java_conf.syanidar.util.collection.Range;

public class BitSequence implements Iterable<Boolean>{
	private final boolean firstBit;
	private boolean currentBit;
	private int successiveBitCount;
	private final Deque<Integer> successiveBitCounts;
	
	
	
	
	
	
	
	
	
	public BitSequence(){
		this.firstBit = true;
		this.currentBit = firstBit;
		this.successiveBitCount = 1;
		this.successiveBitCounts = new LinkedList<>();
	}
	public BitSequence(boolean firstBit){
		this.firstBit = firstBit;
		this.currentBit = firstBit;
		this.successiveBitCount = 1;
		this.successiveBitCounts = new LinkedList<>();
	}
	public BitSequence(BitSequence b){
		this.firstBit = b.firstBit;
		this.currentBit = b.currentBit;
		this.successiveBitCount = b.successiveBitCount;
		this.successiveBitCounts = new LinkedList<>(b.successiveBitCounts);
	}
	public static final BitSequence of(String s){
		if(s == null)throw new IllegalArgumentException("arg = null");
		StringTokenizer st = new StringTokenizer(s);
		BitSequence result = new BitSequence(true);
		while(st.hasMoreTokens()){
			result.successiveBitCounts.add(Integer.valueOf(st.nextToken()));
		}
		result.successiveBitCount = result.successiveBitCounts.removeLast();
		return result;
	}
	public static final BitSequence of(long l){
		long firstBit = Long.MIN_VALUE;
		BitSequence result = new BitSequence(Bits.intersects(l, firstBit));
		for(int i : Range.of(1, 64)){
			result.append(Bits.intersects(l, firstBit >>> i));
		}
		return result;
	}
	public static final BitSequence of(int input){
		int firstBit = Integer.MIN_VALUE;
		BitSequence result = new BitSequence(Bits.intersects(input, firstBit));
		for(int i : Range.of(1, 32)){
			result.append(Bits.intersects(input, firstBit >>> i));
		}
		return result;
	}
	public static final BitSequence of(byte b){
		byte firstBit = Byte.MIN_VALUE;
		BitSequence result = new BitSequence(Bits.intersects(b, firstBit));
		for(int i : Range.of(1, 8)){
			result.append(Bits.intersects(b, (firstBit & 0xFF) >>> i));
		}
		return result;
	}
	public static final BitSequence of(BigInteger bi){
		if(bi.signum() != 1)throw new IllegalArgumentException("arg <= 0");
		int bitLength = bi.bitLength();
		BigInteger firstBit = BigInteger.ONE.shiftLeft(bitLength - 1);
		BitSequence result = new BitSequence(true);
		for(int i : Range.of(1, bitLength)){
			result.append(!bi.and(firstBit.shiftRight(i)).equals(BigInteger.ZERO));
		}
		return result;
	}
	public void append(boolean b){
		if(currentBit != b){
			successiveBitCounts.addLast(successiveBitCount);
			currentBit = b;
			successiveBitCount = 1;
		}else{
			successiveBitCount += 1;
		}
	}
	public void append(int successiveBitCount){
		if(successiveBitCount <= 0)throw new IllegalArgumentException("arg <= 0");
		currentBit = ! currentBit;
		successiveBitCounts.addLast(this.successiveBitCount);
		this.successiveBitCount = successiveBitCount;
	}
	public void removeLast(){
		if(successiveBitCount == 1){
			if(successiveBitCounts.size() == 0)
				throw new IllegalStateException("Only one bit is present.");
			currentBit = !currentBit;
			successiveBitCount = successiveBitCounts.removeLast();
		}else{
			successiveBitCount--;
		}
	}
	public boolean get(int index){
		int sum = 0;
		boolean bitIsSet = firstBit;
		for(int i : successiveBitCounts){
			sum += i;
			bitIsSet = !bitIsSet;
			if(index < sum)return !bitIsSet;
		}
		if(index < sum + successiveBitCount)return bitIsSet;
		
		throw new IndexOutOfBoundsException();
	}
	public boolean firstBitIsSet(){
		return firstBit;
	}
	public int[] successiveBitCounts(){
		int size = successiveBitCounts.size();
		int[] result = new int[size + 1];
		int index = 0;
		for(int i : successiveBitCounts){
			result[index] = i;
			index++;
		}
		result[size] = successiveBitCount;
		return result;
	}
	public BigInteger value(){
		BigInteger result = BigInteger.ZERO;
		BigInteger currentBit = BigInteger.ONE.shiftLeft(bitLength() - 1);
		boolean bitIsSet = firstBit;
		for(int i : successiveBitCounts){
			for(int j = 0; j < i; j++){
				if(bitIsSet){
					result = result.or(currentBit);
				}
				currentBit = currentBit.shiftRight(1);
			}
			bitIsSet = !bitIsSet;
		}
		if(bitIsSet){
			for(int i =0; i <successiveBitCount; i++){
				result = result.or(currentBit);
				currentBit = currentBit.shiftRight(1);
			}
		}
		return result;
	}
	public int bitLength(){
		int result = 0;
		for(int i : successiveBitCounts){
			result += i;
		}
		result += successiveBitCount;
		return result;
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		sb.append(firstBit);
		sb.append(", {");
		for(int i : successiveBitCounts){
			sb.append(i);
			sb.append(", ");
		}
		sb.append(successiveBitCount);
		sb.append("})");
		return sb.toString();
	}
	@Override
	public Iterator<Boolean> iterator() {
		return new Iterator<Boolean>(){
			private int currentIndex = 0;
			
			@Override
			public boolean hasNext() {
				return currentIndex < bitLength() - 1;
			}
			@Override
			public Boolean next() {
				return get(currentIndex++);
			}
		};
	}
	public static final void main(String[] args){
		int[] input = {1, 8, 7, 8, 2};
		BitSequence bs = new BitSequence(true);
		for(int i : input){
			bs.append(i);
		}
		System.out.println(Arrays.toString(input));
		System.out.println(bs.value());
		System.out.println(Arrays.toString(bs.successiveBitCounts()));
	}
}
