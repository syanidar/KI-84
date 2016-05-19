package jp.gr.java_conf.syanidar.util.collection;

import java.util.Comparator;


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
}
