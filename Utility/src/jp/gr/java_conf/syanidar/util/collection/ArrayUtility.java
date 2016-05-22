package jp.gr.java_conf.syanidar.util.collection;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;


public final class ArrayUtility {
	private ArrayUtility(){}
	
	public static final <T0, T1> void sort(T0[] list0, T1[] list1, Comparator<T0> comp){
		int length = list0.length;
		if(length != list1.length)throw new IllegalArgumentException("list0.length = " + length + ", list1.length = " + list1.length);
		
		for (int i= 0, n = list0.length; i < n; i++){
			for (int j = i; j > 0 && comp.compare(list0[j - 1], list0[j]) > 0; j--){
            	T0 removed0 = list0[j - 1];
            	T1 removed1 = list1[j - 1];
            	list0[j - 1] = list0[j];
            	list1[j - 1] = list1[j];
            	list0[j] = removed0;
            	list1[j] = removed1;
            }
		}
	}
	@SuppressWarnings("unchecked")
	public static final <T0, T1> void sort(T0[] list0, T1[] list1){
		int length = list0.length;
		if(length != list1.length)throw new IllegalArgumentException("list0.length = " + length + ", list1.length = " + list1.length);
		
		for (int i= 0, n = list0.length; i < n; i++){
			for (int j = i; j > 0 && ((Comparable<T0>)list0[j - 1]).compareTo(list0[j]) > 0; j--){
            	T0 removed0 = list0[j - 1];
            	T1 removed1 = list1[j - 1];
            	list0[j - 1] = list0[j];
            	list1[j - 1] = list1[j];
            	list0[j] = removed0;
            	list1[j] = removed1;
            }
		}
	}
	public static final <T> T[] filter(T[] array, Predicate<? super T> predicate){
		int index = 0;
		for(int i = 0, n = array.length; i < n; i++){
			if(predicate.test(array[i])){array[index++] = array[i];}
		}
		@SuppressWarnings("unchecked")
		T[] result = (T[])Array.newInstance(array.getClass().getComponentType(), index);
		System.arraycopy(array, 0, result, 0, index);
		return result;
	}
	public static final void main(String[] args){
		Integer[] array = {0, 1, 2, 0, 4, 4, 3, 0, 1, 2};
		System.out.println(Arrays.toString(filter(array, i -> i == 0)));
	}
}
