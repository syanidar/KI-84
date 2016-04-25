package jp.gr.java_conf.syanidar.utils.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

public class ListUtility {
	private ListUtility(){}
	
	public static final <T, R> List<R> map(List<T> input, Function<T, R> func){
		List<R> result = new ArrayList<>();
		for(T t : input){
			result.add(func.apply(t));
		}
		return result;
	}
	@SuppressWarnings("unchecked")
	public static final <T0, T1> void sort(List<T0> list0, List<T1> list1){
		Object[] array0 = list0.toArray();
		Object[] array1 = list1.toArray();
		
		ArrayUtility.sort(array0, array1);
		int length = array0.length;
		
		ListIterator<T0> iterator0 = list0.listIterator();
		for(int i = 0; i < length; i++){
			iterator0.next();
			iterator0.set((T0)array0[i]);
		}
		
		ListIterator<T1> iterator1 = list1.listIterator();
		for(int i = 0; i < length; i++){
			iterator1.next();
			iterator1.set((T1)array1[i]);
		}
	}
}
